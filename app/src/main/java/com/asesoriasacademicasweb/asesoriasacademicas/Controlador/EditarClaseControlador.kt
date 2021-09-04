package com.asesoriasacademicasweb.asesoriasacademicas.Controlador

import android.content.Context
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Clase
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Modelo
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Tutoria
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IEditarClaseVista

class EditarClaseControlador(val iEditarClaseVista: IEditarClaseVista): IEditarClaseControlador {
    override fun onEditClass(context: Context, fecha: String, hora: String, duracion: String, materia: String, tema: String, inquietudes: String, estado: String, _idEstudiante: Int): Int{
        val clase = Clase(
                0,
                fecha,
                hora,
                duracion,
                materia,
                tema,
                inquietudes,
                estado,
                _idEstudiante
        )

        when(clase.esValido(context)){
            0 -> this.iEditarClaseVista.onLoginError("El campo materia no puede estar vacío")
            1 -> this.iEditarClaseVista.onLoginError("El campo tema no puede estar vacío")
            2 -> this.iEditarClaseVista.onLoginError("El campo inquietudes no puede estar vacío")
            9 -> this.iEditarClaseVista.onLoginError("El campo estado no puede estar vacío")
            10 -> this.iEditarClaseVista.onLoginError("No es posible editar una clase con estado INACTVO")
            3 -> this.iEditarClaseVista.onLoginError("El campo fecha no puede estar vacío")
            4 -> this.iEditarClaseVista.onLoginError("El campo fecha es inferior a la fecha actual")
            5 -> this.iEditarClaseVista.onLoginError("El campo hora no puede estar vacío")
            6 -> this.iEditarClaseVista.onLoginError("El campo hora es inferior a la hora actual")
            7 -> this.iEditarClaseVista.onLoginError("El campo duración no puede estar vacío")
            8 -> this.iEditarClaseVista.onLoginError("El campo duracion debe ser igual o inferior a 6 horas")
            -1 -> this.iEditarClaseVista.onLoginSuccess("Solicitud satisfactoria")
        }
        return clase.esValido(context)
    }

    override fun updateClase(context: Context, tutoria: Tutoria, clase: Clase): Int {
        val obj = Modelo()
        return obj.actualizarClase(context, tutoria, clase)
    }
}