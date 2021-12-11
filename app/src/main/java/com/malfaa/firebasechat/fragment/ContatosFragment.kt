package com.malfaa.firebasechat.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import com.malfaa.firebasechat.viewmodel.AdicionaContatoViewModel
import com.malfaa.firebasechat.viewmodel.ContatosViewModel
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.EMAIL_REFERENCIA
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.UID_REFERENCIA
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.auth
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.deletarUsuario
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.meuUid
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.referenciaContato
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.referenciaUser
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.uidItem
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.usuarioDestino
import com.malfaa.firebasechat.viewmodel.LoadingViewModel
import com.malfaa.firebasechat.viewmodel.LoadingViewModel.Companion.meuNum
import com.malfaa.firebasechat.viewmodelfactory.ContatosViewModelFactory

class ContatosFragment : Fragment() {

    private lateinit var viewModel: ContatosViewModel
    private lateinit var binding: ContatosFragmentBinding
    private lateinit var viewModelFactory: ContatosViewModelFactory
    private lateinit var loadingViewmodel: LoadingViewModel

    companion object{
        private lateinit var meuContato: ContatosEntidade
        private lateinit var numeroRef: String
        private const val online = "Online"
        private const val offline = "Offline"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.contatos_fragment, container, false)

        return binding.root
    }

    private fun retornaDao():MeuDao {
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
        loadingViewmodel = LoadingViewModel(dataSource)
        loadingViewmodel.retornaMeuNumero()

        val mAdapter = ContatosAdapter()
        binding.RVContatos.adapter = mAdapter

        meuNum.observe(viewLifecycleOwner,{
                valor ->
            if (valor != null){
                binding.numero.text = valor.toString()
                meuContato = ContatosEntidade(meuUid.toString()).apply {
                    nome = auth.currentUser?.displayName.toString()
                    email = auth.currentUser?.email.toString()
                    number = valor
                }
                numeroRef = valor.toString()

            }else{
                Log.d("Error", "Não foi possível resgatar número")
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.taskContatos(numeroRef)
        }, 300)

        viewModel.contatos.observe(viewLifecycleOwner, { // TODO: 10/12/2021 talvez substituir o UI resgatado pelo firebase pelo do Room, sem ideias ainda
            mAdapter.submitList(it.toMutableList())
            //viewModel.adicionaAosContatos() // TODO: 10/12/2021 talvez solucione problema do getNumber() = null
        })

        viewModel.status.observe(viewLifecycleOwner,{
            estado ->
            if (estado){
                binding.status.text = online
                binding.statusIcone.setImageResource(R.drawable.ic_status_online)
            }else{
                binding.status.text = offline
                binding.statusIcone.setImageResource(R.drawable.ic_status_offline)
                Toast.makeText(context, "Reinicie o Aplicativo.",Toast.LENGTH_LONG).show()
            }
        })

        binding.adicaoNovoContato.setOnClickListener {
            Log.d("teste", viewModel.contatos.value.toString()) // resultado = null
            alertDialogAdicionarContato()
        }

        deletarUsuario.observe(viewLifecycleOwner, {
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

        usuarioDestino.observe(viewLifecycleOwner, {
                condicao ->
            if (condicao){
                val argumento = //uidItem.uid // TODO: 07/12/2021 arrumar aqui
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
        deletarUsuario.value = false
    }

    // FIXME: 08/12/2021 COLOCAR APENAS O NUM P/ O CONTATO, ASSIM RETORNA TODAS AS INFOS NECESSÁRIAS
    // FIXME: 02/12/2021 Trocar o ID para ID_CONVERSA_GERADA (talvez n precise, é necessário um teste quando o room estiver atualizado com o firebase db)
    private fun alertDialogDeletarContato(){
        val construtor = AlertDialog.Builder(requireActivity())
        val idParaDeletar = uidItem

        construtor.setTitle(R.string.tituloDeletarContato)
        construtor.setMessage(R.string.mensagemDeletarContato)
        construtor.setPositiveButton("Confirmar") { dialogInterface: DialogInterface, _: Int ->
            viewModel.removeContato(idParaDeletar)
            referenciaContato.child(meuNum.value.toString()).child(idParaDeletar.number.toString()).removeValue() // FIXME: 07/12/2021 arrumar auqi
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
            val num: EditText = adicionarContBinding.contatoNumero
            val reference = referenciaUser.result.child(num.text.toString())
            try{
                if(reference.key.toString() == num.text.toString() ){ // TODO: 08/12/2021 colocar pra por o nome tbm pra add
                    val ref = reference.getValue(ContatosEntidade::class.java)

                    val contato = ContatosEntidade(ref?.uid.toString()).apply {
                        nome = ref?.nome!!
                        email = ref.email
                        number = ref.number
                    }
                    referenciaContato.child(meuNum.value.toString()).child(num.text.toString()).setValue(contato)
                    referenciaContato.child(num.text.toString()).child(meuNum.value.toString()).setValue(meuContato)

                    AdicionaContatoViewModel(retornaDao()).adicionaContato(ContatosEntidade(referenciaUser.result.child(num.text.toString()).child(
                        UID_REFERENCIA).value.toString()).apply {
                        nome = adicionarContBinding.contatoNome.text.toString()
                        email = referenciaUser.result.child(num.text.toString()).child(EMAIL_REFERENCIA).value.toString()
                        number = num.text.toString().toLong()
                    })
                    dialogo.cancel()
                    Toast.makeText(context, "Contato Adicionado!", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(context, "Contato Inválido. Tente novamente.", Toast.LENGTH_SHORT).show()
                    adicionarContBinding.contatoNumero.text.clear()
                }
            }catch (e: Exception){
                Log.d("Error", e.toString())
            }
        }
        val alerta = construtor.create()
        alerta.show()
    }
    // FIXME: 04/11/2021 corrigir bug de apagar vários contatos em seguida
}
// TODO: 06/12/2021 quando receber mensagem de um user que n tenha salvo, aparecer nos contatos
