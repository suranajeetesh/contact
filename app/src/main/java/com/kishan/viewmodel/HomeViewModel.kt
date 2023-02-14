package com.kishan.viewmodel

import android.app.Application
import android.net.Uri
import android.provider.Telephony
import com.kishan.data.remote.model.response.contact.ContactResponse
import com.kishan.data.remote.model.response.sms.Sms
import com.kishan.network.ApiException
import com.kishan.repository.HomeRepository
import com.kishan.util.bindingAdapter.mutableLiveData
import com.kishan.util.extensionFunction.convertIntoErrorObjet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject


/**
 * Created by JeeteshSurana.
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : com.kishan.core.uI.BaseViewModel() {

    var sms = mutableLiveData(ArrayList<Sms>())
    var contactList = mutableLiveData(ArrayList<ContactResponse>())

    suspend fun getData() = withContext(Dispatchers.IO) {
        try {
            contactList.postValue(repository.getContact() as ArrayList)
        } catch (e: ApiException) {
            mError.postValue(convertIntoErrorObjet(e))
        }
    }


    suspend fun getSms(application: Application) = withContext(Dispatchers.IO) {
        try {
            val inboxUri = Uri.parse("content://sms/inbox")
            val cursor = application.contentResolver.query(inboxUri, null, null, null, Telephony.Sms.Inbox.DEFAULT_SORT_ORDER)

            val smsList = ArrayList<Sms>()

            // Retrieve all the SMS messages in the inbox and store them in the smsList
            if (cursor?.moveToFirst() == true) {
                do {
                    var address =
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.Inbox.ADDRESS))
                    val body =
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.Inbox.BODY))
                    val rawDate =
                        cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.Inbox.DATE))

                    val date = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date(rawDate))
                    val time = SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(Date(rawDate))

                    // Format the address as a phone number
                    address = formatPhoneNumber(address)
                    val sms = Sms(
                        address, body, date,time
                    )
                    smsList.add(sms)
                } while (cursor.moveToNext())
            }
            cursor?.close()
            sms.value?.clear()
            sms.postValue(smsList)
        } catch (e: ApiException) {
            mError.postValue(convertIntoErrorObjet(e))
        }
    }

    private fun formatPhoneNumber(phoneNumber: String): String {
        val pattern = Pattern.compile("(\\d{3})(\\d{3})(\\d+)")
        val matcher = pattern.matcher(phoneNumber)
        return if (matcher.matches()) {
            "(${matcher.group(1)}) ${matcher.group(2)}-${matcher.group(3)}"
        } else {
            phoneNumber
        }
    }

}