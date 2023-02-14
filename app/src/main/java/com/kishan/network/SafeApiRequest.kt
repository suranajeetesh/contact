@file:Suppress("BlockingMethodInNonBlockingContext")

package com.kishan.network

import android.content.Context
import com.kishan.network.HttpConstants.SOMETHING_WANT_WRONG
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * Created by Jeetesh Surana.
 */

abstract class SafeApiRequest(val context: Context) {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()
                ?.string()
            val message = StringBuilder()
            var errNo: String? = null
            var code: String? = null
            if (!error.isNullOrBlank()) {
                try {
                    code = JSONObject(error).getString("code")
                    errNo = JSONObject(error).getString("errno")
                    message.append(JSONObject(error).getString("message"))
                } catch (e: JSONException) {
                    e.printStackTrace()
                } finally {
                    try {
                        val serverMessage = JSONObject(error).getString("message")
                        if (message.isEmpty()) {
                            if (!serverMessage.isNullOrEmpty()) {
                                message.append(serverMessage)
                            } else {
                                message.append("\n")
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        message.append("$error")
                    }
                }
            } else {
                if (response.code() == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
                    code = HttpURLConnection.HTTP_CLIENT_TIMEOUT.toString()
                    message.append(SOMETHING_WANT_WRONG)
                    errNo = HttpURLConnection.HTTP_CLIENT_TIMEOUT.toString()
                }
            }
            throw ApiException(
                message.toString(),
                errNo,
                response.code()
            )
        }
    }
}
