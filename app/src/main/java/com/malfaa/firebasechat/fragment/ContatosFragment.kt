package com.malfaa.firebasechat.fragment

import android.content.DialogInterface
import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.adapter.ContatosAdapter
import com.malfaa.firebasechat.databinding.AdicionaContatoFragmentBinding
import com.malfaa.firebasechat.databinding.ContatosFragmentBinding
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.viewmodel.AdicionaContatoViewModel
import com.malfaa.firebasechat.viewmodel.ContatosViewModel
import com.malfaa.firebasechat.viewmodelfactory.ContatosViewModelFactory


class ContatosFragment : Fragment() {

    private lateinit var viewModel: ContatosViewModel
    private lateinit var binding: ContatosFragmentBinding
    private lateinit var viewModelFactory: ContatosViewModelFactory

    companion object{
        val auth = FirebaseAuth.getInstance()
        val meuUid = FirebaseAuth.getInstance().uid
        val database = Firebase.database
        private const val USERS_REFERENCIA = "Users"
        private const val UID_REFERENCIA = "uid"
        private const val EMAIL_REFERENCIA = "email"
        private const val CONTATO_REFERENCIA = "Contatos"

        private val referenciaUser = database.reference.child(USERS_REFERENCIA).get().addOnSuccessListener {
            Log.d("Ref", "Dados Recuperados")
        }.addOnFailureListener{
            Log.d("Ref", "Falha em recuperar os dados")
        }
        private val referenciaContato = database.getReference(CONTATO_REFERENCIA)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.contatos_fragment, container, false)

        return binding.root
    }

    private fun retornaDao():MeuDao{
        val application = requireNotNull(this.activity).application
        return MeuDatabase.recebaDatabase(application).meuDao()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val dataSource = MeuDatabase.recebaDatabase(application).meuDao()
        viewModelFactory = ContatosViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[ContatosViewModel::class.java]
        binding.viewModel = viewModel

        viewModel.retornaMeuNumero()

        val mAdapter = ContatosAdapter()
        binding.RVContatos.adapter = mAdapter


        viewModel.taskContatos()

        viewModel.meuNum.observe(viewLifecycleOwner,{
                valor ->
            if (valor != null){
                binding.numero.text = valor
            }else{
                Log.d("Error", "Não foi possível resgatar número")
            }
        })

        viewModel.contatos.observe(viewLifecycleOwner, {
            mAdapter.submitList(it.toMutableList())
        })

        binding.adicaoNovoContato.setOnClickListener {
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
                val argumento = ContatosAdapter.uidItem.uid // TODO: 07/12/2021 arrumar aqui
                findNavController().navigate(
                    ContatosFragmentDirections.actionContatosFragmentToConversaFragment(argumento)
                )
                Log.d("Condicao", "foi até destino, $argumento")
            }else{
                Log.d("Condicao", "Retido")
            }
        })
    }

    private fun voltaDeletarValParaNormal(){
        ContatosAdapter.deletarUsuario.value = false
    }

    // FIXME: 02/12/2021 Trocar o ID para ID_CONVERSA_GERADA (talvez n precise, é necessário um teste quando o room estiver atualizado com o firebase db)
    private fun alertDialogDeletarContato(){
        val construtor = AlertDialog.Builder(requireActivity())
        val idParaDeletar = ContatosAdapter.uidItem

        construtor.setTitle(R.string.tituloDeletarContato)
        construtor.setMessage(R.string.mensagemDeletarContato)
        construtor.setPositiveButton("Confirmar") { dialogInterface: DialogInterface, _: Int ->
            viewModel.removeContato(idParaDeletar)
            referenciaContato.child(viewModel.meuNum.value.toString()).child(idParaDeletar.number).push().removeValue() // FIXME: 07/12/2021 arrumar auqi
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
        val construtor = AlertDialog.Builder(requireActivity())

        val adicionarContBinding = DataBindingUtil.inflate<AdicionaContatoFragmentBinding>(layoutInflater,R.layout.adiciona_contato_fragment,null,false)
        construtor.setTitle(R.string.tituloAdicionarContato)
        construtor.setView(adicionarContBinding.root)

        construtor.setPositiveButton("Adicionar"){
                dialogo, _ ->
            val num: String = adicionarContBinding.contatoNumero.text.toString()
            val meuContato = ContatosEntidade(meuUid.toString()).apply {
                nome = auth.currentUser?.displayName.toString()
                email = auth.currentUser?.email.toString()
                number =  viewModel.meuNum.toString()
            }

            if(referenciaUser.result.child(num).key.toString()== num ){
                val ref = referenciaUser.result.child(num).getValue(ContatosEntidade::class.java)

                val contato = ContatosEntidade(ref?.uid.toString()).apply {
                    nome = ref?.nome.toString()
                    email = ref?.email.toString()
                    number = ref?.number.toString()
                }

                referenciaContato.child(viewModel.meuNum.value.toString()).child(num).push().setValue(contato)
                referenciaContato.child(num).child(viewModel.meuNum.value.toString()).push().setValue(meuContato)

                AdicionaContatoViewModel(retornaDao()).adicionaContato(ContatosEntidade(referenciaUser.result.child(num).child(
                    UID_REFERENCIA).value.toString()).apply {
                    nome = adicionarContBinding.contatoNome.text.toString()
                    email = referenciaUser.result.child(num).child(EMAIL_REFERENCIA).value.toString()
                    number = num
                })

                dialogo.cancel()
                Toast.makeText(context, "Contato Adicionado!", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(context, "Contato Inválido. Tente novamente.", Toast.LENGTH_SHORT).show()
                adicionarContBinding.contatoNumero.text.clear()
            }
        }

        val alerta = construtor.create()
        alerta.show()
    }
    // FIXME: 04/11/2021 corrigir bug de apagar vários contatos em seguida
}
// TODO: 06/12/2021 quando receber mensagem de um user que n tenha salvo, aparecer nos contatos
