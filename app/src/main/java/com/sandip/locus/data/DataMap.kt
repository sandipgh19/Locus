package com.sandip.locus.data

import com.google.gson.annotations.SerializedName

data class DataMap(
    @SerializedName(value="options")
    var options: List<String>?
)
