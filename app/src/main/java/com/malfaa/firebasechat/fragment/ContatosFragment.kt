package com.malfaa.firebasechat.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.adapter.ContatosAdapter
import com.malfaa.firebasechat.databinding.AdicionaContatoFragmentBinding
import com.malfaa.firebasechat.databinding.ContatosFragmentBinding
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.viewmodel.ContatosViewModel
import com.malfaa.firebasechat.viewmodelfactory.ContatosViewModelFactory
import android.content.Intent
import com.malfaa.firebasechat.DialogFragment
import com.malfaa.firebasechat.viewmodel.AdicionaContatoViewModel


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

    private fun retornaDao():MeuDao{
        val application = requireNotNull(this.activity).application
        return MeuDatabase.recebaDatabase(application).meuDao()

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
            //findNavController().safeNavigate(ContatosFragmentDirections.actionContatosFragmentToAdicionaContatoFragment())
            alertDialogAdicionarContato()
        }

        ContatosAdapter.deletarUsuario.observe(viewLifecycleOwner, {
                condicao ->
            if (condicao){
                alertDialogDeletarContato()
            }else{
                Log.d("Del", "Sem usuario p/ deletar")
            }
        })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            Log.d("status", "apertou back")
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        }
        callback.isEnabled

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

    private fun voltaDeletarValParaNormal(){
        ContatosAdapter.deletarUsuario.value = false
    }
    private fun alertDialogDeletarContato(){
        val construtor = AlertDialog.Builder(requireActivity())
        val idParaDeletar = ContatosAdapter.idItem

        construtor.setTitle(R.string.tituloDeletarContato)
        construtor.setMessage(R.string.mensagemDeletarContato)
        construtor.setPositiveButton("Confirmar") { dialogInterface: DialogInterface, _: Int ->
            viewModel.removeContato(idParaDeletar)
            Toast.makeText(context, "Contato Deletado.", Toast.LENGTH_SHORT).show()
            voltaDeletarValParaNormal()
            dialogInterface.cancel()
        }
        construtor.setNegativeButton("Cancelar"){
            dialogInterface:DialogInterface, _: Int ->
            voltaDeletarValParaNormal()
            dialogInterface.cancel()
        }

        val alerta = construtor.create()
        alerta.show()
    }
    private fun alertDialogAdicionarContato(){
        Log.d("Status", "Função sendo chamada")
        val construtor = AlertDialog.Builder(requireActivity())
        var any: Int = 0
        val adicionarContBinding = DataBindingUtil.inflate<AdicionaContatoFragmentBinding>(layoutInflater,R.layout.adiciona_contato_fragment,null,false)
        construtor.setTitle(R.string.tituloAdicionarContato)
        construtor.setView(adicionarContBinding.root)

        construtor.setPositiveButton("Adicionar"){
                dialogo, _ ->
            if(adicionarContBinding.contatoEmail.text.isNotEmpty()){
                AdicionaContatoViewModel(retornaDao()).adicionaContato(ContatosEntidade(any).apply {
                    nome = adicionarContBinding.contatoEmail.text.toString()
                })

                // TODO: 17/11/2021 Pesquisa um contato e o adiciona. Envia para o firebase que retornará o valor do email e usará assim p/ conversar

                dialogo.cancel()
                Toast.makeText(context, "Contato Adicionado!", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(context, "Contato Inválido. Tente novamente.", Toast.LENGTH_SHORT).show()
                adicionarContBinding.contatoEmail.text.clear()
            }
        }

        val alerta = construtor.create()
        alerta.show()

    }

    // TODO: 03/11/2021 escolher entre usar o novo modo de adicionar contato ou o antigo que é por navegação
    // FIXME: 04/11/2021 corrigir bug de apagar vários contatos em seguida
}