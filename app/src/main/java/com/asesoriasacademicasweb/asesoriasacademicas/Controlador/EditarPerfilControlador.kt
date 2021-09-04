package com.asesoriasacademicasweb.asesoriasacademicas.Controlador

import android.content.Context
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Modelo
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Persona
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IEditarPerfilVista

class EditarPerfilControlador(val iEditarPerfilVista: IEditarPerfilVista) : IEditarPerfilControlador {
    override fun onEditProfile(context: Context, nombre: String, email: String, telefono: String, direccion: String, contrasenia: String,  repetContrasenia: String): Int {
        val persona = Persona(
                nombre,
                email,
                telefono,
                direccion,
                contrasenia,
                "Estudiante"
        )

        when (persona.editarPerfil(context, repetContrasenia)) {
            0 -> this.iEditarPerfilVista.onLoginError("El campo nombre no puede estar vacío")
            1 -> this.iEditarPerfilVista.onLoginError("El campo email no puede estar vacío")
            2 -> this.iEditarPerfilVista.onLoginError("El campo email no es válido")
            3 -> this.iEditarPerfilVista.onLoginError("El campo telefono no puede estar vacío")
            4 -> this.iEditarPerfilVista.onLoginError("El campo direccion no puede estar vacío")
            5 -> this.iEditarPerfilVista.onLoginError("El campo contraseña no puede estar vacío")
            6 -> this.iEditarPerfilVista.onLoginError("El campo contraseña no es válido. Debe contener entre 8 y 15 caracteres, utiliza al menos 1 letra Mayúscula, 1 letra minúscula y un número")
            7 -> this.iEditarPerfilVista.onLoginError("La contraseña no coincide, verifica e intenta nuevamente")
            8 -> this.iEditarPerfilVista.onLoginError("El campo nombre no es válido")
            9 -> this.iEditarPerfilVista.onLoginError("El campo telefono no es válido")
            -1 -> this.iEditarPerfilVista.onLoginSuccess("Edición del perfil satisfactoria")
        }
        return persona.editarPerfil(context, repetContrasenia)
    }

    override fun getUser(context: Context, email: String): Persona {
        val obj = Modelo()
        return obj.obtenerPersona(context, email)
    }

    override fun updateProfile(context: Context, persona: Persona): Int {
        val obj = Modelo()
        return obj.actualizarPersona(context, persona)
    }
}