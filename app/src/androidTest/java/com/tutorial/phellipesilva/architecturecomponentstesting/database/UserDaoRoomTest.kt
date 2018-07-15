package com.tutorial.phellipesilva.architecturecomponentstesting.database

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoRoomTest {

    private lateinit var userDao: UserDao
    private lateinit var database: Database

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, Database::class.java)
            .build()
        userDao = database.userDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun shouldInsertUserWhenInsertionIsCalledOnDao() {
        val expectedUser = User(1, "name", "username", "test@domain.com", "99999", "website")

        userDao.save(expectedUser)
        val savedUser = userDao.load(1).getValueBlocking()

        assertEquals(expectedUser, savedUser)
    }

    @Test
    fun shouldReplaceUserWhenInsertsUserWithSameId() {
        val previousUser = User(1, "name", "username", "test@domain.com", "99999", "website")
        val expectedUser = User(1, "name two", "username", "test@domain.com", "22222", "website")

        userDao.save(previousUser)
        userDao.save(expectedUser)
        val savedUser = userDao.load(1).getValueBlocking()

        assertEquals(expectedUser, savedUser)
    }

    @Test
    fun shouldStoreBothUsersWhenInsertsUsersWithDifferentIds() {
        val userOne = User(1, "name", "username", "test@domain.com", "99999", "website")
        val userTwo = User(2, "name two", "username", "test@domain.com", "22222", "website")

        userDao.save(userOne)
        userDao.save(userTwo)
        val savedUserOne = userDao.load(1).getValueBlocking()
        val savedUserTwo = userDao.load(2).getValueBlocking()

        assertEquals(userOne, savedUserOne)
        assertEquals(userTwo, savedUserTwo)
    }

    @Test
    fun shouldReturnNullWhenTryingToLoadUserThatNotExists() {
        val storedUser = userDao.load(1).getValueBlocking()

        assertNull(storedUser)
    }

    @Test
    fun shouldReturnNullWhenClearingAllUsersFromDatabase() {
        val userOne = User(1, "name", "username", "test@domain.com", "99999", "website")
        val userTwo = User(2, "name two", "username", "test@domain.com", "22222", "website")

        userDao.save(userOne)
        userDao.save(userTwo)
        userDao.clearAllUsers()
        val savedUserOne = userDao.load(1).getValueBlocking()
        val savedUserTwo = userDao.load(2).getValueBlocking()

        assertNull(savedUserOne)
        assertNull(savedUserTwo)
    }
}