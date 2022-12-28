package com.loki.ripoti.di

import android.app.Application
import androidx.room.Room
import com.loki.ripoti.data.local.UserDatabase
import com.loki.ripoti.data.remote.RipotiApi
import com.loki.ripoti.data.repository.UserRepositoryImpl
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.domain.repository.UserRepository
import com.loki.ripoti.domain.useCases.user.UpdateUserPasswordUseCase
import com.loki.ripoti.domain.useCases.user.UpdateUserUseCase
import com.loki.ripoti.domain.useCases.user.UserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserDatabase(app: Application): UserDatabase {

        return Room.databaseBuilder(
            app,
            UserDatabase::class.java,
            UserDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(database: UserDatabase, ripotiApi: RipotiApi): UserRepository {
        return UserRepositoryImpl(database.userDao, ripotiApi )
    }


    @Provides
    @Singleton
    fun provideUserUseCase(repository: UserRepository): UserUseCase {
        return UserUseCase(
            updateUser = UpdateUserUseCase(repository),
            updateUserPassword = UpdateUserPasswordUseCase(repository)
        )
    }
}