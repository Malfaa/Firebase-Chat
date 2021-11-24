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
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.safeNavigate
import com.malfaa.firebasechat.viewmodel.ConversaViewModel
import com.malfaa.firebasechat.viewmodelfactory.ConversaViewModelFactory

class ConversaFragment : Fragment() {

    private lateinit var viewModel: ConversaViewModel
    private lateinit var binding: ConversaFragmentBinding
    private lateinit var viewModelFactory: ConversaViewModelFactory

    private val args : ConversaFragmentArgs by navArgs() // poderia passar o novo random number

    companion object{
        lateinit var companionArguments : ConversaFragmentArgs
        lateinit var num: String
        val referenciaUser = database.reference.child("Users").get().addOnSuccessListener {
            Log.d("Dados", "Dados recuperados")
        }.addOnFailureListener{
            Log.d("Dados", "Dados não encontrados")
        }



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

        //val uid = referenciaUser.result.child(num)

        binding.enviarBtn.setOnClickListener{ //PRoblema é que ta retornando um kotlin PONTO unit (kotlin.unit)

        val num = viewModel.retornaNumero()
        Log.d("Numero", num.toString())

            // FIXME: 24/11/2021 arrumar num, como conseguir o String Number do room usando livedata? Ou melhor usar outro tipo de método?

/*
            viewModel.adicionandoMensagem(ConversaEntidade(companionArguments.uid).apply {
                //souEu = selfUid.toString()
                mensagem = binding.mensagemEditText.text.toString()
                horario = viewModel.setHorarioMensagem
                Log.d("Mensagem:", mensagem)
                Log.d("Horario:", horario)
            })
*/

            binding.mensagemEditText.setText("")
        }
    }
    private fun retornaOrdem(){
        this.findNavController().safeNavigate(ConversaFragmentDirections.actionConversaFragmentToContatosFragment())
        ContatosAdapter.usuarioDestino.value = false
    }


    /*private fun adicionaMensagemAoFirebase(){

        // FIXME: 23/11/2021 problema é aqui abaixo
        num = viewModel.retornandoNumber(args.uid).toString()
        Log.d("UID", num)
        //val uid: String = referenciaUid.result.child(num).child("uid").value.toString()

        Log.d("UId", referenciaUid.toString())
        //Log.d("UId", uid)

        var conversaId : String = ""

        if(selfUid?.length!! < referenciaUid.result.child(num).child("uid").value.toString().length){
            conversaId = selfUid+referenciaUid.result.child(num).child("uid").value.toString()
            Log.d("ConversaId", conversaId)
        }else{
            conversaId = referenciaUid.result.child(num).child("uid").value.toString()+selfUid
            Log.d("ConversaId", conversaId)
        }
        if(selfUid?.length!! < referenciaUid.result.child(num).child("uid").value.toString().length){
                conversaId = selfUid+referenciaUid.result.child(num).child("uid").value.toString()
                Log.d("ConversaId", conversaId)
            }else{
                conversaId = referenciaUid.result.child(num).child("uid").value.toString()+selfUid
                Log.d("ConversaId", conversaId)
            }

//        val referenciaMensagem = database.getReference("Conversas").child(conversaId)
//
//        val mensagem = ConversaEntidade(uid).apply {
//            horario = viewModel.setHorarioMensagem
//            mensagem = binding.mensagemEditText.text.toString()
//            myUid
//        }
//
//        referenciaMensagem.push().child(selfUid/**talvez usar {num}?**/).setValue(mensagem)

    }*/
}