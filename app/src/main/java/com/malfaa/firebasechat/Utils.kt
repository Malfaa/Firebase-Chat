package com.malfaa.firebasechat

import android.annotation.SuppressLint
import java.text.SimpleDateFormat


@SuppressLint("SimpleDateFormat")
fun converteLongParaString(horaSistema: Long): String {
    return SimpleDateFormat("Time: 'HH:mm")
        .format(horaSistema).toString()
}
//"EEEE DD-mmm-yyyy' Time: 'HH:mm"

