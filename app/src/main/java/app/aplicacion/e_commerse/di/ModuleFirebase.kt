package app.aplicacion.e_commerse.di

import app.aplicacion.e_commerse.data.repository.UserRepository
import app.aplicacion.e_commerse.data.repository.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn (SingletonComponent::class)
object ModuleFirebase {

    @Provides
    @Singleton

    fun providesInstace()=FirebaseAuth.getInstance()



    @Provides
    @Singleton
    fun providesInstaceRealtime()=FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun providesRealtime(data:FirebaseDatabase)=data.getReference("userEcomerce")


    @Provides
    @Singleton

    fun providesRepository(auth:FirebaseAuth):UserRepository =UserRepositoryImpl(auth)


}