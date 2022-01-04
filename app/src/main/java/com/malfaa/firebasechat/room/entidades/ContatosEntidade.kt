package com.malfaa.firebasechat.room.entidades

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "contatos")
data class ContatosEntidade(
    @PrimaryKey()
    val uid: String,
    var nome: String,
    var email: String,
    var number: Long
): Parcelable {

    constructor():this("","","",0 )
}