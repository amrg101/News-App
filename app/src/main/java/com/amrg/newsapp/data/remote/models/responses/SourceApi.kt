package com.amrg.newsapp.data.remote.models.responses

import com.google.gson.annotations.SerializedName

data class SourceApi(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?
)
