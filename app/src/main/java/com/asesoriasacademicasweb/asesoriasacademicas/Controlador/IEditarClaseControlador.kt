package com.asesoriasacademicasweb.asesoriasacademicas.Controlador

import android.content.Context
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Clase
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Tutoria

interface IEditarClaseControlador {
    fun onEditClass(context: Context, fecha: String, hora: String, duracion: String, materia: String, tema: String, inquietudes: String, estado: String, _idEstudiante: Int): Int
    fun updateClase(context: Context, tutoria: Tutoria, clase: Clase): Int
}