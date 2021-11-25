package com.malfaa.firebasechat

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun converteLongParaString(horaSistema: Date): String {
    return SimpleDateFormat("h:mm a")//"h:mm:ss a '||' dd.MM.yyyy"
        .format(horaSistema).toString()
}

@SuppressLint("SimpleDateFormat")
fun usarNoDatabase(horaSistema: Date): String {
    return SimpleDateFormat("h:mm:ss a '||' dd.MM.yyyy")//"h:mm:ss a '||' dd.MM.yyyy"
        .format(horaSistema).toString()
}

// se tal horario é igual a tal, mostrar dia junto com a hora, caso contrário só a hora
//"EEEE DD-mmm-yyyy' Time: 'HH:mm"

@BindingAdapter("setNome")
fun TextView.setNome(item: ContatosEntidade){
    text = item.nome
}

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}