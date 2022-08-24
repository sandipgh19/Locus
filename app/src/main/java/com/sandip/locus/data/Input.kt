package com.sandip.locus.data

import com.google.gson.annotations.SerializedName

data class Input(
    @SerializedName(value="type")
    val type: String,
    @SerializedName(value="id")
    val id: String,
    @SerializedName(value="title")
    val title: String,
    @SerializedName(value="value")
    var value: String?,
    @SerializedName(value="provideComment")
    var isComment: Boolean,
    @SerializedName(value="dataMap")
    val dataMap: DataMap
) {

    override fun toString(): String {
        return "id: $id -- type: $type -- user input: $value"
    }
}
