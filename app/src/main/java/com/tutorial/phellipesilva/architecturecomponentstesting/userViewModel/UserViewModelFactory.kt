package com.tutorial.phellipesilva.architecturecomponentstesting.userViewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tutorial.phellipesilva.architecturecomponentstesting.database.Database
import com.tutorial.phellipesilva.architecturecomponentstesting.remote.RemoteService
import com.tutorial.phellipesilva.architecturecomponentstesting.repository.Repository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.arch.persistence.room.Room
import android.content.Context
import com.tutorial.phellipesilva.architecturecomponentstesting.BuildConfig
import java.util.concurrent.Executors

class UserViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val repository = Repository(
            getRemoteService(),
            buildDatabase().userDao(),
            Executors.newSingleThreadExecutor()
        )

        return UserViewModel(repository) as T
    }

    private fun getRemoteService(): RemoteService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RemoteService::class.java)
    }

    private fun buildDatabase(): Database {
        return Room.databaseBuilder(context, Database::class.java, "Arch Database").build()
    }
}