package com.malfaa.firebasechat.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.databinding.AdicionaContatoFragmentBinding
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.setNome
import com.malfaa.firebasechat.viewmodel.AdicionaContatoViewModel
import com.malfaa.firebasechat.viewmodelfactory.AdicionaContatoViewModelFactory

class AdicionaContatoFragment : Fragment() {

    private lateinit var viewModel: AdicionaContatoViewModel
    private lateinit var binding: AdicionaContatoFragmentBinding
    private lateinit var viewModelFactory: AdicionaContatoViewModelFactory
    var any: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.adiciona_contato_fragment, container, false)

        return binding.root
    }

    private fun SetupVariaveisIniciais(){
        val application = requireNotNull(this.activity).application
        val dataSource = MeuDatabase.recebaDatabase(application).meuDao()
        viewModelFactory = AdicionaContatoViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[AdicionaContatoViewModel::class.java]
        binding.viewModel = viewModel

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SetupVariaveisIniciais()

        binding.adicionarContato.setOnClickListener{
            if(binding.contatoNome.text.isNotEmpty()){
                viewModel.adicionaContato(ContatosEntidade(any).apply {
                    nome = binding.contatoNome.text.toString() //troquei aqui auhauahuauhauhauhuahuahuha
                })

            Toast.makeText(context, "Contato Adicionado!", Toast.LENGTH_SHORT).show()
            this.findNavController().navigate(AdicionaContatoFragmentDirections.actionAdicionaContatoFragmentToContatosFragment())
        }else{
            Toast.makeText(context, "Contato Inválido.\n Tente novamente.", Toast.LENGTH_SHORT).show()
            binding.contatoNome.text.clear()
            }
        }

        /*E/AndroidRuntime: FATAL EXCEPTION: main
    Process: com.malfaa.firebasechat, PID: 2734
    java.lang.NullPointerException: Parameter specified as non-null is null: method kotlin.jvm.internal.Intrinsics.checkNotNullParameter, parameter nome
        at com.malfaa.firebasechat.UtilsKt.setNome(Utils.kt)*/


    }
// binding.adicionarContato.setOnClickListener{
//            viewModel.adicionaContato(ContatosEntidade(binding.contatoNome.text.toString()).apply {
//                nome = binding.contatoNome.text.toString()
//            }) // AQUI binding.contatoNome.setNome(binding.contatoNome.text.toString()
}