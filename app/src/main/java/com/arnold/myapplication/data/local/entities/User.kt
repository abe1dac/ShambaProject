package com.arnold.myapplication.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "email", index = true)
    val email: String,

    @ColumnInfo(name = "password_hash")
    val password: String,

    @ColumnInfo(name = "full_name")
    val fullName: String,

    @ColumnInfo(name = "profile_image_uri")
    val profileImageUri: String? = null,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String? = null,

    @ColumnInfo(name = "location")
    val location: String? = null,

    @ColumnInfo(name = "is_verified", defaultValue = "0")
    val isVerified: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    @ColumnInfo(name = "last_updated")
    val lastUpdated: Date = Date(),

    @ColumnInfo(name = "account_type")
    val accountType: AccountType = AccountType.STANDARD
) {
    enum class AccountType {
        STANDARD,
        FARMER,
        AGRONOMIST,
        ADMIN
    }
}