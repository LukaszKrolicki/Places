package eu.pl.snk.senseibunny.places.database

import android.app.Application

class PlaceApp: Application() {

    val db by lazy {
        PlacesDatabase.getInstance(this)
    }
}