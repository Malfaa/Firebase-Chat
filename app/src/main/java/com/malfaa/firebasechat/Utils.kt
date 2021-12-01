package com.malfaa.firebasechat

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import java.text.SimpleDateFormat
import java.util.*

//@SuppressLint("SimpleDateFormat")
//fun converteStringParaFormat(horaSistema: String): String {
//    return SimpleDateFormat("h:mm a '\n' dd.MM")//"h:mm:ss a '||' dd.MM.yyyy"
//        .forma
//        t(horaSistema)
//}

@SuppressLint("SimpleDateFormat")
fun teste(horaSistema: Long): String {
    try {
        val formato = SimpleDateFormat("HH:mm '\n' dd.MM")
        val data = Date(horaSistema*1000L)
        return formato.format(data).toString()
    }catch (e: Exception) {
        return e.toString()
    }
}

@SuppressLint("SimpleDateFormat")
fun getDateTime(s: String): String? {
    try {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val netDate = Date(s.toLong() * 1000L)
        return sdf.format(netDate)
    } catch (e: Exception) {
        return e.toString()
    }
}

@SuppressLint("SimpleDateFormat")
fun getDateString(time: Long) : String {
    val simpleDateFormat = SimpleDateFormat("HH:mm '\n' dd.MM")
    return simpleDateFormat.format(time * 1000L)
}

fun data(horaSistema: Long): String {
    try {
        return Date(horaSistema*1000L).toString()
    }catch (e: Exception) {
        return e.toString()
    }
}
// FIXME: 26/11/2021 todos errados


// se tal horario é igual a tal, mostrar dia junto com a hora, caso contrário só a hora
//"EEEE DD-mmm-yyyy' Time: 'HH:mm"

@BindingAdapter("setNome")
fun TextView.setNome(item: ContatosEntidade){
    text = item.nome
}

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}