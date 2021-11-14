package com.asesoriasacademicasweb.asesoriasacademicas.Controlador

import android.content.Context
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Clase
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Estudiante
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Modelo
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Profesor
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.ISolicitarClaseVista

class SolicitarClaseControlador(var iSolicitarClaseVista: ISolicitarClaseVista) : ISolicitarClaseControlador {
    override fun onNewClass(context: Context, fecha: String, hora: String, duracion: String, materia: String, tema: String, inquietudes: String, estado: String, _idEstudiante: Int, _idProfesor: Int): Int {
        val clase = Clase(
                0,
                fecha,
                hora,
                duracion,
                materia,
                tema,
                inquietudes,
                estado,
                _idEstudiante,
                _idProfesor
        )

        when(clase.esValido(context)){
            0 -> this.iSolicitarClaseVista.onLoginError("El campo materia no puede estar vacío")
            1 -> this.iSolicitarClaseVista.onLoginError("El campo tema no puede estar vacío")
            2 -> this.iSolicitarClaseVista.onLoginError("El campo inquietudes no puede estar vacío")
            9 -> this.iSolicitarClaseVista.onLoginError("El campo estado no puede estar vacío")
            3 -> this.iSolicitarClaseVista.onLoginError("El campo fecha no puede estar vacío")
            4 -> this.iSolicitarClaseVista.onLoginError("El campo fecha es inferior a la fecha actual")
            5 -> this.iSolicitarClaseVista.onLoginError("El campo hora no puede estar vacío")
            6 -> this.iSolicitarClaseVista.onLoginError("El campo hora es inferior a la hora actual")
            7 -> this.iSolicitarClaseVista.onLoginError("El campo duración no puede estar vacío")
            8 -> this.iSolicitarClaseVista.onLoginError("El campo duracion debe ser igual o inferior a 6 horas")
            -1 -> this.iSolicitarClaseVista.onLoginSuccess("El ingreso de la clase se realizó satisfactoriamente")
        }
        return clase.esValido(context)
    }

    override fun getTeacher(context: Context, email: String): Profesor {
        val obj = Modelo()
        return obj.obtenerProfesor(context, email)
    }

    override fun insertClass(context: Context, clase: Clase, idClase: Int): Int {
        val obj = Modelo()
        return obj.insertarClase(context, clase, idClase)
    }


}