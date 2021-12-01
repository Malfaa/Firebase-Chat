package com.malfaa.firebasechat.room.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.malfaa.firebasechat.fragment.ContatosFragment

@Entity(tableName = "conversa")
data class ConversaEntidade(
    @PrimaryKey
    val uid : String
) {
    var idMensagem: String = "" //!

    var mensagem: String = ""

    var horario: String = ""

    var myUid: String = ContatosFragment.myUid.toString()
}