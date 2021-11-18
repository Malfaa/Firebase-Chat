package com.malfaa.firebasechat.room.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contatos")
data class ContatosEntidade(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contato_id")
    val id: Int

){
    var nome: String = ""
    var email: String = ""
}