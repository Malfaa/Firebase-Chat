package com.malfaa.firebasechat.room.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contatos")
data class ContatosEntidade(
    @PrimaryKey()
    val uid: String
){
    var nome: String = ""
    var email: String = ""
    var number: String = ""
}