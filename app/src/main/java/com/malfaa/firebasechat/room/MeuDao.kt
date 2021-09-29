package com.malfaa.firebasechat.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import kotlinx.coroutines.flow.StateFlow

@Dao
interface MeuDao {
    //Contatos
    @Query("SELECT * FROM contatos")
    fun receberContatos(): LiveData<List<ContatosEntidade>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun novoContato(nome: ContatosEntidade)

    @Update
    suspend fun atualizarContato(nome:ContatosEntidade)

    @Delete
    suspend fun removerContato(nome: ContatosEntidade)

    //Conversa
    @Query("SELECT * FROM conversa")
    fun receberConversa(): LiveData<List<ConversaEntidade>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirMensagem(mensagem: ConversaEntidade)

    @Delete
    suspend fun deleterMensagem(mensagem: ConversaEntidade)
}