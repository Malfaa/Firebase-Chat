package com.malfaa.firebasechat.room.entidades

import androidx.room.*

@Entity(tableName = "conversa")
data class ConversaEntidade(
    val contatosConversaIds : Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mensagem_id")
    var id: Long = 0

    var mensagem: String = ""

    var horario: String = ""

    var souEu: Boolean = false
}