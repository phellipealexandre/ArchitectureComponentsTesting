package com.tutorial.phellipesilva.architecturecomponentstesting.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    var id: Int,
    var name: String,
    var username: String,
    var email: String,
    var phone: String,
    var website: String
)
