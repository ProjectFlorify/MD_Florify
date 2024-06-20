package com.example.florify.api.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PostCommentResponse(

	@field:SerializedName("commentData")
	val commentData: CommentsItem? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
) : Parcelable

