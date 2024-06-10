package com.example.florify.api.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class LoginResponse(
    val loginResult: LoginResult? = null,
    val error: Boolean? = null,
    val message: String? = null
) : Parcelable