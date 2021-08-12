package de.brotherstone.watchyserver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.*


data class PushableNotification(val title: String, val text: String);

class WatchyNotificationListener : NotificationListenerService() {
    companion object {
        const val TAG = "WatchyNotificationListener"
        val PushableNotifications = HashMap<String, PushableNotification>()
    }

    /*
    // Suspect we need to bind to this service in order that we can collect / edit the pushablenotifications,
    // but maybe we can just use the singleton static?
    override fun onBind(intent: Intent?): IBinder? {
        Log.i(TAG, "onBind started")
        return super.onBind(intent)
    }
     */

    override fun onCreate() {
        super.onCreate();
        val notificationChannel = NotificationChannel(
            WatchyNotificationListener::class.java.simpleName,
            resources.getString(R.string.notification_service_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationService =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationService.createNotificationChannel(notificationChannel)

        val notification =
            NotificationCompat.Builder(this, WatchyNotificationListener::class.java.simpleName)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(resources.getString(R.string.notification_service_name))
                .setContentText(resources.getString(R.string.notification_service_running_notification))
                .setAutoCancel(true)

        Log.i(TAG, "Created WatchyNotificationListener")
        startForeground(2, notification.build())
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.i(TAG, "OnListenerConnected")
        for (notification in activeNotifications) {
            Log.i(
                TAG,
                "current active:id ${notification.id} in ${notification.notification.group} ticker ${notification.notification.tickerText}"
            )
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        if (sbn != null) {
            PushableNotifications.remove(getKeyForNotification(sbn))
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn != null) {
            val pushable = PushableNotification(
                sbn.notification.extras.getString(
                    "android.title",
                    ""
                ), sbn.notification.extras.getString("android.text", "")
            )
            // We somehow need to filter out summary ones (e.g. "6 new messages")
            // - think we might be able to copy that logic from GadgetBridge
            PushableNotifications.set(getKeyForNotification(sbn), pushable);

            Log.i(
                TAG,
                "New notification posted, set is now:\n${PushableNotifications.entries.map {
                    entry ->
                    "${entry.key} = ${entry.value.title}: ${entry.value.text}"
                }.joinToString("\n")}"
            )
        }
    }

    fun getKeyForNotification(sbn: StatusBarNotification): String {
        // This isn't the right "key" for these messages, multiple SMS from the same number get the same key
        // Maybe we can use the Notification itself as the key - although I really don't know if we should be referencing that
        return "${sbn.key}#${sbn.id}@${sbn.notification.channelId}"
    }
}