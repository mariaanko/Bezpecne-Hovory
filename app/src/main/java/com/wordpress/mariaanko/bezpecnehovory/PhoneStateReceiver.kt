package com.wordpress.mariaanko.bezpecnehovory

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import androidx.core.text.isDigitsOnly
import io.karn.notify.Notify

class PhoneStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
        val receivedPhoneNumber = intent?.getStringExtra((TelephonyManager.EXTRA_INCOMING_NUMBER))
        var isInContacts = false


        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING) && receivedPhoneNumber != null) {

            //check for number in Contacts
            val allContacts: HashMap<String, ArrayList<String>> = getContactNumbers(context)
            for (contact in allContacts) {
                for (number in contact.value) {
                    val contactNumber = number.replace("[^0-9]".toRegex(), "")
                    if(receivedPhoneNumber.contains(contactNumber)){
                        isInContacts = true
                    }
                }
            }

            //create a Notification if the number does not exist in Contacts
            if (context != null && !isInContacts) {
                Notify
                    .with(context)
                    .meta { // this: Payload.Meta
                        // Launch the MainActivity once the notification is clicked.
                        clickIntent = PendingIntent.getActivity(
                            context,
                            0,
                            Intent(context, MainActivity::class.java)
                                .putExtra("number", receivedPhoneNumber)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                            FLAG_UPDATE_CURRENT
                        )
                    }
                    .content { // this: Payload.Content.Default
                        title = "Bezpecne Hovory"
                        text = "Informacie o telefonnom cisle: " + receivedPhoneNumber
                    }
                    .show()
            }
        }
    }

    private fun getContactNumbers(context: Context?): HashMap<String, ArrayList<String>> {

        val contactsNumberMap = HashMap<String, ArrayList<String>>()
        val phoneCursor: Cursor? = context?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (phoneCursor != null && phoneCursor.count > 0) {
            val contactIdIndex =
                phoneCursor!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val numberIndex =
                phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (phoneCursor.moveToNext()) {
                val contactId = phoneCursor.getString(contactIdIndex)
                val number: String = phoneCursor.getString(numberIndex)
                //check if the map contains key or not, if not then create a new array list with number
                if (contactsNumberMap.containsKey(contactId)) {
                    contactsNumberMap[contactId]?.add(number)
                } else {
                    contactsNumberMap[contactId] = arrayListOf(number)
                }
            }
            //contact contains all the number of a particular contact
            phoneCursor.close()
        }
        return contactsNumberMap
    }
}
