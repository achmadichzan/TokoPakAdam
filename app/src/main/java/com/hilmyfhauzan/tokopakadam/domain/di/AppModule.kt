package com.hilmyfhauzan.tokopakadam.domain.di

import androidx.room.Room
import com.hilmyfhauzan.tokopakadam.data.local.AppDatabase
import com.hilmyfhauzan.tokopakadam.data.repository.TransactionRepositoryImpl
import com.hilmyfhauzan.tokopakadam.domain.repository.TransactionRepository
import com.hilmyfhauzan.tokopakadam.domain.usecase.GetTransactionsUseCase
import com.hilmyfhauzan.tokopakadam.domain.usecase.InsertTransactionUseCase
import com.hilmyfhauzan.tokopakadam.presentation.viewmodel.MainViewModel
import com.hilmyfhauzan.tokopakadam.presentation.viewmodel.HistoryViewModel
import com.hilmyfhauzan.tokopakadam.presentation.viewmodel.ThemeViewModel
import com.hilmyfhauzan.tokopakadam.domain.repository.UserPreferencesRepository
import com.hilmyfhauzan.tokopakadam.data.repository.UserPreferencesRepositoryImpl
import com.hilmyfhauzan.tokopakadam.domain.usecase.DeleteTransactionUseCase
import com.hilmyfhauzan.tokopakadam.domain.usecase.UpdateTransactionUseCase
import com.hilmyfhauzan.tokopakadam.domain.usecase.GetThemeUseCase
import com.hilmyfhauzan.tokopakadam.domain.usecase.SetThemeUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    // --- 1. Database & DAO (Singleton: Hidup selama aplikasi berjalan) ---
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "toko_pak_adam.db" // Nama file database di HP
        )
            .fallbackToDestructiveMigration(false) // Reset DB jika struktur berubah (aman untuk dev awal)
            .build()
    }

    // Provide DAO dari Database instance
    single { get<AppDatabase>().transactionDao() }

    // --- 2. Repository (Singleton) ---
    // singleOf dengan bind<> otomatis menghubungkan Interface ke Implementation
    singleOf(::TransactionRepositoryImpl) { bind<TransactionRepository>() }

    // --- 3. Use Cases (Factory: Dibuat baru setiap kali dibutuhkan ViewModel) ---
    // factoryOf otomatis mencari dependency yang dibutuhkan di constructor UseCase
    factoryOf(::GetTransactionsUseCase)
    factoryOf(::InsertTransactionUseCase)
    factoryOf(::DeleteTransactionUseCase)
    factoryOf(::UpdateTransactionUseCase)
    factoryOf(::GetThemeUseCase)
    factoryOf(::SetThemeUseCase)

    // --- 4. User Preferences ---
    single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(androidContext()) }

    // Nanti ViewModel juga didaftarkan di sini
    viewModelOf(::MainViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::ThemeViewModel)
}