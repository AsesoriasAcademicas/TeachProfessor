package com.asesoriasacademicasweb.asesoriasacademicas.Controlador

import android.content.Context
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Clase
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Modelo
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IGananciasVista

class GanaciasControladorar( iGananciasVista: IGananciasVista) : IGananciasControlador {
    override fun getClass(context: Context, idProfesor: String): ArrayList<Clase> {
        val obj = Modelo()
        return obj.listarClases(context, idProfesor)
    }
}