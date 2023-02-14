package com.kishan.repository

import android.content.Context
import com.kishan.data.remote.model.response.contact.ContactResponse
import com.kishan.network.ApiRestService
import com.kishan.network.SafeApiRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class HomeRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val api: ApiRestService
) : SafeApiRequest(context) {

    suspend fun getContact(): List<ContactResponse> {
        return apiRequest { api.getContact() }
    }
}