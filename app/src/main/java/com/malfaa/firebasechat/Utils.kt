package com.malfaa.firebasechat

import android.annotation.SuppressLint
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import java.text.SimpleDateFormat


@SuppressLint("SimpleDateFormat")
fun converteLongParaString(horaSistema: Long): String {
    return SimpleDateFormat("Time: 'HH:mm")
        .format(horaSistema).toString()
}
//"EEEE DD-mmm-yyyy' Time: 'HH:mm"

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}
//https://nezspencer.medium.com/navigation-components-a-fix-for-navigation-action-cannot-be-found-in-the-current-destination-95b63e16152e
