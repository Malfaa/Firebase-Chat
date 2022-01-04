package com.malfaa.firebasechat.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.databinding.FragmentLoadingBinding
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.safeNavigate
import com.malfaa.firebasechat.viewmodel.LoadingViewModel
import com.malfaa.firebasechat.viewmodelfactory.LoadingViewModelFactory

class LoadingFragment : Fragment() {

    private lateinit var binding: FragmentLoadingBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: LoadingViewModel
    private lateinit var viewModelFactory: LoadingViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_loading, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val dataSource = MeuDatabase.recebaDatabase(application).meuDao()
        viewModelFactory = LoadingViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoadingViewModel::class.java]

        mAuth = FirebaseAuth.getInstance()
        val usuario = mAuth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if(usuario != null) {
                viewModel.retornaMeuNumero()
                this.findNavController().safeNavigate(LoadingFragmentDirections.actionLoadingFragmentToContatosFragment())
            }
        }, 5000)
    }
}
