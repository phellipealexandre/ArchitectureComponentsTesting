package com.tutorial.phellipesilva.architecturecomponentstesting.repository

import android.arch.lifecycle.LiveData
import android.util.Log
import com.tutorial.phellipesilva.architecturecomponentstesting.database.User
import com.tutorial.phellipesilva.architecturecomponentstesting.database.UserDao
import com.tutorial.phellipesilva.architecturecomponentstesting.remote.RemoteService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class Repository(
    private val remoteService: RemoteService,
    private val userDao: UserDao,
    private val executor: Executor
) {

    fun getUser(userId: Int): LiveData<User> = userDao.load(userId)

    fun fetchAndStoreUser(userId: Int, callback: RepositoryOperationCallback) {
        refreshUser(userId, callback)
    }

    fun updateUser(user: User) {
        executor.execute { userDao.save(user) }
    }

    fun clearAllUsers() {
        executor.execute { userDao.clearAllUsers() }
    }

    private fun refreshUser(userId: Int, callback: RepositoryOperationCallback) {
        remoteService.getUserById(userId).enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.e("ArchComponents", t?.message)
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                response?.body()?.apply {
                    executor.execute { userDao.save(this) }
                    callback.onFinished()
                }
            }
        })
    }
}