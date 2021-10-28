package com.malfaa.firebasechat

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

@SuppressLint("SimpleDateFormat")
fun converteLongParaString(horaSistema: Date): String {
    return SimpleDateFormat("h:mm a '||' dd.MM.yyyy")
        .format(horaSistema).toString()
}
//"EEEE DD-mmm-yyyy' Time: 'HH:mm"

@BindingAdapter("setHorario")
fun TextView.setHorario(horario: Date){
    text = converteLongParaString(horario)
}

@BindingAdapter("setNome")
fun TextView.setNome(item: ContatosEntidade){
    text = item.nome
}

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}