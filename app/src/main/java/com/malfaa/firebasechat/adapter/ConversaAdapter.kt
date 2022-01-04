package com.malfaa.firebasechat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.databinding.MensagemBinding
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.myUid

class ConversaAdapter : ListAdapter<ConversaEntidade, ConversaAdapter.ViewHolder>(ConversaDiffCallBack()) {
    class ViewHolder private constructor(var binding : MensagemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ConversaEntidade){
            if (item.meuUid == myUid){
                binding.conteudoDaMensagem.text = item.mensagem
                binding.horaDisplay.text = item.horario
                binding.cardViewDoConteudoMensagem.setBackgroundResource(R.drawable.corner_raio_sender)
            }else{
                binding.conteudoDaMensagem.text = item.mensagem
                binding.horaDisplay.text = item.horario
                binding.cardViewDoConteudoMensagem.setBackgroundResource(R.drawable.corner_raio_receiver)
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