package com.asesoriasacademicasweb.asesoriasacademicas.Controlador

import android.content.Context
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Estudiante
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Persona
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.ILoginVista

class LoginControlador(var iLoginVista: ILoginVista) : ILoginControlador {

    override fun onLogin(context: Context, _email: String, _contrasenia: String): Int {
        val estudiante = Estudiante(
                _email,
                _contrasenia
        )

        when (estudiante.esValido(context)) {
            0 -> this.iLoginVista.onLoginError("El campo email no puede estar vacío")
            1 -> this.iLoginVista.onLoginError("El campo email no es válido")
            2 -> this.iLoginVista.onLoginError("El campo contraseña no puede estar vacío")
            3 -> this.iLoginVista.onLoginError("El campo contraseña no es válido")
            4 -> this.iLoginVista.onLoginError("No existe un usuario con el email ingresado")
            5 -> this.iLoginVista.onLoginError("El campo constraseña es incorrecta")
            -1 -> this.iLoginVista.onLoginError("Login satisfactorio")
        }
        return estudiante.esValido(context)
    }
}