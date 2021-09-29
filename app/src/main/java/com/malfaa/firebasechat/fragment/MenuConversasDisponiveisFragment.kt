package com.malfaa.firebasechat.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.viewmodel.MenuConversasViewModel

class MenuConversasDisponiveisFragment : Fragment() {

    companion object {
        fun newInstance() = MenuConversasDisponiveisFragment()
    }

    private lateinit var viewModelMenu: MenuConversasViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.menu_conversas_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelMenu = ViewModelProvider(this).get(MenuConversasViewModel::class.java)
        // TODO: Use the ViewModel
    }

}