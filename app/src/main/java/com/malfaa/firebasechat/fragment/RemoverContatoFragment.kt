package com.malfaa.firebasechat.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.util.DataUtils
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.adapter.ContatosAdapter
import com.malfaa.firebasechat.databinding.RemoverContatoFragmentBinding
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.viewmodel.ContatosViewModel
import com.malfaa.firebasechat.viewmodel.RemoverContatoViewModel
import com.malfaa.firebasechat.viewmodelfactory.ContatosViewModelFactory
import com.malfaa.firebasechat.viewmodelfactory.RemoverContatoViewModelFactory

class RemoverContatoFragment : Fragment() {

    private lateinit var binding: RemoverContatoFragmentBinding
    private lateinit var viewModelFactory: RemoverContatoViewModelFactory
    companion object {
        fun newInstance() = RemoverContatoFragment()
    }

    private lateinit var viewModel: RemoverContatoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.remover_contato_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val dataSource = MeuDatabase.recebaDatabase(application).meuDao()
        viewModelFactory = RemoverContatoViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[RemoverContatoViewModel::class.java]
        //binding.viewModel = viewModel
//
//        binding.confirmarBtn.setOnClickListener {
//            viewModel.removeContato(idParaDeletar)
//            Toast.makeText(context, "Contato Deletado.", Toast.LENGTH_SHORT).show()
//            Log.d("Var Deletar Status: ", "${ContatosAdapter.deletarUsuario.value}")
//            voltaDeletarValParaNormal()
//            Log.d("Var Deletar Status: ", "${ContatosAdapter.deletarUsuario.value}")
//        }


    }

}