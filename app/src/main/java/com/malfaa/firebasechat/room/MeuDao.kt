package com.malfaa.firebasechat.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.room.entidades.SignUpEntidade

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

    //Pessoal
    @Query("SELECT * FROM info_pessoal")
    fun myNum(): SignUpEntidade

    @Insert
    suspend fun inserirInfos(infos: SignUpEntidade)

    @Query("DELETE FROM info_pessoal")
    suspend fun apagarInfos()

    @Query("DELETE FROM contatos")
    suspend fun apagarContatos()

}