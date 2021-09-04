package com.asesoriasacademicasweb.asesoriasacademicas.Model

import android.content.Context

class Tutoria(_materia: String, _tema: String, _inquietudes: String, _estado: String) : ITutoria {

    override var materia: String = _materia
        get() = field
        set(value) {
            field = value
        }
    override var tema: String = _tema
        get() =  field
        set(value) {
            field = value
        }
    override var inquietudes: String = _inquietudes
        get() = field
        set(value) {
            field = value
        }
    override var estado: String = _estado
        get() = field
        set(value) {
            field = value
        }

    constructor(): this("","","", ""){
        this.materia = ""
        this.tema = ""
        this.inquietudes = ""
        this.estado = ""
    }

    override fun esValido(context: Context): Int {
        return if (materia.trim().isEmpty()){
            4
        } else if (tema.trim().isEmpty()){
            5
        } else if (inquietudes.trim().isEmpty()){
            6
        } else if (estado.trim().isEmpty()){
            7
        } else {
            -1
        }
    }
}