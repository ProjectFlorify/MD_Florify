package com.example.florify.api.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PredictResponse(

    @field:SerializedName("predictionData")
    val predictionData: PredictionData? = null,

    @field:SerializedName("error")
    val error: Boolean? = null
) : Parcelable

@Parcelize
data class PredictionData(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("plant")
    val plant: String? = null,

    @field:SerializedName("imageUrl")
    val imageUrl: String? = null,

    @field:SerializedName("prediction")
    val prediction: String? = null,

    @field:SerializedName("timestamp")
    val timestamp: String? = null
) : Parcelable
