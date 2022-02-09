package com.fslabs.penguin.models

data class Contact(val id: String, val name:String) {
    var numbers = ArrayList<String>()
}