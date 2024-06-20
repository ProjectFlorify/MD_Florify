package com.example.florify.api.data

import com.google.gson.annotations.SerializedName

data class GetUserResponse(

	@field:SerializedName("userData")
	val userData: UserData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)