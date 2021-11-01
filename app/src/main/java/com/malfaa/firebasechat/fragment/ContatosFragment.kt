package com.malfaa.firebasechat.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.adapter.ContatosAdapter
import com.malfaa.firebasechat.databinding.ContatosFragmentBinding
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.safeNavigate
import com.malfaa.firebasechat.viewmodel.ContatosViewModel
import com.malfaa.firebasechat.viewmodelfactory.ContatosViewModelFactory

class ContatosFragment : Fragment() {

    private lateinit var viewModel: ContatosViewModel
    private lateinit var binding: ContatosFragmentBinding
    private lateinit var viewModelFactory: ContatosViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.contatos_fragment, container, false)

        return binding.root
    }

    private fun SetupVariaveisIniciais() {
        val application = requireNotNull(this.activity).application
        val dataSource = MeuDatabase.recebaDatabase(application).meuDao()
        viewModelFactory = ContatosViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[ContatosViewModel::class.java]
        binding.viewModel = viewModel

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SetupVariaveisIniciais()

        val mAdapter = ContatosAdapter()
        binding.RVContatos.adapter = mAdapter

        viewModel.verificaRecyclerView.observe(viewLifecycleOwner, {
            mAdapter.submitList(it.toMutableList())
        })

        binding.adicaoNovoContato.setOnClickListener {
            findNavController().safeNavigate(ContatosFragmentDirections.actionContatosFragmentToAdicionaContatoFragment())
        }

        ContatosAdapter.deletarUsuario.observe(viewLifecycleOwner, {
            condicao ->
            if (condicao){
                val idParaDeletar = ContatosAdapter.idItem
                Log.d("Teste", "ta passando")
                teste()
//                viewModel.removeContato(idParaDeletar)
//                Toast.makeText(context, "Contato Deletado.", Toast.LENGTH_SHORT).show()
//                Log.d("Var Deletar Status: ", "${ContatosAdapter.deletarUsuario.value}")
//                voltaDeletarValParaNormal()
//                Log.d("Var Deletar Status: ", "${ContatosAdapter.deletarUsuario.value}")
            }else{
                Log.d("Del", "Sem usuario p/ deletar")
            }
                // TODO: 28/10/2021 novo layout que da pop up perguntando se realmente deseja deletar o usuario
            })


            ContatosAdapter.usuarioDestino.observe(viewLifecycleOwner, {
                    condicao ->
                if (condicao){
                    val argumento = ContatosAdapter.idItem.id
                    findNavController().navigate(
                        ContatosFragmentDirections.actionContatosFragmentToConversaFragment(argumento)
                    )
                    Log.d("Condicao", "foi até destino")
                }else{
                    Log.d("Condicao", "Retido")
                }
            })
        }

    fun voltaDeletarValParaNormal(){
        ContatosAdapter.deletarUsuario.value = false
    }

//    private fun showAlertFilter(): PopupWindow {
//        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val view = inflater.inflate(R.layout.confirma_deletar_contato,false)
//
//        return PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//    }

    // TODO: 01/11/2021 Ajustar um popup window para poder confirmar a exclusão de um contato


    fun teste(){
        val window = PopupWindow()
        val view = layoutInflater.inflate(R.layout.remover_contato_fragment, null)
        window.contentView = view

        window.showAtLocation(view, CENTER, 0, 0)
        window.isShowing
    }

// TODO: 20/10/2021 Layout precisa ser aprimorado
}
