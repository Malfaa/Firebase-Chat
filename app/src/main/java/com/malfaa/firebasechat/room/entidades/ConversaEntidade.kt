package com.malfaa.firebasechat.room.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversa")
data class ConversaEntidade(

    val uid : String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mensagem_id")
    var id: Long = 0

    var mensagem: String = ""

    var horario: String = ""

    var souEu: Boolean = false //retrieve self UID from firebase
}