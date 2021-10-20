package com.malfaa.firebasechat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malfaa.firebasechat.databinding.ContatoItemBinding
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import kotlin.properties.Delegates

class ContatosAdapter: ListAdapter<ContatosEntidade, ContatosAdapter.ViewHolder>(MenuConversaDiffCallBack()){
    companion object {
        private val _usuarioDestino = MutableLiveData<Boolean>()
        val usuarioDestino: LiveData<Boolean>
            get() = _usuarioDestino
        lateinit var idItem : ContatosEntidade
    }

    class ViewHolder private constructor(val binding: ContatoItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item : ContatosEntidade){
            binding.item = item
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup):ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ContatoItemBinding.inflate(layoutInflater,parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class MenuConversaDiffCallBack: DiffUtil.ItemCallback<ContatosEntidade>(){
        override fun areItemsTheSame(
            oldItem: ContatosEntidade,
            newItem: ContatosEntidade
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ContatosEntidade,
            newItem: ContatosEntidade
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
        holder.binding.contatoItem.setOnClickListener{
            idItem = item
            _usuarioDestino.value = true
        }
        // PRÓXIMA ETAPA É REALIZAR O CLICK NA TELA DE CONTATOS P/ IR ATÉ A CONVERSA
    }

}
