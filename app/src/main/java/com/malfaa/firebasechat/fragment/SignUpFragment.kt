package com.malfaa.firebasechat.fragment

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.databinding.SignUpFragmentBinding
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.safeNavigate
import com.malfaa.firebasechat.viewmodel.SignUpViewModel
import com.malfaa.firebasechat.viewmodelfactory.SignUpViewModelFactory

class SignUpFragment : Fragment() {

    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: SignUpFragmentBinding
    private lateinit var viewModelFactory: SignUpViewModelFactory

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment_main, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val dataSource = MeuDatabase.recebaDatabase(application).meuDao()
        viewModelFactory = SignUpViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[SignUpViewModel::class.java]

        binding.signIn.setOnClickListener {
            createSignInIntent()
        }
    }
    private fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
        // [END auth_fui_create_intent]
    }

    // FIXME: 13/12/2021 Como colocar foto dos users
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            // TODO: 15/12/2021 Adicionar no signUp a imagem própria e tbm pelo os contatos, que a imagem será disponibilizada pelos Contatos
            val user = FirebaseAuth.getInstance().currentUser
            viewModel.adicaoInfosPessoal(user)
            viewModel.adicaoDeUserAoFDB(user)
            findNavController().safeNavigate(SignUpFragmentDirections.actionSignUpFragmentToLoadingFragment())
        } else {
            Log.d("Erro SignUpResult", "Erro")
        }
    }
}