package com.example.florify.api.data

import com.google.gson.annotations.SerializedName

data class UserData(

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("email")
    val email: String? = null
)