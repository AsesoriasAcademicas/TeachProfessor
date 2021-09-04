package com.asesoriasacademicasweb.asesoriasacademicas.Controlador

import android.content.Context
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Clase
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Profesor

interface ISolicitarClaseControlador {
    fun onNewClass(context: Context, fecha: String, hora: String, duracion: String, materia: String, tema: String, inquietudes: String, estado: String, _idEstudiante: Int, _idProfesor: Int): Int
    fun getTeacher(context: Context, email: String): Profesor
    fun insertClass(context: Context, clase: Clase, idClase: Int): Int
}