package com.malfaa.firebasechat

import android.annotation.SuppressLint
import android.text.Editable
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


@SuppressLint("SimpleDateFormat")
fun converteLongParaString(horaSistema: LocalDateTime): String {
    return SimpleDateFormat("Time: 'HH:mm")
        .format(horaSistema).toString()
}
//"EEEE DD-mmm-yyyy' Time: 'HH:mm"


@BindingAdapter("setHorario")
fun TextView.setHorario(horario: LocalDateTime){
    text = converteLongParaString(horario)
}

@BindingAdapter("setNome")
fun TextView.setNome(nome: String){
    text = nome
}

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}
//https://nezspencer.medium.com/navigation-components-a-fix-for-navigation-action-cannot-be-found-in-the-current-destination-95b63e16152e
