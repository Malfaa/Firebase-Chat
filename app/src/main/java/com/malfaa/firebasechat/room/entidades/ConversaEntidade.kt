package com.malfaa.firebasechat.room.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.malfaa.firebasechat.fragment.ContatosFragment.Companion.selfUid

@Entity(tableName = "conversa")
data class ConversaEntidade(
    @PrimaryKey
    val uid : String
) {
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "mensagem_id")
//    var id: Long = 0

    var mensagem: String = ""

    var horario: String = ""

    var myUid: String = selfUid.toString()
    //var souEu: String = "" // //retrieve self UID from firebase
}