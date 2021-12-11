package com.malfaa.firebasechat.room.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "info_pessoal")
data class SignInEntidade(
    @PrimaryKey
    @ColumnInfo(name = "uid")
    val myUid: String,
    @ColumnInfo(name = "numero")
    val myNum: Long,
    @ColumnInfo(name = "nome")
    val myName: String,
    @ColumnInfo(name = "email")
    val myEmail: String
)