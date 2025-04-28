package com.arnold.myapplication.data.local

import androidx.room.TypeConverter
import com.arnold.myapplication.data.local.entities.User
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

    @TypeConverter
    fun fromAccountType(value: String): User.AccountType =
        User.AccountType.valueOf(value)

    @TypeConverter
    fun accountTypeToString(type: User.AccountType): String =
        type.name
}