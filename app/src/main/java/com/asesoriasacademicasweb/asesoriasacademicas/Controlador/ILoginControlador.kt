package com.asesoriasacademicasweb.asesoriasacademicas.Controlador

import android.content.Context


interface ILoginControlador {
    fun onLogin(context: Context, email: String, contrasenia: String): Int
}