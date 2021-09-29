package com.malfaa.firebasechat.room.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contatos")
data class ContatosEntidade(
    @ColumnInfo(name = "contato_nome")
    var nome: String
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contato")
    var id: Long = 0
}
