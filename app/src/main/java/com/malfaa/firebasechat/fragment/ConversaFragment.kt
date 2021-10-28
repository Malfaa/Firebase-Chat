package com.malfaa.firebasechat.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.malfaa.firebasechat.Data
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.adapter.ContatosAdapter
import com.malfaa.firebasechat.adapter.ConversaAdapter
import com.malfaa.firebasechat.databinding.ConversaFragmentBinding
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import com.malfaa.firebasechat.safeNavigate
import com.malfaa.firebasechat.viewmodel.ConversaViewModel
import com.malfaa.firebasechat.viewmodelfactory.ConversaViewModelFactory
import java.util.*

class ConversaFragment : Fragment() {

    private lateinit var viewModel: ConversaViewModel
    private lateinit var binding: ConversaFragmentBinding
    private lateinit var viewModelFactory: ConversaViewModelFactory

    val args : ConversaFragmentArgs by navArgs()

    companion object{
        lateinit var companionArguments : ConversaFragmentArgs
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.conversa_fragment, container, false)

        companionArguments = args

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val dataSource = MeuDatabase.recebaDatabase(application).meuDao()

        viewModelFactory = ConversaViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[ConversaViewModel::class.java]
        binding.viewModel = viewModel

        val mAdapter = ConversaAdapter(dataSource)
        binding.conversaRecyclerView.adapter = mAdapter

        viewModel.recebeConversa.observe(viewLifecycleOwner, {
            mAdapter.submitList(it.toMutableList())
        })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            retornaOrdem()
        }

        callback.isEnabled

        binding.enviarBtn.setOnClickListener{
            viewModel.adicionandoMensagem(ConversaEntidade(companionArguments.contatoId).apply {
                souEu = true
                mensagem = binding.mensagemEditText.text.toString()
                horario = viewModel.setHorarioMensagem.toString() //Data(Date()).toString()
                Log.d("Mensagem:", mensagem)
                Log.d("Horario:", horario)

            }) // TODO: 26/10/2021 melhorar o seter de horarios, bug que faz com que o display mostre o local armazenado no horário ao invés do horário
            binding.mensagemEditText.setText("")
        }
    }
    fun retornaOrdem(){
        this.findNavController().safeNavigate(ConversaFragmentDirections.actionConversaFragmentToContatosFragment())
        ContatosAdapter.usuarioDestino.value = false
    }


}