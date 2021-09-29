package com.malfaa.firebasechat.room.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversa")
data class ConversaEntidade(
    @ColumnInfo(name = "mensagem")
    val mensagem: String
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mensagem_id")
    var id: Long = 0

    @ColumnInfo(name = "horario")
    var horario: String = ""
}
// FAZER FOREIGNKEY pra receber a conversa certa pra quando for clicado pelos contatos fragment