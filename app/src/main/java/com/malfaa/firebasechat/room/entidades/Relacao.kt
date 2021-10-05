package com.malfaa.firebasechat.room.entidades

import androidx.room.Embedded
import androidx.room.Relation


data class Relacao(
    @Embedded
    val idContato: ContatosEntidade,

    @Relation(
        parentColumn = "contato",
        entityColumn = "contato_id"
    )
    val contatoId: ConversaEntidade
)
