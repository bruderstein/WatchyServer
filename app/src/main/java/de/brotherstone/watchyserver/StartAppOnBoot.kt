package de.brotherstone.watchyserver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class StartAppOnBoot : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (Intent.ACTION_BOOT_COMPLETED == intent!!.action) {
            Log.i("StartAppOnBoot", "received boot completed")
            val i = Intent(context, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context!!.startActivity(i)
        }
    }
}