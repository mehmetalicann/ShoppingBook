package com.mehmetalican.shoppingbook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Item(
    @ColumnInfo("name")
    var itemName: String,

    @ColumnInfo("storename")
    var storedName: String?,

    @ColumnInfo("price")
    var price: String?,

    @ColumnInfo("image")
    var image: ByteArray?
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}