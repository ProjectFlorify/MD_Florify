package com.example.florify.api.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResult(
    val accessToken: String? = null,
    val userId: String? = null
) : Parcelable