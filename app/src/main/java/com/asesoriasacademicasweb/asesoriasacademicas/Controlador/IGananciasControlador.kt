package com.asesoriasacademicasweb.asesoriasacademicas.Controlador

import android.content.Context
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Clase

interface IGananciasControlador {
    fun getClass(context: Context, idProfesor: String): ArrayList<Clase>
}