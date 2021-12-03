package com.malfaa.firebasechat.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import com.malfaa.firebasechat.room.entidades.SignInEntidade

@Dao
interface MeuDao {
    //Contatos
    @Query("SELECT * FROM contatos")
    fun retornarContatos(): LiveData<List<ContatosEntidade>>

    @Query("SELECT * FROM contatos WHERE uid = :uid")
    suspend fun retornaNumero(uid: String): ContatosEntidade

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun novoContato(nome: ContatosEntidade)

    @Update
    suspend fun atualizarContato(nome:ContatosEntidade)

    @Delete
    suspend fun removerContato(id: ContatosEntidade)

    @Query("DELETE FROM conversa WHERE uid = :id")
    suspend fun removeContato(id: String)

    @Query("SELECT * FROM conversa WHERE conversa_id = :id ORDER BY horario ASC")//mensagem_id
    fun receberConversa(id: String): LiveData<List<ConversaEntidade>>

    @Insert
    suspend fun inserirMensagem(id: ConversaEntidade)

    @Delete
    suspend fun deleterMensagem(id: ConversaEntidade)

    //Pessoal
    @Query("SELECT * FROM info_pessoal WHERE uid = :uid")
    suspend fun myNum(uid:String): SignInEntidade

    @Insert
    suspend fun inserirInfos(infos: SignInEntidade)

}