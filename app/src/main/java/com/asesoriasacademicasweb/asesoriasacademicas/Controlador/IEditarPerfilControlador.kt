package com.asesoriasacademicasweb.asesoriasacademicas.Controlador

import android.content.Context
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Persona

interface IEditarPerfilControlador {
    fun onEditProfile(context: Context, nombre: String, email: String, telefono: String, direccion: String, contrasenia: String, repetContrasenia: String): Int
    fun getUser(context: Context, email: String): Persona
    fun updateProfile(context: Context, persona: Persona): Int
}