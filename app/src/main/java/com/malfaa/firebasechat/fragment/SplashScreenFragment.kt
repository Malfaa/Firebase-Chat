package com.malfaa.firebasechat.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.databinding.SplashScreenFragmentBinding
import com.malfaa.firebasechat.safeNavigate
import com.malfaa.firebasechat.viewmodel.SplashScreenViewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

    private lateinit var viewModel: SplashScreenViewModel
    private lateinit var binding: SplashScreenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.splash_screen_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SplashScreenViewModel::class.java]

        val animacao = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in)
        binding.fcLogo.startAnimation(animacao)

        // TODO: 05/11/2021 Se estiver autenticado ir direto aos contatos, se não, inscrever-se
        // TODO: 11/11/2021 IF cadastrado, entra se não no
        Handler(Looper.getMainLooper()).postDelayed({
//            if(/*recebe google auth*/) {
//               this.findNavController().safeNavigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToSignInFragment())
//            }
            this.findNavController().safeNavigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToContatosFragment())
        }, 800)
    }
}
