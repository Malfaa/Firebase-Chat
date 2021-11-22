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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.adapter.ContatosAdapter
import com.malfaa.firebasechat.adapter.ConversaAdapter
import com.malfaa.firebasechat.databinding.ConversaFragmentBinding
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
    private lateinit var mAuth: FirebaseAuth

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
        val user = FirebaseAuth.getInstance().currentUser

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
                souEu = selfUid.toString()
                mensagem = binding.mensagemEditText.text.toString()
                horario = viewModel.setHorarioMensagem
                Log.d("Mensagem:", mensagem)
                Log.d("Horario:", horario)
            })
            // TODO: 17/11/2021 Quando for enviar a mensagem, enviar tanto para o room quanto p/ o firebase

            adicionaMensagemAoFirebase(user)

            binding.mensagemEditText.setText("")
        }
    }
    private fun retornaOrdem(){
        this.findNavController().safeNavigate(ConversaFragmentDirections.actionConversaFragmentToContatosFragment())
        ContatosAdapter.usuarioDestino.value = false
    }

    private fun adicionaMensagemAoFirebase(user: FirebaseUser?){
        //val ref = ContatosFragment.database.getReference(user?.displayName.toString()).child("Mensagens").child(ConversaFragment.companionArguments.uid)
        val ref = ContatosFragment.database.getReference("Mensagens").child("$selfUid | ${companionArguments.uid}") //problema aqui
        // TODO: 22/11/2021 O que pode  ser feito é, ao invés de usar os dois uid's para criar a conversa, só é colocado o do outro user, assim o adapter sempre att o próprio UID para novas mensagens que não foram enviadas pelo próprio user
        val valores = ConversaEntidade(user?.uid.toString()).apply{
            souEu = selfUid.toString()
            mensagem = binding.mensagemEditText.text.toString()
            horario = viewModel.setHorarioMensagem
        }// TODO: 22/11/2021 meter um "para: nome da pessoa caso não user o segundo ref
        ref.setValue(valores)
    }
}