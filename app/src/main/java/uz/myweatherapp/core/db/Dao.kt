package uz.myweatherapp.core.db

import androidx.room.*
import androidx.room.Dao
import uz.myweatherapp.core.model.Data

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: Data)

    @Query("delete from data")
    fun clear()

    @Query("select * from data")
    fun getData():List<Data>?
}