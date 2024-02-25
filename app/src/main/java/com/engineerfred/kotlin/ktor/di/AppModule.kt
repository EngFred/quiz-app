package com.engineerfred.kotlin.ktor.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.engineerfred.kotlin.ktor.data.local.PreferencesRepoImpl
import com.engineerfred.kotlin.ktor.data.remote.repository.AdminRepoImpl
import com.engineerfred.kotlin.ktor.data.remote.repository.QuestionsRepoImpl
import com.engineerfred.kotlin.ktor.data.remote.repository.StudentsRepoImpl
import com.engineerfred.kotlin.ktor.domain.repository.AdminRepository
import com.engineerfred.kotlin.ktor.domain.repository.PreferencesRepository
import com.engineerfred.kotlin.ktor.domain.repository.QuestionsRepository
import com.engineerfred.kotlin.ktor.domain.repository.StudentsRepository
import com.engineerfred.kotlin.ktor.ui.use_case.ValidateEmailInputUseCase
import com.engineerfred.kotlin.ktor.ui.use_case.ValidateNameInputUseCase
import com.engineerfred.kotlin.ktor.ui.use_case.ValidatePasswordInputUseCase
import com.engineerfred.kotlin.ktor.ui.use_case.ValidatePhoneNumberUseCase
import com.engineerfred.kotlin.ktor.ui.use_case.ValidateRePasswordInputUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestoreInstance() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthInstance() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideStorageInstance() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideAdminRepositoryInstance(
        db: FirebaseFirestore,
        auth: FirebaseAuth
    ) : AdminRepository  = AdminRepoImpl( db, auth )

    @Provides
    @Singleton
    fun provideStudentsRepositoryInstance(
        db: FirebaseFirestore,
        auth: FirebaseAuth,
        storage: FirebaseStorage
    ) : StudentsRepository  = StudentsRepoImpl( db, auth, storage )

    @Provides
    @Singleton
    fun provideQuestionsRepositoryInstance( db: FirebaseFirestore ) : QuestionsRepository  = QuestionsRepoImpl( db )

    @Singleton
    @Provides
    fun provideDatastoreInstance( @ApplicationContext context : Context) = PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile("settings")
    }

    @Provides
    @Singleton
    fun providePreferencesRepositoryInstance( dataStore: DataStore<Preferences> ) : PreferencesRepository  = PreferencesRepoImpl(dataStore)

    @Provides
    @Singleton
    fun provideValidateEmailInputUseCaseInstance() : ValidateEmailInputUseCase  = ValidateEmailInputUseCase()

    @Provides
    @Singleton
    fun provideValidatePasswordInputUseCaseInstance() : ValidatePasswordInputUseCase = ValidatePasswordInputUseCase()

    @Provides
    @Singleton
    fun provideValidateRePasswordInputUseCaseInstance() : ValidateRePasswordInputUseCase  = ValidateRePasswordInputUseCase()

    @Provides
    @Singleton
    fun provideValidateNameInputUseCaseInstance() : ValidateNameInputUseCase  = ValidateNameInputUseCase()

    @Provides
    @Singleton
    fun provideValidatePhoneNumberUseCaseInstance() : ValidatePhoneNumberUseCase  = ValidatePhoneNumberUseCase()

}