package com.example.moneyfy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moneyfy.data.model.User
import com.example.moneyfy.data.model.UserDao
import com.example.moneyfy.data.model.Spending
import com.example.moneyfy.data.model.SpendingDao

@Database(
    entities = [User::class, Spending::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun spendingDao(): SpendingDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {

                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "moneyfy_db"
                )
                    .fallbackToDestructiveMigration()  // XÓA DB cũ nếu sai version
                    .build()
                    .also { instance = it }
            }
    }
}
