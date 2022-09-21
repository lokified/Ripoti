package com.loki.ripoti.di

import com.loki.ripoti.data.remote.RipotiApi
import com.loki.ripoti.data.repository.OnboardingRepositoryImpl
import com.loki.ripoti.data.repository.ReportsRepositoryImpl
import com.loki.ripoti.domain.repository.OnBoardingRepository
import com.loki.ripoti.domain.repository.ReportsRepository
import com.loki.ripoti.domain.useCases.auth.AuthUseCase
import com.loki.ripoti.domain.useCases.auth.login.LoginUserUseCase
import com.loki.ripoti.domain.useCases.auth.registration.RegisterUserUseCase
import com.loki.ripoti.domain.useCases.reports.*
import com.loki.ripoti.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRipotiApi(): RipotiApi {

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RipotiApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOnBoardingRepository(api: RipotiApi): OnBoardingRepository {
        return OnboardingRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideReportsRepository(api: RipotiApi): ReportsRepository {
        return ReportsRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideAuthUseCase(repository: OnBoardingRepository): AuthUseCase {

        return AuthUseCase(
            loginUser = LoginUserUseCase(repository),
            registerUser = RegisterUserUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideReportsUseCase(repository: ReportsRepository): ReportsUseCase {

        return ReportsUseCase(
            addReport = AddReportUseCase(repository),
            getReports = GetReportsUseCase(repository),
            updateReport = UpdateReportUseCase(repository),
            deleteReport = DeleteReportUseCase(repository)
        )
    }
}