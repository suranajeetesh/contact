package com.kishan.network.interceptor

import com.example.newbasicstructure.BuildConfig
import com.kishan.util.DeviceUtil
import com.kishan.util.PreferenceProvider
import okhttp3.*
import okio.Buffer
import okio.BufferedSource
import java.net.HttpURLConnection.HTTP_CLIENT_TIMEOUT

/**
 * Created by Jeetesh Surana.
 */
class HeaderInterceptor(
    private val deviceUtil: DeviceUtil,
    private val preferenceProvider: PreferenceProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        return try {
            chain.run {
                var userAgent = ""
                val authToken = ""
                val deviceName = deviceUtil.getDeviceName()
                deviceUtil.getUserAgent()?.let { userAgent = it }

                val builder: Request.Builder = request().newBuilder()
                   /* .addHeader("Application-Name", DEVICE_TYPE)
                    .addHeader("x-device-type", DEVICE_TYPE)
                    .addHeader("x-device-id", deviceUtil.getDeviceId())
                    .addHeader("x-access-token", authToken)
                    .addHeader("x-device-name", deviceName)
                    .addHeader("user-agent", userAgent)*/

                builder.addHeader("Application-Version", BuildConfig.VERSION_CODE.toString())
                proceed(builder.build())
            }
        } catch (e: Exception) {
            Response.Builder()
                .code(HTTP_CLIENT_TIMEOUT)
                .request(chain.request())
                .body(object : ResponseBody() {
                    override fun contentLength() = 0L
                    override fun contentType(): MediaType? = null
                    override fun source(): BufferedSource = Buffer()
                })
                .message(e.message ?: e.toString())
                .protocol(Protocol.HTTP_1_1)
                .build()
        }
    }
}
