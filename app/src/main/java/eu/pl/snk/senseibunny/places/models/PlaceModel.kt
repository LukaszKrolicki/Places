package eu.pl.snk.senseibunny.places.models

data class PlaceModel(val id: Int, val title: String, val image: String, val description: String,
                      val date: String, val location: String, val latitide: Double, val longitude: Double)