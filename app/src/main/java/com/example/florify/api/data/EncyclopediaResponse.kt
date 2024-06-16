package com.example.florify.api.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class EncyclopediaResponse(

	@field:SerializedName("encyclopedia")
	val encyclopedia: List<EncyclopediaItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
) : Parcelable