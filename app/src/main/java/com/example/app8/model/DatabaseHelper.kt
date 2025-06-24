package com.example.app8.model

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.runtime.mutableStateOf

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "Contacts_app", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "create table contact(id integer primary key autoincrement,image Text,firstname Text,surname Text,company Text,phone number,lPhone Text,dPhone Text,email Text,lEmail Text,dEmail Text,birthdate Text,address Text,lAddress Text,dAddress Text,color Text,favorite Text,selected Text)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { }


    fun insertContact(contactModel: ContactModel) {
        val insert =
            "insert into contact(image,firstname,surname,company,phone,lPhone,dPhone,email,lEmail,dEmail,birthdate,address,lAddress,dAddress,color,favorite,selected) values('${contactModel.image}','${contactModel.firstname}','${contactModel.surname}','${contactModel.company}','${contactModel.phone}','${contactModel.lPhone}','${contactModel.dPhone}','${contactModel.email}','${contactModel.lEmail}','${contactModel.dEmail}','${contactModel.birthDate}','${contactModel.address}','${contactModel.lAddress}','${contactModel.dAddress}','${contactModel.color}','${contactModel.favorite}','${contactModel.selected}')"
        writableDatabase.execSQL(insert)
    }

    fun updateContact(contactModel: ContactModel, id: Int) {
        val update =
            "update contact set image='${contactModel.image}',firstname='${contactModel.firstname}',surname='${contactModel.surname}',company='${contactModel.company}',phone='${contactModel.phone}',lPhone='${contactModel.lPhone}',dPhone='${contactModel.dPhone}',email='${contactModel.email}',lEmail='${contactModel.lEmail}',dEmail='${contactModel.dEmail}',birthDate='${contactModel.birthDate}',address='${contactModel.address}',lAddress='${contactModel.lAddress}',dAddress='${contactModel.dAddress}' where id=${id}"
        writableDatabase.execSQL(update)
    }

    fun updateDefault(contactModel: ContactModel,id: Int) {
        val update =
            "update contact set dPhone='${contactModel.dPhone}',dEmail='${contactModel.dEmail}',dAddress='${contactModel.dAddress}',favorite='${contactModel.favorite}' where id=${id}"
        writableDatabase.execSQL(update)
    }

    fun contactSelected(contactModel: ContactModel, id: Int) {
        val update = "update contact set selected='${contactModel.selected}' where id=${id}"
        writableDatabase.execSQL(update)
    }

    fun deleteContact(id: Int) {
        val delete = "delete from contact where id=${id}"
        readableDatabase.execSQL(delete)
    }

    @SuppressLint("Range")
    fun getAllContacts(): ArrayList<ContactModel> {
        val select = "select * from contact"
        val cursor = readableDatabase.rawQuery(select, null)
        val user = ArrayList<ContactModel>()

        while (cursor.moveToNext()) {
            val contact = ContactModel(
                id = cursor.getInt(cursor.getColumnIndex("id")),
                image = cursor.getString(cursor.getColumnIndex("image")),
                firstname = cursor.getString(cursor.getColumnIndex("firstname")),
                surname = cursor.getString(cursor.getColumnIndex("surname")),
                color = cursor.getString(cursor.getColumnIndex("color")),
                phone = cursor.getString(cursor.getColumnIndex("phone")),
                dPhone = cursor.getString(cursor.getColumnIndex("dPhone")),
                favorite = cursor.getString(cursor.getColumnIndex("favorite")),
                selected = mutableStateOf(cursor.getString(cursor.getColumnIndex("selected")))
            )
            user.add(contact)
        }
        cursor.close()
        return user
    }

    @SuppressLint("Range")
    fun getContactDetail(id: Int): ContactModel {
        val select =
            "select * from contact where id=${id}"
        val cursor = readableDatabase.rawQuery(select, null)
        var detail: ContactModel? = null

        while (cursor.moveToNext()) {
            detail = ContactModel(
                id = cursor.getInt(cursor.getColumnIndex("id")),
                image = cursor.getString(cursor.getColumnIndex("image")),
                firstname = cursor.getString(cursor.getColumnIndex("firstname")),
                surname = cursor.getString(cursor.getColumnIndex("surname")),
                company = cursor.getString(cursor.getColumnIndex("company")),
                phone = cursor.getString(cursor.getColumnIndex("phone")),
                lPhone = cursor.getString(cursor.getColumnIndex("lPhone")),
                dPhone = cursor.getString(cursor.getColumnIndex("dPhone")),
                email = cursor.getString(cursor.getColumnIndex("email")),
                lEmail = cursor.getString(cursor.getColumnIndex("lEmail")),
                dEmail = cursor.getString(cursor.getColumnIndex("dEmail")),
                birthDate = cursor.getString(cursor.getColumnIndex("birthdate")),
                address = cursor.getString(cursor.getColumnIndex("address")),
                lAddress = cursor.getString(cursor.getColumnIndex("lAddress")),
                dAddress = cursor.getString(cursor.getColumnIndex("dAddress")),
                color = cursor.getString(cursor.getColumnIndex("color")),
                favorite = cursor.getString(cursor.getColumnIndex("favorite"))
            )
        }
        cursor.close()
        return detail!!
    }

}