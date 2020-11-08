package uz.myweatherapp.core.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.myweatherapp.core.model.current.CurrentWeather
import uz.myweatherapp.core.model.daily.DailyData
import java.io.Serializable

@Entity(tableName = "data")
data class Data(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "current")
    val current: String?,
    @ColumnInfo(name = "daily")
    val daily: String?,
    @ColumnInfo(name = "lastUpdate")
    val lastUpdate:String?
)