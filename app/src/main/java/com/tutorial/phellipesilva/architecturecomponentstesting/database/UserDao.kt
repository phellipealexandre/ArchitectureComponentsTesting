package com.tutorial.phellipesilva.architecturecomponentstesting.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

@Dao
interface UserDao {

    @Insert(onConflict = REPLACE)
    fun save(user: User)

    @Query("SELECT * FROM user WHERE id = :userId")
    fun load(userId: Int): LiveData<User>

    @Query("DELETE FROM user")
    fun clearAllUsers()
}