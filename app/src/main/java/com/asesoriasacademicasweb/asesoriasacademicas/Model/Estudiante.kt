package com.asesoriasacademicasweb.asesoriasacademicas.Model

import android.content.Context
import java.util.regex.Pattern

class Estudiante(_id: Int = 0, _nombre: String = "", _email: String = "", _telefono: String = "", _direccion: String = "", _contrasenia: String= "", _tipoPersona: String = "Estudiante", _clases : ArrayList<Clase>): IEstudiante, IPersona {
    override var nombre: String = _nombre
        get() = field
        set(value) {
            field = value
        }
    override var email: String = _email
        get() = field
        set(value) {
            field = value
        }
    override var telefono: String = _telefono
        get() = field
        set(value) {
            field = value
        }
    override var direccion: String = _direccion
        get() = field
        set(value) {
            field = value
        }
    override var contrasenia: String = _contrasenia
        get() = field
        set(value) {
            field = value
        }
    override var tipoPersona: String = _tipoPersona
        get() = field
        set(value) {
            field = value
        }
    override var id: Int = _id
        get() = field
        set(value) {
            field = value
        }
    override var clases: ArrayList<Clase> = arrayListOf()
        get() = field
        set(value) {
            field = value
        }

    init {
        this.id = _id
        this.nombre = _nombre
        this.email = _email
        this.telefono = _telefono
        this.direccion = _direccion
        this.contrasenia = _contrasenia
        this.tipoPersona = _tipoPersona
        this.clases = _clases
    }

    constructor(_email: String, _contrasenia: String): this(0, "", _email, "", "", _contrasenia, "Estudiante", ArrayList()){
        this.email = _email
        this.contrasenia = _contrasenia
    }

    constructor(_nombre: String, _email: String, _contrasenia: String): this(0, _nombre, _email, "", "", _contrasenia,"Estudiante", ArrayList()){
        this.nombre = _nombre
        this.email = _email
        this.contrasenia = _contrasenia
    }

    constructor(_nombre: String, _email: String, _telefono: String, _direccion: String): this(0, _nombre, _email, _telefono, "", _direccion, "Estudiante", ArrayList()){
        this.nombre = _nombre
        this.email = _email
        this.telefono = _telefono
        this.direccion = _direccion
    }

    constructor(): this(0, "", "", "", "", "", "", ArrayList()){
        this.id = 0
        this.nombre = ""
        this.email = ""
        this.direccion = ""
        this.telefono = ""
        this.contrasenia = ""
        this.tipoPersona = ""
        this.clases = ArrayList()
    }

    override fun toString(): String{
        return nombre + "\n" + direccion + "\n" + telefono
    }

    override fun esValido(context: Context): Int {
        val obj = Modelo()
        return if (email.trim().isEmpty()){
            0
        } else if (!Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(email.trim()).matches()){
            1
        } else if (contrasenia.trim().isEmpty()){
            2
        } else if (!Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S{8,15}$").matcher(contrasenia).matches()) {
            3
        } else if (obj.buscarPersona(context, email) == 0) {
            4
        } else if (obj.validarSesion(context, email, contrasenia) == 0) {
            5
        } else {
            -1
        }
    }

    override fun registroValido(context: Context, repetContrasenia: String, booleanCheck: Boolean): Int {
        val obj = Modelo()
        return if (nombre.trim().isEmpty()) {
            0
        } else if (!Pattern.compile("^([a-zA-ZñÑáéíóúÁÉÍÓÚ\\s])*\$").matcher(nombre.trim()).matches()){
            7
        } else if (email.trim().isEmpty()) {
            1
        } else if (!Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(email.trim()).matches()){
            2
        } else if (contrasenia.trim().isEmpty()){
            3
        } else if (!Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S{8,15}$").matcher(contrasenia).matches()) {
            4
        } else if (obj.buscarPersona(context, email) == 1) {
            5
        } else if (!contrasenia.equals(repetContrasenia)) {
            6
        } else if (!booleanCheck) {
            8
        } else{
            -1
        }
    }

    override fun ingresoValido(context: Context): Int {
        val obj = Modelo()
        return if (nombre.trim().isEmpty()) {
            0
        } else if (!Pattern.compile("^([a-zA-ZñÑáéíóúÁÉÍÓÚ\\s])*\$").matcher(nombre.trim()).matches()) {
            8
        } else if(email.trim().isEmpty()) {
            1
        } else if (!Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(email.trim()).matches()){
            2
        } else if (telefono.trim().isEmpty()) {
            3
        } else if (!Pattern.compile("^(8+[0-9]{6}|3+[0-9]{9})\$").matcher(telefono.trim()).matches()){
            9
        } else if (direccion.trim().isEmpty()) {
            4
        } else if (obj.buscarPersona(context, email) == 1) {
            5
        } else{
            -1
        }
    }

    override fun editarPerfil(context: Context, repetContrasenia: String): Int {
        return if (nombre.trim().isEmpty()) {
            0
        } else if (!Pattern.compile("^([a-zA-ZñÑáéíóúÁÉÍÓÚ\\s])*\$").matcher(nombre.trim()).matches()){
            8
        } else if (email.trim().isEmpty()) {
            1
        } else if (!Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(email.trim()).matches()){
            2
        } else if (telefono.trim().isEmpty()) {
            3
        } else if (!Pattern.compile("^(8+[0-9]{6}|3+[0-9]{9})\$").matcher(telefono.trim()).matches()){
            9
        } else if (direccion.trim().isEmpty()) {
            4
        } else if (contrasenia.trim().isEmpty()){
            5
        } else if (!Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S{8,15}$").matcher(contrasenia).matches()) {
            6
        } else if (!contrasenia.equals(repetContrasenia)) {
            7
        } else{
            -1
        }
    }
}