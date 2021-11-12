package com.malfaa.firebasechat.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MeuDatabaseTest : TestCase() {

    private lateinit var database: MeuDatabase
    private lateinit var dao: MeuDao
    private lateinit var vm: AdicionaContatoViewModel

    //--------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------
    //              ALTERAR O MEUDAO P/ LIST COMUM, SEM SER LIVEDATA = TESTE NÃO BUGA
    //--------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MeuDatabase::class.java).build()
        dao = database.meuDao()

        //vm = AdicionaContatoViewModel(dao)
    }

    @After
    public override fun tearDown() {
        database.close()
    }

    @Test
    fun contatos() = runBlocking {
        val teste = ContatosEntidade("teste")

        dao.novoContato(teste)

        val item = dao.retornarContatos()
        // problema com livedata, talvez seja junit4?, talvez usar outro tipo de assert?

        //assertEquals(item[0], teste)//item[0]
    }

    @Test
    fun mensagens() = runBlocking{
        val contatoTeste = ContatosEntidade("teste")
        val teste = ConversaEntidade(contatoTeste).apply {
            mensagem = "teste"
        }

        dao.inserirMensagem(teste)

        val item = dao.receberConversa()

        //assertEquals(item[0], teste)
    }

    @Test
    fun contatoRecebeMensagem() = runBlocking{
        val contato = ContatosEntidade("jan")
        val conversa = ConversaEntidade(contato).apply {
            mensagem = "testando duas tabelas"
        }

        dao.novoContato(contato)
        dao.inserirMensagem(conversa)

        //mmudar aq
        val item = dao.receberConversa()

        //assertEquals(item[0], conversa)
        //assertEquals(item[0].contatosConversaIds, contato)
    }
}
//verificar ligacao, estudar melhor método para link entre contato e conversa