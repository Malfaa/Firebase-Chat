package com.malfaa.firebasechat

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.Repositorio
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.viewmodel.ContatosViewModel

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TesteDatabase {
    private lateinit var database: MeuDatabase
    private lateinit var meuDao: MeuDao
    private lateinit var rep: Repositorio
    private lateinit var viewModel: ContatosViewModel

    @Before
    fun testeDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, database::class.java).build()
        meuDao = database.meuDao()
        rep = Repositorio(meuDao)
        viewModel = ContatosViewModel(rep)
    }


    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.malfaa.firebasechat", appContext.packageName)
    }
}