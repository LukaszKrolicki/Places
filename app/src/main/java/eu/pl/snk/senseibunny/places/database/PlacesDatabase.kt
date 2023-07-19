package eu.pl.snk.senseibunny.places.database

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PlaceEntity::class], version=1) // if we change fe. properties we must change the version
abstract class PlacesDatabase:RoomDatabase() {
    abstract fun placesDao():PlaceDao

    companion object{
        @Volatile
        private  var INSTANCE: PlacesDatabase?=null

        fun getInstance(context: Context):PlacesDatabase{
            synchronized(this){
                var instance= INSTANCE

                if(instance==null){ //create instance to database only if it wasnt created already
                    instance= Room.databaseBuilder(
                        context.applicationContext,
                        PlacesDatabase::class.java,
                        "person_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE=instance
                }
                return instance
            }
        }
    }
}