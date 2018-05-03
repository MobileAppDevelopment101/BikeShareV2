package com.coffeeio.bikeshare

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

class Database {
    fun setup(context: Context) {
        Realm.init(context)
        val realmConfig = RealmConfiguration.Builder()
                .name("tasky.realm4")
                .schemaVersion(0)
                .build()
        Realm.setDefaultConfiguration(realmConfig)

    }
    fun getRealm(context: Context) : Realm {
        setup(context)
        return Realm.getDefaultInstance()
    }
}