package com.kishan.core.uI

import androidx.lifecycle.ViewModel
import com.kishan.util.bindingAdapter.mutableLiveData

/**
 * Created by JeeteshSurana.
 */

open class BaseViewModel : ViewModel() {
    var mError = mutableLiveData(com.kishan.data.remote.model.ErrorModel())
}