package com.asesoriasacademicasweb.asesoriasacademicas.Model

import android.content.Context

interface IPersona {
    var nombre : String
    var email : String
    var telefono : String
    var direccion : String
    var contrasenia : String
    var tipoPersona : String
    fun esValido(context: Context): Int
    fun registroValido(context: Context, repetContrasenia: String, booleanCheck: Boolean): Int
    fun ingresoValido(context: Context): Int
    fun editarPerfil(context: Context, repetContrasenia: String): Int
}