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
    suspend fun removerContato(id: ContatosEntidade)

    @Query("DELETE FROM conversa WHERE uid = :id")
    suspend fun removeContato(id: String)

    @Query("SELECT * FROM conversa WHERE uid = :id ORDER BY mensagem_id DESC")
    fun receberConversa(id: String): LiveData<List<ConversaEntidade>>

    @Insert
    suspend fun inserirMensagem(id: ConversaEntidade)

    @Delete
    suspend fun deleterMensagem(id: ConversaEntidade)

}

// MUDAR OS SUSPEND LIST PARA LIVEDATA E REMOVER SUSPEND