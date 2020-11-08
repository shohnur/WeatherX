package uz.myweatherapp.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.myweatherapp.core.model.Data

@Database(entities = [Data::class], version = 3)
abstract class Database : RoomDatabase() {

    abstract fun dao(): Dao


    companion object {
        private var INSTANSE: uz.myweatherapp.core.db.Database? = null

        fun init(context: Context) {
            if (INSTANSE == null)
                INSTANSE = Room.databaseBuilder(
                    context,
                    uz.myweatherapp.core.db.Database::class.java,
                    "data"
                )
                    .allowMainThreadQueries()
                    .build()
        }

        fun getDB(): uz.myweatherapp.core.db.Database = INSTANSE!!
    }

}