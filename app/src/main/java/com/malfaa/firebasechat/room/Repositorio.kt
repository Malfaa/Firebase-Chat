package com.malfaa.firebasechat.room

import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import kotlinx.coroutines.flow.StateFlow

class Repositorio(val meuDao: MeuDao): MeuDao {
    override fun receberContatos(): List<ContatosEntidade> {
        TODO("Not yet implemented")
    }

    override suspend fun novoContato(nome: ContatosEntidade) {
        meuDao.novoContato(nome)
    }

    override suspend fun atualizarContato(nome: ContatosEntidade) {
        TODO("Not yet implemented")
    }

    override suspend fun removerContato(nome: ContatosEntidade) {
        TODO("Not yet implemented")
    }

    override fun receberConversa(): List<ConversaEntidade> {
        TODO("Not yet implemented")
    }

    override suspend fun inserirMensagem(mensagem: ConversaEntidade) {
        TODO("Not yet implemented")
    }

    override suspend fun deleterMensagem(mensagem: ConversaEntidade) {
        TODO("Not yet implemented")
    }
}