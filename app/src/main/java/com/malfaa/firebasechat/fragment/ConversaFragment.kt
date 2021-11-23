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
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.adapter.ContatosAdapter
import com.malfaa.firebasechat.adapter.ConversaAdapter
import com.malfaa.firebasechat.databinding.ConversaFragmentBinding
import com.malfaa.firebasechat.fragment.ContatosFragment.Companion.database
import com.malfaa.firebasechat.fragment.ContatosFragment.Companion.num
import com.malfaa.firebasechat.fragment.ContatosFragment.Companion.selfUid
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import com.malfaa.firebasechat.safeNavigate
import com.malfaa.firebasechat.viewmodel.ConversaViewModel
import com.malfaa.firebasechat.viewmodelfactory.ConversaViewModelFactory

class ConversaFragment : Fragment() {

    private lateinit var viewModel: ConversaViewModel
    private lateinit var binding: ConversaFragmentBinding
    private lateinit var viewModelFactory: ConversaViewModelFactory

    val args : ConversaFragmentArgs by navArgs() // poderia passar o novo random number

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

        val mAdapter = ConversaAdapter()
        binding.conversaRecyclerView.adapter = mAdapter

        viewModel.recebeConversa.observe(viewLifecycleOwner, {
            mAdapter.submitList(it.toMutableList())
        })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            retornaOrdem()
        }

        callback.isEnabled

        binding.enviarBtn.setOnClickListener{
            viewModel.adicionandoMensagem(ConversaEntidade(companionArguments.uid).apply {
                //souEu = selfUid.toString()
                mensagem = binding.mensagemEditText.text.toString()
                horario = viewModel.setHorarioMensagem
                Log.d("Mensagem:", mensagem)
                Log.d("Horario:", horario)
            })

            adicionaMensagemAoFirebase()

            binding.mensagemEditText.setText("")
        }
    }
    private fun retornaOrdem(){
        this.findNavController().safeNavigate(ConversaFragmentDirections.actionConversaFragmentToContatosFragment())
        ContatosAdapter.usuarioDestino.value = false
    }


    private fun adicionaMensagemAoFirebase(){

        // FIXME: 23/11/2021 problema é aqui abaixo
        val uid: String = database.reference.child("Users").child(num).child("uid").get().result.value.toString()
        val conversaId : String

        if(selfUid?.length!! < uid.length){
            conversaId = selfUid+uid
            Log.d("ConversaId", conversaId)
        }else{
            conversaId = uid+selfUid
            Log.d("ConversaId", conversaId)
        }
        val referencia = database.getReference("Conversas").child(conversaId)

        val mensagem = ConversaEntidade(uid).apply {
            horario = viewModel.setHorarioMensagem
            mensagem = binding.mensagemEditText.text.toString()
            myUid
        }

        referencia.push().child(selfUid/**talvez usar {num}?**/).setValue(mensagem)

    }
}