package app.aplicacion.e_commerse.di

import app.aplicacion.e_commerse.data.repositoryStore.DataRepository
import app.aplicacion.e_commerse.data.repositoryStore.DataRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesDataRepository(data:DatabaseReference):DataRepository=DataRepositoryImpl(data)
}