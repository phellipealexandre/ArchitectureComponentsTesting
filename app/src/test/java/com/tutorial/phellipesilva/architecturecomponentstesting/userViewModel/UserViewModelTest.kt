package com.tutorial.phellipesilva.architecturecomponentstesting.userViewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.tutorial.phellipesilva.architecturecomponentstesting.database.User
import com.tutorial.phellipesilva.architecturecomponentstesting.repository.Repository
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var userViewModel: UserViewModel

    @Mock
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        userViewModel = UserViewModel(repository)
        userViewModel.getUser().observeForever { }
    }

    @Test
    fun shouldReturnUserWithIdOneWhenSearchingForAnUserWithEmptyId() {
        userViewModel.showStoredUserById("")

        verify(repository).getUser(1)
    }

    @Test
    fun shouldReturnUserWithParameterIdWhenSearchingForAnUserWithSpecificId() {
        userViewModel.showStoredUserById("1")

        verify(repository).getUser(1)
    }

    @Test
    fun shouldReturnUserWithParameterIdWhenSearchingForAnUserWithOtherSpecificId() {
        userViewModel.showStoredUserById("2")

        verify(repository).getUser(2)
    }

    @Test
    fun shouldUpdateUserLiveDataWhenShowingAnUserWithSpecificId() {
        val expectedLiveData = MutableLiveData<User>()
        expectedLiveData.value = User(2, "name", "username", "test@domain.com", "99999", "website")
        whenever(repository.getUser(2)).thenReturn(expectedLiveData)

        userViewModel.showStoredUserById("2")
        val returnedLiveData = userViewModel.getUser()

        assertEquals(expectedLiveData.value, returnedLiveData.value)
    }

    @Test
    fun shouldReplaceAndShowUserWithIdOneWhenPassingEmptyStringToReplaceOperation() {
        userViewModel.replaceAndShowUserFromFetchedFromService("")

        verify(repository).fetchAndStoreUser(eq(1), any())
    }

    @Test
    fun shouldReplaceAndShowUserWithParameterIdWhenPassingSpecificIdToReplaceOperation() {
        userViewModel.replaceAndShowUserFromFetchedFromService("1")

        verify(repository).fetchAndStoreUser(eq(1), any())
    }

    @Test
    fun shouldReplaceAndShowUserWithParameterIdWhenPassingOtherSpecificIdToReplaceOperation() {
        userViewModel.replaceAndShowUserFromFetchedFromService("2")

        verify(repository).fetchAndStoreUser(eq(2), any())
    }

    @Test
    fun shouldCallClearInRepositoryWhenClearingAllUsers() {
        userViewModel.removeAllUsers()

        verify(repository).clearAllUsers()
    }

    @Test
    fun shouldNotUpdatePhoneWhenUserLiveDataHasNoValue() {
        userViewModel.updateCurrentUserPhone("99999")

        verify(repository, never()).updateUser(any())
    }

    @Test
    fun shouldUpdatePhoneWhenUserLiveDataHasValue() {
        val liveData = MutableLiveData<User>()
        liveData.value = User(1, "name", "username", "test@domain.com", "99999", "website")
        whenever(repository.getUser(1)).thenReturn(liveData)
        userViewModel.showStoredUserById("1")

        userViewModel.updateCurrentUserPhone("11111")

        verify(repository).updateUser(
            User(
                1,
                "name",
                "username",
                "test@domain.com",
                "11111",
                "website"
            )
        )
    }
}