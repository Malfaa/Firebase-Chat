package com.malfaa.firebasechat.room.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.malfaa.firebasechat.fragment.ContatosFragment.Companion.selfUid

@Entity(tableName = "conversa")
data class ConversaEntidade(

    val uid : String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mensagem_id")
    var id: Long = 0 // FIXME: 22/11/2021 problema aqui é que no room ele autogerenerate, no firebase não acontece isso aparentemente

    var mensagem: String = ""

    var horario: String = ""

    var souEu: String = "" //selfUid.toString() //retrieve self UID from firebase
}