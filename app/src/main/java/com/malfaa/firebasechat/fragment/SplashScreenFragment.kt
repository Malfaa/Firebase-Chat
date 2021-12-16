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
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.databinding.SplashScreenFragmentBinding
import com.malfaa.firebasechat.safeNavigate

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

    private lateinit var binding: SplashScreenFragmentBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.splash_screen_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        val usuario = mAuth.currentUser

        val animacao = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in)
        binding.let {
            it.logo.startAnimation(animacao)
            it.baixoB.startAnimation(animacao)
            it.cimaB.startAnimation(animacao)
            it.dirB.startAnimation(animacao)
            it.dirC.startAnimation(animacao)
            it.esqB.startAnimation(animacao)
            it.esqC.startAnimation(animacao)
        }


        Handler(Looper.getMainLooper()).postDelayed({
            if(usuario == null) {
                this.findNavController().safeNavigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToSignUpFragment())
            }else{
                this.findNavController().safeNavigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToContatosFragment())
            }
        }, 1800)
    }
}