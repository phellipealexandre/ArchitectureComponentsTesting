package com.tutorial.phellipesilva.architecturecomponentstesting.userViewModel

import android.arch.lifecycle.*
import com.tutorial.phellipesilva.architecturecomponentstesting.database.User
import com.tutorial.phellipesilva.architecturecomponentstesting.repository.Repository
import com.tutorial.phellipesilva.architecturecomponentstesting.repository.RepositoryOperationCallback

class UserViewModel(private val repository: Repository) : ViewModel() {

    private val defaultUserId = 1
    private val userLiveData: LiveData<User>
    private var currentUserId = MutableLiveData<String>()

    init {
        userLiveData = Transformations.switchMap(currentUserId) { userId ->
            val id = parseStringIdToIntegerCheckingEmptyState(userId)
            repository.getUser(id)
        }
    }

    fun getUser(): LiveData<User> {
        return userLiveData
    }

    fun replaceAndShowUserFromFetchedFromService(userId: String) {
        val id = parseStringIdToIntegerCheckingEmptyState(userId)
        repository.fetchAndStoreUser(id,
            object : RepositoryOperationCallback {
                override fun onFinished() {
                    showStoredUserById(userId)
                }
            })
    }

    fun showStoredUserById(userId: String) {
        currentUserId.value = userId
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

    private fun parseStringIdToIntegerCheckingEmptyState(userId: String): Int {
        return if (!userId.isEmpty()) Integer.parseInt(userId) else defaultUserId
    }
}
