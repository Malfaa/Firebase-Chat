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
import com.malfaa.firebasechat.adapter.ConversaAdapter
import com.malfaa.firebasechat.dataFormato
import com.malfaa.firebasechat.databinding.ConversaFragmentBinding
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.safeNavigate
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.usuarioDestino
import com.malfaa.firebasechat.viewmodel.ConversaViewModel
import com.malfaa.firebasechat.viewmodel.ConversaViewModel.Companion.setHorarioMensagem
import com.malfaa.firebasechat.viewmodelfactory.ConversaViewModelFactory

class ConversaFragment : Fragment() {

    private lateinit var viewModel: ConversaViewModel
    lateinit var binding: ConversaFragmentBinding
    private lateinit var viewModelFactory: ConversaViewModelFactory
    private val args : ConversaFragmentArgs by navArgs()

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

        viewModel.taskConversa()

        viewModel.conversa.observe(viewLifecycleOwner,{
            mAdapter.submitList(it.asReversed().toMutableList())
        })

        viewModel.horario.observe(viewLifecycleOwner,{
            horario -> setHorarioMensagem = dataFormato(horario)
        })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            retornaOrdem()
        }
        callback.isEnabled

        binding.enviarBtn.setOnClickListener{
            try{
                viewModel.adicionaMensagemAoFirebase()
                Log.d("Firebase", "Enviado")
            //aqui

            }catch (e: Exception){
                Log.d("Error", e.toString())
            }
            binding.mensagemEditText.setText("")
        }
    }

    private fun retornaOrdem(){
        this.findNavController().safeNavigate(ConversaFragmentDirections.actionConversaFragmentToContatosFragment())
        usuarioDestino.value = false
    }

//    private fun adicionaMensagemAoFirebase(){ // TODO: 07/12/2021 colocar essa fun no viewmodel
//
//        viewModel.retornaHorario()
//
//        val conversaId = viewModel.testeConversaUid(LoadingViewModel.meuNum.value, args.contato.number)
//
//        val referenciaMensagem = database.getReference(CONVERSA_REFERENCIA).child(conversaId)
//
//        val mensagem = ConversaEntidade().apply {
//            uid = companionArguments.contato.uid
//            horario = setHorarioMensagem
//            mensagem = binding.mensagemEditText.text.toString()
//            myUid = meuUid.toString()
//            idConversaGerada = conversaId
//        }
//        referenciaMensagem.push().setValue(mensagem)
//    }

    // TODO: 01/12/2021 colocar foto das pessoas nos contatos (ou não)
}
// TODO: 08/12/2021 alterar os valores do FRAGMENT p/ VIEWMODEL