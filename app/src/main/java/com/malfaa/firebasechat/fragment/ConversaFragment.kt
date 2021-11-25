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
import com.malfaa.firebasechat.fragment.ContatosFragment.Companion.myUid
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import com.malfaa.firebasechat.safeNavigate
import com.malfaa.firebasechat.viewmodel.ConversaViewModel
import com.malfaa.firebasechat.viewmodelfactory.ConversaViewModelFactory

class ConversaFragment : Fragment() {

    private lateinit var viewModel: ConversaViewModel
    private lateinit var binding: ConversaFragmentBinding
    private lateinit var viewModelFactory: ConversaViewModelFactory

    private val args : ConversaFragmentArgs by navArgs()

    companion object{
        lateinit var companionArguments : ConversaFragmentArgs
        lateinit var num: String  // mudei aqui
//        val referenciaUser = database.reference.child("Users").get().addOnSuccessListener {
//            Log.d("Dados", "Dados recuperados")
//        }.addOnFailureListener{
//            Log.d("Dados", "Dados não encontrados")
//        }
//        val uid: String
//            get() {
//                return referenciaUser.result.child(num).child("uid").value.toString() // Aqui
//            }
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
        viewModel.retornaNumeroUser()

        val mAdapter = ConversaAdapter()
        binding.conversaRecyclerView.adapter = mAdapter


        viewModel.recebeConversa.observe(viewLifecycleOwner, { // TODO: 25/11/2021 aqui
            mAdapter.submitList(it.toMutableList())
        })


        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            retornaOrdem()
        }
        callback.isEnabled

        binding.enviarBtn.setOnClickListener{
            adicionaMensagemAoFirebase()//problema
            Log.d("Firebase", "Enviado")
            try{
                viewModel.adicionandoMensagem(ConversaEntidade(companionArguments.uid).apply {
                    mensagem = binding.mensagemEditText.text.toString()
                    horario = viewModel.setHorarioMensagem
                    Log.d("Mensagem", mensagem)
                    Log.d("Horario", horario)
                })
            }catch (e: Exception){
                Log.d("Error", e.toString())
            }



            binding.mensagemEditText.setText("")
        }
    }
    private fun retornaOrdem(){
        this.findNavController().safeNavigate(ConversaFragmentDirections.actionConversaFragmentToContatosFragment())
        ContatosAdapter.usuarioDestino.value = false
    }

    private fun adicionaMensagemAoFirebase(){

        val conversaId = viewModel.conversaUid(myUid.toString(), args.uid)

        val referenciaMensagem = database.getReference("Conversas").child(conversaId)

        val mensagem = ConversaEntidade(companionArguments.uid).apply {
            horario = viewModel.setHorarioMensagem
            mensagem = binding.mensagemEditText.text.toString()
            myUid
        }

        referenciaMensagem.push().child(myUid!!).setValue(mensagem)// TODO: 25/11/2021 arrumar o push com o adapter

    }
}
