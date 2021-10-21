package com.malfaa.firebasechat.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malfaa.firebasechat.databinding.MensagemBinding
import com.malfaa.firebasechat.room.entidades.ConversaEntidade


class ConversaAdapter: ListAdapter<ConversaEntidade, ConversaAdapter.ViewHolder>(ConversaDiffCallBack()) {
    class ViewHolder private constructor(val binding : MensagemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ConversaEntidade){
            item.mensagem = item.mensagem //teste
            item.horario = binding.horaDisplay.toString()

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
        //holder.bind(item)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.END
        }
        if (item.souEu){ // FIXME: 21/10/2021 corrigir troca de lados referente ao sender
            holder.binding.caixaMensagem.setBackgroundColor(Color.GRAY)
            holder.binding.horaDisplay.gravity = Gravity.END
            holder.binding.caixaMensagem.foregroundGravity = Gravity.END
            holder.bind(item)
        }else{
            holder.bind(item)
        }
    }
// FIXME: 20/10/2021 corrigir lado, string e hora da mensagem 21/10
// Mensagens se dando replace corrigido, mudanças nas estruturas meudao e relacionados feitas, prox. correcao de lado/string/ horario das mensagens
}
//Colocar no adapter um IF antes de setar no view as configs, assim observando o SEND ele troca os layouts