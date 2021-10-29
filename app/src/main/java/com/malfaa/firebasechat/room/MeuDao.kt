package com.malfaa.firebasechat.room

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.room.*
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.room.entidades.ConversaEntidade

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
    suspend fun removerContato(id: ContatosEntidade) // quando deletar o contato, deletar a conversa tbm

    @Query("DELETE FROM conversa WHERE contatosConversaIds = :id")
    suspend fun removeContato(id: Int)

    @Query("SELECT * FROM conversa WHERE contatosConversaIds = :id ORDER BY mensagem_id DESC")
    fun receberConversa(id: Int): LiveData<List<ConversaEntidade>>

//    @Query("SELECT * FROM conversa WHERE souEu = :valor")
//    fun receberConversaDoReceiver(valor: Boolean): LiveData<List<ConversaEntidade>>

    @Insert
    suspend fun inserirMensagem(id: ConversaEntidade)

    @Delete
    suspend fun deleterMensagem(id: ConversaEntidade)

}

// MUDAR OS SUSPEND LIST PARA LIVEDATA E REMOVER SUSPEND