package com.malfaa.firebasechat.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malfaa.firebasechat.databinding.MensagemBinding
import com.malfaa.firebasechat.room.entidades.ConversaEntidade

class ConversaAdapter: ListAdapter<ConversaEntidade, ConversaAdapter.ViewHolder>(ConversaDiffCallBack()) {

    class ViewHolder private constructor(val binding : MensagemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ConversaEntidade){
            item.mensagem = binding.conteudoDaMensagem.toString()
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
        val mConstraintSet = ConstraintSet()
// mudar aqui pra receber o valor correto do id?
        if (item.contatosConversaIds.id.toInt() == 0){
            holder.binding.mensagem.setBackgroundColor(Color.GRAY)

            //--------------------------------------------------------------------------------------_
            //Troca de lado a mensagem que foi enviada

            mConstraintSet.clone(holder.binding.pacoteDaMensagem)
            mConstraintSet.clear(holder.binding.horaDisplay.id, ConstraintSet.START)
            mConstraintSet.connect(holder.binding.horaDisplay.id, ConstraintSet.END, ConstraintSet.END,0)
            mConstraintSet.clear(holder.binding.mensagem.id, ConstraintSet.START)
            mConstraintSet.connect(holder.binding.mensagem.id, ConstraintSet.END, ConstraintSet.END,0)
            mConstraintSet.applyTo(holder.binding.pacoteDaMensagem)

            //--------------------------------------------------------------------------------------_
            holder.bind(item)
        }else{
            holder.bind(item)
        }


    }

}
//Colocar no adapter um IF antes de setar no view as configs, assim observando o SEND ele troca os layouts