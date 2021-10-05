package com.malfaa.firebasechat.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import com.malfaa.firebasechat.room.entidades.Relacao

@Dao
interface MeuDao {
    //Contatos
    @Query("SELECT * FROM contatos")
    fun retornarContatos(): LiveData<List<ContatosEntidade>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun novoContato(nome: ContatosEntidade)

    @Update
    suspend fun atualizarContato(nome:ContatosEntidade)

    @Delete
    suspend fun removerContato(nome: ContatosEntidade) // quando deletar o contato, deletar a conversa tbm

    //Conversa
    @Query("SELECT * FROM conversa")
    fun receberConversa(): LiveData<List<ConversaEntidade>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirMensagem(mensagem: ConversaEntidade)

    @Delete
    suspend fun deleterMensagem(mensagem: ConversaEntidade)


    //-------------------------------------------------------------------------------
    @Transaction
    @Query("SELECT * FROM conversa, contatos WHERE contato_id = contato")
    fun getAllConversaFromContato(): LiveData<List<Relacao>> //alterar aqui


}//    @Query("SELECT contato_id, mensagem_id,mensagem, horario, contato_nome FROM conversa, contatos WHERE contato_id = contato")
