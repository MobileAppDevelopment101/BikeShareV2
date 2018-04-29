package com.coffeeio.bikeshare

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*


class Bike : RealmObject {
    @PrimaryKey
    @Required
    private var id = ""
    @Required
    private var name = ""

    private var price = 0
    //private var picture? = null
    //location

    constructor() {
        id =  UUID.randomUUID().toString()
        name = "Something"

    }

    fun getId():String {
        return id
    }


}