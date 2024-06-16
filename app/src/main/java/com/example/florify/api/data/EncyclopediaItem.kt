package com.example.florify.api.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EncyclopediaItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("handling")
    val handling: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("title")
    val title: String? = null
) : Parcelable

