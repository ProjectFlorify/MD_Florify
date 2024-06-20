package com.example.florify.api.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PostForumResponse(

	@field:SerializedName("post_data")
	val postData: PostData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
) : Parcelable

@Parcelize
data class PostData(

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("caption")
	val caption: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("result_prediction")
	val resultPrediction: String? = null,

	@field:SerializedName("image_prediction")
	val imagePrediction: String? = null,

	@field:SerializedName("plant_prediction")
	val plantPrediction: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
) : Parcelable
