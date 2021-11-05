package com.malfaa.firebasechat.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malfaa.firebasechat.databinding.MensagemBinding
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import com.malfaa.firebasechat.viewmodel.ConversaViewModel


class ConversaAdapter(): ListAdapter<ConversaEntidade, ConversaAdapter.ViewHolder>(ConversaDiffCallBack()) {
    class ViewHolder private constructor(val binding : MensagemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: ConversaEntidade){
            val params = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.END
                marginEnd = 10
                bottomMargin = 20
                topMargin = 20
            }
            if (item.souEu){
                binding.conteudoDaMensagem.text = item.mensagem
                binding.horaDisplay.text = item.horario
                binding.caixaMensagem.setBackgroundColor(Color.GRAY)
                binding.horaDisplay.layoutParams = params
                binding.caixaMensagem.layoutParams = params
            }else{
                binding.conteudoDaMensagem.text = item.mensagem
                binding.horaDisplay.text = item.horario
            }

        }
        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MensagemBinding.inflate(layoutInflater,parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class ConversaDiffCallBack: DiffUtil.ItemCallback<ConversaEntidade>(){
        override fun areItemsTheSame(
            oldItem: ConversaEntidade,
            newItem: ConversaEntidade
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ConversaEntidade,
            newItem: ConversaEntidade
        ): Boolean {
            return oldItem === newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}