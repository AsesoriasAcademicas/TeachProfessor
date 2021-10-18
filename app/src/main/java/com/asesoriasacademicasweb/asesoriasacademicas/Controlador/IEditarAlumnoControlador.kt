package com.asesoriasacademicasweb.asesoriasacademicas.Controlador

import android.content.Context

interface IEditarAlumnoControlador {
    fun onEditAlumno(context: Context, nombre: String, email: String, telefono: String, direccion: String): Int
}