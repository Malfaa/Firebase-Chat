package com.malfaa.firebasechat.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.malfaa.firebasechat.databinding.ConversaFragmentBindingImpl
import com.malfaa.firebasechat.databinding.MensagemBinding
import com.malfaa.firebasechat.fragment.ContatosFragment.Companion.database
import com.malfaa.firebasechat.fragment.ContatosFragment.Companion.selfUid
import com.malfaa.firebasechat.fragment.ConversaFragment
import com.malfaa.firebasechat.room.entidades.ConversaEntidade


class ConversaAdapter(): ListAdapter<ConversaEntidade, ConversaAdapter.ViewHolder>(ConversaDiffCallBack()) {
    class ViewHolder private constructor(val binding : MensagemBinding): RecyclerView.ViewHolder(binding.root){
        private fun receberMensagensFirebase(user:FirebaseUser){
            val ordersRef = database.reference.child("${selfUid}+${ConversaFragment.companionArguments.uid}")
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val mensagem = dataSnapshot.child("mensagem").value
                    mensagem.toString()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("Data", databaseError.getMessage()) //Don't ignore errors!
                }
            }
            ordersRef.addValueEventListener(valueEventListener)
        }
        val user = FirebaseAuth.getInstance().currentUser
        fun bind(item: ConversaEntidade){
            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.END
                marginEnd = 10
                bottomMargin = 20
            }

            // FIXME: 22/11/2021 arrumar aqui p/ receber valor do firebase
            receberMensagensFirebase(user!!)

            if (item.souEu == selfUid.toString()){
                binding.conteudoDaMensagem.text = item.mensagem
                binding.horaDisplay.text = item.horario
                binding.cardViewDoConteudoMensagem.setCardBackgroundColor(Color.parseColor("#102027"))
                binding.cardViewDoConteudoMensagem.layoutParams = params

            }else{
                binding.conteudoDaMensagem.text = item.mensagem
                binding.horaDisplay.text = item.horario
                binding.cardViewDoConteudoMensagem.setCardBackgroundColor(Color.parseColor("#546e7a"))
            }
// TODO: 17/11/2021 quando receber a mensagem do firebase, armazenar junto ao room. Vice-versa p/ enviar
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
//child("${ConversaFragment.companionArguments.uid}"+${selfUid})
//child("${selfUid}+${ConversaFragment.companionArguments.uid}")

    /*private fun receberMensagensFirebase(user:FirebaseUser){
        val ordersRef = database.reference.child("${selfUid}+${ConversaFragment.companionArguments.uid}")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val mensagem = dataSnapshot.child("mensagem").value
                mensagem.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Data", databaseError.getMessage()) //Don't ignore errors!
            }
        }
        ordersRef.addValueEventListener(valueEventListener)
    }*/

}

// TODO: 10/11/2021 Figma - arrumar o adicionar e o remover (layout)


// TODO: 22/11/2021 SOu eu pode ser o uid, pq se continuar a estrutura que está agora, terá problema para mostrar qual dado é de tal.
// TODO: 22/11/2021 Arrumar o parent e child de mensanges, arranjar algum jeito de linkar os users a uma conversa