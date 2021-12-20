package com.malfaa.firebasechat.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malfaa.firebasechat.databinding.MensagemBinding
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.meuUid

class ConversaAdapter : ListAdapter<ConversaEntidade, ConversaAdapter.ViewHolder>(ConversaDiffCallBack()) {
    class ViewHolder private constructor(val binding : MensagemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: ConversaEntidade){
            val params = FrameLayout.LayoutParams(
                WRAP_CONTENT,
                WRAP_CONTENT
            ).apply {
                gravity = Gravity.END
                rightMargin = 10
                bottomMargin = 20
            }

            if (item.myUid == meuUid.toString()){
                binding.conteudoDaMensagem.text = item.mensagem
                binding.horaDisplay.text = item.horario
                binding.cardViewDoConteudoMensagem.setCardBackgroundColor(Color.parseColor("#49cbbe"))// FIXME: 20/12/2021 aqui
                binding.cardViewDoConteudoMensagem.layoutParams = params

            }else{
                binding.conteudoDaMensagem.text = item.mensagem
                binding.horaDisplay.text = item.horario
                //binding.cardViewDoConteudoMensagem.setCardBackgroundColor(Color.parseColor("#8a9bd7"))// FIXME: 20/12/2021 aqui
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