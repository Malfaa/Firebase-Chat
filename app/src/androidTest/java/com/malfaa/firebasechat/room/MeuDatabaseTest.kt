package com.malfaa.firebasechat.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.viewmodel.AdicionaContatoViewModel
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MeuDatabaseTest : TestCase() {

    private lateinit var database: MeuDatabase
    private lateinit var dao: MeuDao
    private lateinit var vm: AdicionaContatoViewModel

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MeuDatabase::class.java).build()
        dao = database.meuDao()

        vm = AdicionaContatoViewModel(dao)
    }

    @After
    public override fun tearDown() {
        database.close()
    }

    @Test
    fun meuDao() = runBlocking {
        val teste = ContatosEntidade("teste")

        vm.adicionaContato(teste)// problema ou está aqui pra adicionar no db

        val item = vm.recebeContatos() // ou está aqui pra receber do db, pq ele espera null como retorno do assert abaixo

        assertEquals(item.value, teste)
    }
}