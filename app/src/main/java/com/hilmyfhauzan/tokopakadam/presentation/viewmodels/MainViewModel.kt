package com.hilmyfhauzan.tokopakadam.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmyfhauzan.tokopakadam.domain.model.ProductType
import com.hilmyfhauzan.tokopakadam.domain.model.Transaction
import com.hilmyfhauzan.tokopakadam.domain.model.TransactionItem
import com.hilmyfhauzan.tokopakadam.domain.usecase.InsertTransactionUseCase
import com.hilmyfhauzan.tokopakadam.presentation.state.ActiveInput
import com.hilmyfhauzan.tokopakadam.presentation.state.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val insertTransactionUseCase: InsertTransactionUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    // --- EVENT HANDLERS (Fungsi yang dipanggil dari UI) ---

    // 1. Saat Tab Produk (Telur Kecil/Sedang/Besar) diklik
    fun onProductSelected(type: ProductType) {
        _uiState.update { it.copy(selectedProduct = type, isLastInputFromNumpad = false) }
    }

    fun setActiveInput(input: ActiveInput) {
        _uiState.update { it.copy(activeInput = input) }
    }

    // 2. Logika Numpad (Angka 0-9, 00, 000)
    fun onNumpadClick(digit: String) {
        when (_uiState.value.activeInput) {
            ActiveInput.QUANTITY -> {
                updateCurrentQty { currentQtyString ->
                    if (currentQtyString == "0") digit else currentQtyString + digit
                }
                _uiState.update { it.copy(isLastInputFromNumpad = true) }
            }
            ActiveInput.CASH -> {
                updateCashInput { currentCashString ->
                    if (currentCashString == "0") digit else currentCashString + digit
                }
            }
        }
    }

    // 3. Tombol Hapus (Backspace)
    fun onBackspaceClick() {
        when (_uiState.value.activeInput) {
            ActiveInput.QUANTITY -> {
                updateCurrentQty { currentString ->
                    if (currentString.length > 1) currentString.dropLast(1) else "0"
                }
                _uiState.update { it.copy(isLastInputFromNumpad = true) }
            }
            ActiveInput.CASH -> {
                updateCashInput { currentCashString ->
                    if (currentCashString.length > 1) currentCashString.dropLast(1) else "0"
                }
            }
        }
    }

    // 4. Tombol "C" (Clear All untuk produk terpilih)
    fun onClearClick() {
        when (_uiState.value.activeInput) {
            ActiveInput.QUANTITY -> {
                updateQtyValue(0.0)
                _uiState.update { it.copy(isLastInputFromNumpad = false) }
            }
            ActiveInput.CASH -> _uiState.update { it.copy(cashInput = 0L) }
        }
    }

    fun clearAllTransaction() {
        _uiState.update { it.copy(quantities = emptyMap(), cashInput = 0L) }
    }

    // 5. Tombol Shortcut "1/2 Rak"
    fun onHalfTrayClick() {
        if (uiState.value.selectedProduct.isEgg) {
            val currentQty = uiState.value.quantities[uiState.value.selectedProduct] ?: 0.0
            val newQty =
                    if (uiState.value.isLastInputFromNumpad) {
                        currentQty * 15.0
                    } else {
                        currentQty + 15.0
                    }
            updateQtyValue(newQty)
            _uiState.update { it.copy(isLastInputFromNumpad = false) }
        }
    }

    // 6. Tombol Shortcut "1 Rak" (Reset jadi 1)
    fun onOneTrayClick() {
        if (uiState.value.selectedProduct.isEgg) {
            val currentQty = uiState.value.quantities[uiState.value.selectedProduct] ?: 0.0
            val newQty =
                    if (uiState.value.isLastInputFromNumpad) {
                        currentQty * 30.0
                    } else {
                        currentQty + 30.0
                    }
            updateQtyValue(newQty)
            _uiState.update { it.copy(isLastInputFromNumpad = false) }
        }
    }

    // 7. Input Uang Tunai (Diketik manual di TextField "Tunai")
    fun onCashInputChange(value: String) {
        val longValue = value.filter { it.isDigit() }.toLongOrNull() ?: 0L
        _uiState.update { it.copy(cashInput = longValue) }
    }

    // 8. Simpan Transaksi
    fun saveTransaction(customerName: String? = null) {
        val currentState = _uiState.value
        if (currentState.totalBelanja == 0L) return // Jangan simpan kalau kosong

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Konversi Map Quantity ke List<TransactionItem> Domain
            val items =
                    currentState.quantities.filter { it.value > 0 }.map { (type, qty) ->
                        TransactionItem(
                                productType = type,
                                quantity = qty,
                                unit = type.defaultUnit(),
                                pricePerUnit = type.pricePerPiece, // Ambil langsung dari Enum
                                subTotal = (qty * type.pricePerPiece).toLong()
                        )
                    }

            val transaction =
                    Transaction(
                            timestamp = System.currentTimeMillis(),
                            customerName = customerName?.ifBlank { null }, // Handle string kosong
                            items = items,
                            totalPrice = currentState.totalBelanja,
                            amountPaid = currentState.cashInput,
                            note = null // Bisa ditambah kalau ada field catatan
                    )

            try {
                insertTransactionUseCase(transaction)
                // Reset UI setelah sukses
                _uiState.update {
                    MainUiState(isTransactionSaved = true) // Reset ke state awal + flag success
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    // Reset flag success agar Toast tidak muncul terus menerus
    fun onTransactionSavedConsumed() {
        _uiState.update { it.copy(isTransactionSaved = false) }
    }

    // --- PRIVATE HELPER ---

    // Mengupdate quantity untuk produk yang SEDANG dipilih
    private fun updateCurrentQty(transform: (String) -> String) {
        val currentType = _uiState.value.selectedProduct
        val currentQty = _uiState.value.quantities[currentType] ?: 0.0

        // Ubah double ke string tanpa .0 (misal 5.0 jadi "5")
        val currentString =
                if (currentQty % 1.0 == 0.0) {
                    currentQty.toLong().toString()
                } else {
                    currentQty.toString()
                }

        val newString = transform(currentString)
        val newQty = newString.toDoubleOrNull() ?: 0.0

        updateQtyValue(newQty)
    }

    private fun updateQtyValue(value: Double) {
        _uiState.update { state ->
            // Update map quantities
            val newQuantities =
                    state.quantities.toMutableMap().apply { put(state.selectedProduct, value) }
            state.copy(quantities = newQuantities)
        }
    }

    private fun updateCashInput(transform: (String) -> String) {
        val currentCash = _uiState.value.cashInput.toString()
        val newCashString = transform(currentCash)
        val newCash = newCashString.toLongOrNull() ?: 0L
        _uiState.update { it.copy(cashInput = newCash) }
    }
}
