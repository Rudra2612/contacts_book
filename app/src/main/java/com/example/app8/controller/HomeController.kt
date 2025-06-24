package com.example.app8.controller

import android.content.Context
import com.example.app8.model.ContactModel
import com.example.app8.model.DatabaseHelper

class HomeController(context: Context) {

    private val db = DatabaseHelper(context)

    fun insertContact(contactModel: ContactModel) {
        db.insertContact(contactModel)
    }

    fun updateContact(contactModel: ContactModel,id: Int) {
        db.updateContact(contactModel,id)
    }

    fun updateDefault(contactModel: ContactModel,id: Int) {
        db.updateDefault(contactModel,id)
    }

    fun contactSelected(contactModel: ContactModel,id: Int) {
        db.contactSelected(contactModel,id)
    }

    fun deleteContact(id: Int) {
        db.deleteContact(id)
    }

    fun getAllContacts(): ArrayList<ContactModel> {
        return db.getAllContacts()
    }

    fun getContactDetail(id : Int): ContactModel {
        return db.getContactDetail(id)
    }
}