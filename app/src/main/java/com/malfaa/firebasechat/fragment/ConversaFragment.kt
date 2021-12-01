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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.adapter.ContatosAdapter
import com.malfaa.firebasechat.adapter.ConversaAdapter
import com.malfaa.firebasechat.databinding.ConversaFragmentBinding
import com.malfaa.firebasechat.fragment.ContatosFragment.Companion.database
import com.malfaa.firebasechat.fragment.ContatosFragment.Companion.myUid
import com.malfaa.firebasechat.getDateTime
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
    private lateinit var conversa: ArrayList<String>
    private val args : ConversaFragmentArgs by navArgs()

    companion object{
        lateinit var companionArguments : ConversaFragmentArgs
        val referenciaConversa = database.reference.child("Conversas").get().addOnSuccessListener {
            Log.d("Dados", "Dados recuperados")
        }.addOnFailureListener{
            Log.d("Dados", "Dados não encontrados")
        }

//        viewModel.num.observe(viewLifecycleOwner,{
//            valor ->
//            if (valor != null){
//                uid: String
//                get() {
//                    return referenciaUser.result.child(num).child("uid").value.toString() // Aqui
//                }
//            }else{
//                Log.d("Error", "Falha ao referenciar")
//            }
//        })
//        lateinit var uid: String
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

        conversa = arrayListOf<String>()
        task()


        viewModel.recebeConversaRoom.observe(viewLifecycleOwner, { // TODO: 25/11/2021 aqui
            mAdapter.submitList(it.toMutableList())
        })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            retornaOrdem()
        }
        callback.isEnabled

        Log.d("Hora", getDateTime(Date().time.toString()).toString())

        binding.enviarBtn.setOnClickListener{
            try{
                adicionaMensagemAoFirebase()
                Log.d("Firebase", "Enviado")

                viewModel.adicionandoMensagem(ConversaEntidade(companionArguments.uid).apply {
                    mensagem = binding.mensagemEditText.text.toString()
                    horario = viewModel.setHorarioMensagem.toString()
                    Log.d("Mensagem", mensagem)
                    Log.d("Horario", horario)
                })
            }catch (e: Exception){
                Log.d("Error", e.toString())
            }
            binding.mensagemEditText.setText("")
        }
    }

    private fun task() {
        val conversaId = viewModel.conversaUid(myUid.toString(), args.uid)

        val listaConteudoFirebase = database.getReference("Conversas")

        listaConteudoFirebase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (teste in snapshot.children){
                        //val mensagens = teste.getValue(ConversaEntidade::class.java)
                        val mensagens = teste.value  // FIXME: 30/11/2021 funciona, retorna todas as conversas, próximo passo é corrigir o filtro e passar para o room por meio do listener
                        conversa.add(mensagens!!.toString())
                   }
                Log.d("Log", conversa.toString())
                }

                // TODO: 30/11/2021 posso adicionar esse listener ao room, assim ele att o local e manda pro display que é o adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun retornaOrdem(){
        this.findNavController().safeNavigate(ConversaFragmentDirections.actionConversaFragmentToContatosFragment())
        ContatosAdapter.usuarioDestino.value = false
    }

    private fun adicionaMensagemAoFirebase(){

        val conversaId = viewModel.conversaUid(myUid.toString(), args.uid)

        val referenciaMensagem = database.getReference("Conversas").child(conversaId)

        val mensagem = ConversaEntidade(companionArguments.uid).apply {
            horario = viewModel.setHorarioMensagem.toString()
            mensagem = binding.mensagemEditText.text.toString()
            myUid
        }

        referenciaMensagem.push().setValue(mensagem)// TODO: 25/11/2021 arrumar o push com o adapter  {referenciaMensagem.child(myUid!!).push().setValue(mensagem)}

    }
}

/*
    Ideia é resgatar o id da conversa, pegar os ids construídos aleatoriamente e dar display -> daí ver se é melhor o firebase atualizar o room que dará o display correto por já ter a estrutura.
                                                                                                ou usar o firebase p/ já atualizar o adapter.

    database.reference.child("Conversas").child("conversaId").get()

* */