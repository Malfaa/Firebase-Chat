package com.malfaa.firebasechat.room.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversa")
data class ConversaEntidade(
    @PrimaryKey
    @ColumnInfo(name = "id_mensagem")
    val idMensagem: String
) {
    var uid: String = ""
    var mensagem: String = ""
    var horario: String = ""

    @ColumnInfo(name = "meu_uid")
    var meuUid: String = ""

    constructor(): this("")
}

