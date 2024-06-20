package com.example.florify.api.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class HistoryResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("predictions")
	val predictions: List<PredictionsItem?>? = null
) : Parcelable

@Parcelize
data class PredictionsItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("plant")
	val plant: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("prediction")
	val prediction: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: Timestamp? = null
) : Parcelable

@Parcelize
data class Timestamp(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val seconds: Int? = null
) : Parcelable
