package com.asesoriasacademicasweb.asesoriasacademicas.Controlador

import android.content.Context

interface IIngresarAlumnoControlador {
    fun onRegistry(context: Context, nombre: String, email: String, telefono: String, direccion: String): Int
}