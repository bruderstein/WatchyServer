package de.brotherstone.watchyserver

import android.app.Application
import android.app.Notification
import android.util.Log
import androidx.core.app.NotificationCompat

class WatchyServer: Application() {

    companion object WatchyServer {
        const val foo = "hello world"
        // Factory here for the service(s)?
        // Factory here for the Notification?
    }
    override fun onCreate() {
        super.onCreate()
        Log.i("WatchyServer", "WatchyServer app create")
    }

}