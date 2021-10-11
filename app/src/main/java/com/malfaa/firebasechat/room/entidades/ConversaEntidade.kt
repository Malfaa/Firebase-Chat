package com.malfaa.firebasechat.room.entidades

import androidx.room.*

@Entity(tableName = "conversa")
data class ConversaEntidade(
    @Embedded
    val contatosConversaIds : ContatosEntidade

){
    @ColumnInfo(name = "mensagem")
    var mensagem: String = ""

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mensagem_id")
    var id: Long = 1

    @ColumnInfo(name = "horario")
    var horario: String = ""

}
// FAZER FOREIGNKEY pra receber a conversa certa pra quando for clicado pelos contatos fragment
// ou usar outra entidade como intermediador entre o contato e a conversa
//https://stackoverflow.com/questions/63382855/how-to-add-embedded-and-relation-to-room-database