package com.asesoriasacademicasweb.asesoriasacademicas.Controlador

import android.content.Context
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Persona

interface IRegistrarseControlador {
    fun onRegistry(context: Context, nombre: String, email: String, contrasenia: String, repetContrasenia: String, booleanCheck: Boolean): Int
    fun insertUser(context: Context, persona: Persona, idProfesor: Int): Int
    fun insertUserEst(context: Context, persona: Persona): Int
}