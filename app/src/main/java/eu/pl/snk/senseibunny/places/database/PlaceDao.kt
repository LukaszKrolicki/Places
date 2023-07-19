package eu.pl.snk.senseibunny.places.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import kotlinx.coroutines.flow.Flow;

@Dao
interface PlaceDao {

    @Insert //we create coorutine becasue it takes time, and should be done on a background
    suspend fun insert(placeEntity: PlaceEntity)

    @Update
    suspend fun update(placeEntity: PlaceEntity)

    @Delete
    suspend fun delete(placeEntity: PlaceEntity)

    @Query("SELECT * FROM `places-table`") //you can retrieve all data too
    fun fetchAllEmployess():Flow<List<PlaceEntity>> //flow is part of coroutines that store data that can be changed on runtime, you dont have to notify that it changed

    @Query("SELECT * FROM `places-table` WHERE id=:id") //returns one row
    fun fetchAllEmployessById(id:Int):Flow<PlaceEntity>
}