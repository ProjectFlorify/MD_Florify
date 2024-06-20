package com.example.florify.api.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class GetForumResponse(

	@field:SerializedName("forumData")
	val forumData: List<ForumDataItem?>? = null
) : Parcelable

@Parcelize
data class CommentsItem(

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userName")
	val userName: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: Timestamp? = null
) : Parcelable

@Parcelize
data class ForumDataItem(

	@field:SerializedName("comments")
	val comments: List<CommentsItem?>? = null,

	@field:SerializedName("imagePrediction")
	val imagePrediction: String? = null,

	@field:SerializedName("numberOfLikes")
	val numberOfLikes: Int? = null,

	@field:SerializedName("resultPrediction")
	val resultPrediction: String? = null,

	@field:SerializedName("caption")
	val caption: String? = null,

	@field:SerializedName("plantPrediction")
	val plantPrediction: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userName")
	val userName: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: Timestamp? = null,
) : Parcelable