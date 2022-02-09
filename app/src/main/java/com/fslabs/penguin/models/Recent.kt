package com.fslabs.penguin.models

import java.io.Serializable

data class Recent(
    val name: String?,
    val number: String?,
    val details: HashMap<String, ArrayList<NumberDetailList>>,
    val type: String,
    val lastModified: String
): Serializable

data class NumberDetailList(
    val number: String?,
    val duration: String?
) : Serializable