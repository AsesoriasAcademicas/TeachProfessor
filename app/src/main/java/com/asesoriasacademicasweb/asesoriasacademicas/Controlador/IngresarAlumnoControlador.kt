package com.asesoriasacademicasweb.asesoriasacademicas.Controlador

import android.content.Context
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Estudiante
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IIngresarAlumnoVista

class IngresarAlumnoControlador(var iIngresarAlumnoVista: IIngresarAlumnoVista) : IIngresarAlumnoControlador {
    override fun onRegistry(context: Context, nombre: String, email: String, telefono: String, direccion: String): Int{
        val estudiante = Estudiante(
                nombre,
                email,
                telefono,
                direccion
        )

        when(estudiante.ingresoValido(context)){
            0 -> this.iIngresarAlumnoVista.onLoginError("El campo nombre no puede estar vacío")
            1 -> this.iIngresarAlumnoVista.onLoginError("El campo email no puede estar vacío")
            2 -> this.iIngresarAlumnoVista.onLoginError("El campo email no es válido")
            3 -> this.iIngresarAlumnoVista.onLoginError("El campo telefono no puede estar vacío")
            4 -> this.iIngresarAlumnoVista.onLoginError("El campo direccion no puede estar vacío")
            5 -> this.iIngresarAlumnoVista.onLoginError("Ya existe un usuario con el email ingresado")
            8 -> this.iIngresarAlumnoVista.onLoginError("El campo nombre no es válido")
            9 -> this.iIngresarAlumnoVista.onLoginError("El campo telefono no es válido")
            -1 -> this.iIngresarAlumnoVista.onLoginSuccess("El ingreso del alumno se realizó satisfactoriamente")
        }
        return estudiante.ingresoValido(context)
    }
}