package com.tutorial.phellipesilva.architecturecomponentstesting.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import kotlin.reflect.KClass

@Database(entities = [(User::class)], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): UserDao
}