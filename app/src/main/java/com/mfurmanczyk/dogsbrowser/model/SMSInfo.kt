package com.mfurmanczyk.dogsbrowser.model

data class SMSInfo(
    var to: String,
    val text: String,
    val imageUrl: String?
)
