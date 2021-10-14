package com.malfaa.firebasechat.room.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contatos")
data class ContatosEntidade(
    var nome: String
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contato_id")
    var id: Long = 0

    //como salvar o contato assim vindo pelo google funcionaria?

}
