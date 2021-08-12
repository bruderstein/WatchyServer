package de.brotherstone.watchyserver

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server)
        Log.i("WatchyMainActivity", "main activity on create")
        val bleServiceIntent = Intent(this@MainActivity, BleService::class.java)
        startService(bleServiceIntent)
        val notificationListenerIntent = Intent(this@MainActivity, WatchyNotificationListener::class.java)
        startService(notificationListenerIntent)
    }
}