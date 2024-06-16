package com.example.florify.api.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class RegisterResponse(
    val error: String? = null,
    val message: String? = null,
    val userId: String? = null
) : Parcelable