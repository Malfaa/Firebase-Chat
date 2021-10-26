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


class ConversaAdapter(meuDao: MeuDao): ListAdapter<ConversaEntidade, ConversaAdapter.ViewHolder>(ConversaDiffCallBack()) {

    //val dados = ConversaViewModel(meuDao).recebeConversa

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
        val params = LinearLayoutCompat.LayoutParams(
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.END
            marginEnd = 10
        }
        if (item.souEu){
            holder.binding.conteudoDaMensagem.text = item.mensagem
            holder.binding.horaDisplay.text = item.horario
            holder.binding.caixaMensagem.setBackgroundColor(Color.GRAY)
            holder.binding.horaDisplay.layoutParams = params
            holder.binding.caixaMensagem.layoutParams = params
            //Log.d("Dados", "${dados.value}")
            holder.bind(item)
        }else{
            holder.binding.conteudoDaMensagem.text = item.mensagem
            holder.binding.horaDisplay.text = item.horario

            holder.bind(item)
        }
    }
// FIXME: 20/10/2021 || string/hora da mensagem 21/10 || corrigir distância entre uma mensagem e outra || google auth
    // TODO: 26/10/2021 fazer alguma jeito para ser inserido uma mensagem sendo 'souEu' false
}