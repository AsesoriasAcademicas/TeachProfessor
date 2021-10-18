package com.asesoriasacademicasweb.asesoriasacademicas.Controlador

import android.content.Context
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Estudiante
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Persona
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IEditarAlumnoVista

class EditarAlumnoControlador(val iEditarAlumnoVista: IEditarAlumnoVista) : IEditarAlumnoControlador {
    override fun onEditAlumno(context: Context, nombre: String, email: String, telefono: String, direccion: String): Int {
        val estudiante = Estudiante(
                nombre,
                email,
                telefono,
                direccion
        )

        when (estudiante.ingresoValido(context)) {
            0 -> this.iEditarAlumnoVista.onLoginError("El campo nombre no puede estar vacío")
            8 -> this.iEditarAlumnoVista.onLoginError("El campo nombre no es válido")
            3 -> this.iEditarAlumnoVista.onLoginError("El campo telefono no puede estar vacío")
            9 -> this.iEditarAlumnoVista.onLoginError("El campo telefono no es válido")
            4 -> this.iEditarAlumnoVista.onLoginError("El campo direccion no puede estar vacío")
            1 -> this.iEditarAlumnoVista.onLoginError("El campo email no puede estar vacío")
            2 -> this.iEditarAlumnoVista.onLoginError("El campo email no es válido")
            5 -> this.iEditarAlumnoVista.onLoginError("Ya existe un usuario con el email ingresado")
            -1 -> this.iEditarAlumnoVista.onLoginSuccess("Registro satisfactorio")
        }
        return estudiante.ingresoValido(context)
    }
}