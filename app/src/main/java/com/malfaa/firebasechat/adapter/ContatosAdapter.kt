package com.malfaa.firebasechat.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.malfaa.firebasechat.databinding.ContatoItemBinding
import com.malfaa.firebasechat.room.entidades.ContatosEntidade

class ContatosAdapter: ListAdapter<ContatosEntidade, ContatosAdapter.ViewHolder>(MenuConversaDiffCallBack()){
    companion object {
        val usuarioDestino = MutableLiveData<Boolean>()
        lateinit var idItem : ContatosEntidade
        val deletarUsuario = MutableLiveData<Boolean>()
        lateinit var mAuth: FirebaseAuth
        lateinit var db: FirebaseDatabase
    }

   /* private fun teste(){
        val teste = db.reference

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue<Post>()
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("Erro", "loadPost:onCancelled", databaseError.toException())
            }
        }
        teste.addValueEventListener(postListener)

    }*/
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


    // TODO: 17/11/2021 quando alguém enviar a mensagem, mostrar no adapter já com o nome da pessoa no display

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
            usuarioDestino.value = true
        }
        holder.binding.contatoItem.setOnLongClickListener {
            idItem = item
            deletarUsuario.value = true
            true
        }
    }
}