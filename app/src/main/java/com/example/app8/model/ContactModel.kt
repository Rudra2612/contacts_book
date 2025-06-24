package com.example.app8.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class ContactModel(
    var id: Int = 0,
    var image: String = "",
    var firstname: String = "",
    var surname: String = "",
    var company: String = "",
    var phone: String = "",
    var lPhone: String = "",
    var dPhone: String = "",
    var email: String = "",
    var lEmail: String = "",
    var dEmail: String = "",
    var birthDate: String = "",
    var address: String = "",
    var lAddress: String = "",
    var dAddress: String = "",
    var color: String = "",
    var favorite: String = "",
    var selected:  MutableState<String> = mutableStateOf("false")
){
    var label = ""
}