package com.malfaa.firebasechat

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import java.text.SimpleDateFormat


@SuppressLint("SimpleDateFormat")
fun dataFormato(horaSistema: Long): String {
    return try {
        val formato = SimpleDateFormat("HH:mm'\n'dd.MM")
        formato.format(horaSistema)
    }catch (e: Exception) {
        e.toString()
    }
}

@BindingAdapter("setNome")
fun TextView.setNome(item: ContatosEntidade){
    text = item.nome
}

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}

//fun checaInternet():Boolean{
//    val gerenciadorConectividade = ContatosFragment().context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        val network = gerenciadorConectividade.activeNetwork ?: return false
//        val conexaoAtiva = gerenciadorConectividade.getNetworkCapabilities(network) ?: return false
//
//        return when {
//            conexaoAtiva.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//            conexaoAtiva.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//            else -> false
//        }
//    } else {
//        @Suppress("DEPRECATION")
//        val networkInfo = gerenciadorConectividade.activeNetworkInfo ?: return false
//        @Suppress("DEPRECATION")
//        return networkInfo.isConnected
//    }
//}