package com.tutorial.phellipesilva.architecturecomponentstesting.userViewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.tutorial.phellipesilva.architecturecomponentstesting.database.User
import com.tutorial.phellipesilva.architecturecomponentstesting.repository.Repository
import com.tutorial.phellipesilva.architecturecomponentstesting.repository.RepositoryOperationCallback

class UserViewModel(private val repository: Repository) : ViewModel() {

    private val userLiveData = MediatorLiveData<User>()
    private var currentUser = repository.getUser(1)

    init {
        userLiveData.addSource(currentUser) { this.userLiveData.value = it }
    }

    fun getUser(): LiveData<User> {
        return this.userLiveData
    }

    fun replaceAndShowUserFromFetchedFromService(userId: String) {
        repository.fetchAndStoreUser(Integer.parseInt(userId), object : RepositoryOperationCallback {
            override fun onFinished() {
                userLiveData.removeSource(currentUser)
                currentUser = repository.getUser(Integer.parseInt(userId))
                userLiveData.addSource(currentUser) { userLiveData.value = it }
            }
        })
    }

    fun showStoredUserById(userId: String) {
        userLiveData.removeSource(currentUser)
        currentUser = repository.getUser(Integer.parseInt(userId))
        userLiveData.addSource(currentUser) { userLiveData.value = it }
    }

    fun updateCurrentUserPhone(phone: String) {
        userLiveData.value?.let {
            it.phone = phone
            repository.updateUser(it)
        }
    }

    fun removeAllUsers() {
        repository.clearAllUsers()
    }
}
