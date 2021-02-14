package com.wordpress.mariaanko.bezpecnehovory

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import io.karn.notify.Notify

class PhoneStateReceiver:BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
        val receivedPhoneNumber = intent?.getStringExtra((TelephonyManager.EXTRA_INCOMING_NUMBER))


        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING) && receivedPhoneNumber != null) {

            //check for number in Contacts

            //create a Notification if the number does not exist in Contacts
            if (context != null) {
                Notify
                    .with(context)
                    .meta { // this: Payload.Meta
                        // Launch the MainActivity once the notification is clicked.
                        clickIntent = PendingIntent.getActivity(context,
                            0,
                            Intent(context, MainActivity::class.java)
                                .putExtra("number", receivedPhoneNumber)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                            FLAG_UPDATE_CURRENT)
                    }
                    .content { // this: Payload.Content.Default
                        title = "Bezpecne Hovory"
                        text = "Informacie o telefonnom cisle: " + receivedPhoneNumber
                    }
                    .show()
            }
        }
    }

}