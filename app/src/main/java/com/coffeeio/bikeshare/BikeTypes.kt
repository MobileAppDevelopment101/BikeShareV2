package com.coffeeio.bikeshare

class BikeTypes {
    val types = HashMap<Int, String>()

    constructor() {
        types.put(1, "Road Bike")
        types.put(2, "Touring Bike")
        types.put(3, "Mountain Bike")
        types.put(4, "Comfort Bike")
        types.put(5, "Commuter Bike")
        types.put(6, "BMX Bike")
        types.put(7, "Cyclocross Bike")
        types.put(8, "Folding Bike")
        types.put(9, "Utility/Cargo Bikes")
        types.put(10, "Electric-Assist Bikes")
    }

    fun get() : HashMap<Int, String> {
        return types;
    }
    fun getList() : ArrayList<String> {
        return ArrayList(types.values)
    }
    fun getTypeId(position: Int) : Int {
        val list = ArrayList(types.keys)
        return list[position]
    }
}