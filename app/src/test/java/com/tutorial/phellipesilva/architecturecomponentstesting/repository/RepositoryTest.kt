package com.tutorial.phellipesilva.architecturecomponentstesting.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.tutorial.phellipesilva.architecturecomponentstesting.database.User
import com.tutorial.phellipesilva.architecturecomponentstesting.database.UserDao
import com.tutorial.phellipesilva.architecturecomponentstesting.remote.RemoteService
import junit.framework.Assert.assertEquals
import okhttp3.Request
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: Repository

    @Mock
    private lateinit var remoteService: RemoteService

    @Mock
    private lateinit var userDao: UserDao

    @Mock
    private lateinit var callback: RepositoryOperationCallback

    @Before
    fun setUp() {
        repository = Repository(remoteService, userDao, Executor { it.run() })
    }

    @Test
    fun shouldLoadUserFromDaoWhenRequestedFromRepository() {
        val expectedLiveData = MutableLiveData<User>()
        `when`(userDao.load(1)).thenReturn(expectedLiveData)

        val userLiveData = repository.getUser(1)

        assertEquals(expectedLiveData, userLiveData)
    }

    @Test
    fun shouldSaveUserInDaoWhenRemoteRequestIsSuccessful() {
        val expectedUser = User(1, "name", "username", "test@domain.com", "99999", "website")
        `when`(remoteService.getUserById(1)).thenReturn(object : Call<User> {

            override fun enqueue(callback: Callback<User>?) {
                callback?.onResponse(null, Response.success(expectedUser))
            }

            override fun isExecuted(): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun clone(): Call<User> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun isCanceled(): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun cancel() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun execute(): Response<User> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun request(): Request {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

        repository.fetchAndStoreUser(1, callback)

        verify(userDao).save(expectedUser)
        verify(callback).onFinished()
    }

    @Test
    fun shouldSaveUserInDaoWhenUpdateIsRequestedFromRepository() {
        val user = User(1, "name", "username", "test@domain.com", "99999", "website")

        repository.updateUser(user)

        verify(userDao).save(user)
    }

    @Test
    fun shouldClearAllUsersInDaoWhenClearIsRequestedFromRepository() {
        repository.clearAllUsers()

        verify(userDao).clearAllUsers()
    }
}