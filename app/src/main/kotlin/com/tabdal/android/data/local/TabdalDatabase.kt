package com.tabdal.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tabdal.android.data.local.dao.ListingDao
import com.tabdal.android.data.local.dao.MessageDao
import com.tabdal.android.data.local.dao.UserDao
import com.tabdal.android.data.local.entities.ConversationEntity
import com.tabdal.android.data.local.entities.ListingEntity
import com.tabdal.android.data.local.entities.MessageEntity
import com.tabdal.android.data.local.entities.UserEntity

@Database(
    entities = [UserEntity::class, ListingEntity::class, MessageEntity::class, ConversationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TabdalDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun listingDao(): ListingDao
    abstract fun messageDao(): MessageDao
}
