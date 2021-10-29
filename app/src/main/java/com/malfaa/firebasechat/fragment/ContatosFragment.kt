package com.malfaa.firebasechat.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            // FIXME: 28/10/2021 ele só apaga aquele contato quando o id recebe pelo navegar até ele, exemplo, se tem duas conversas
            //  eu navego até o que quero apagar e volto, assim segurando ele guarda no id o valor e assim faz a ação de apagar
            //PROBLEMA AQUI
            if (condicao){
                val idParaDeletar = ContatosAdapter.idItem
                viewModel.removeContato(idParaDeletar)
                Toast.makeText(context, "Contato Deletado.", Toast.LENGTH_SHORT).show()
                Log.d("Var Deletar Status: ", "${ContatosAdapter.deletarUsuario.value}")
                voltaDeletarValParaNormal()
                Log.d("Var Deletar Status: ", "${ContatosAdapter.deletarUsuario.value}")
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
// TODO: 20/10/2021 Layout precisa ser aprimorado
}