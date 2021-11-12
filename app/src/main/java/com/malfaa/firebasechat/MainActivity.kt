package com.malfaa.firebasechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // TODO: 05/11/2021 Se estiver autenticado ir direto aos contatos, se não, inscrever-se
        // FIXME: 11/11/2021 arrumar logo launcher
    }
}