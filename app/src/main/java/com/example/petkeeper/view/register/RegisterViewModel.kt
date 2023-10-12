package com.example.petkeeper.view.register

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel: ViewModel(){
    private val _uri = MutableLiveData<Uri>()
    val uri: LiveData<Uri>
        get() = _uri

    fun setUri(uri: Uri){
        _uri.value = uri
    }

    private val _bitmap = MutableLiveData<Bitmap>()
}