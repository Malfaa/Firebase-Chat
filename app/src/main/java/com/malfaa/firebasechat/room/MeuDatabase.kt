package com.malfaa.firebasechat.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.room.entidades.ConversaEntidade

@Database(entities = [ContatosEntidade::class, ConversaEntidade::class], version = 1, exportSchema = false)
abstract class MeuDatabase: RoomDatabase() {
    abstract fun meuDao(): MeuDao

    private class DatabaseCallback : RoomDatabase.Callback()

    companion object{
        @Volatile
        private var INSTANCE: MeuDatabase? = null

        fun recebaDatabase(context: Context): MeuDatabase {
            return (INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MeuDatabase::class.java,
                    "database"
                ).addCallback(DatabaseCallback()).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            })
        }
    }
}