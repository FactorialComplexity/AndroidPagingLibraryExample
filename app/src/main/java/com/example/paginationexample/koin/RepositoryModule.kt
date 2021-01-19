package com.example.paginationexample.koin

import com.example.data.repository.CatsRepositoryImpl
import com.example.domain.repository.CatsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<CatsRepository> { CatsRepositoryImpl(get(), get()) }
}
