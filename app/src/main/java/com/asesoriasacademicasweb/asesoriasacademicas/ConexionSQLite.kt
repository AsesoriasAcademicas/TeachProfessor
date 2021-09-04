package com.asesoriasacademicasweb.asesoriasacademicas

import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.lang.Exception

class ConexionSQLite(context: Context, name: String, factory: Nothing?, version: Int): SQLiteOpenHelper(context,"bdasesoriasacademicas", null, 1) {

    val persona = "CREATE TABLE PERSONA(id_persona INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nombre TEXT, email TEXT, telefono TEXT, direccion TEXT, password TEXT);"
    val estudiante = "CREATE TABLE ESTUDIANTE(id_estudiante INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, id_persona, FOREIGN KEY (id_persona) REFERENCES PERSONA (id_persona) ON DELETE CASCADE);"
    val profesor = "CREATE TABLE PROFESOR(id_profesor INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, id_persona, FOREIGN KEY (id_persona) REFERENCES PERSONA (id_persona) ON DELETE CASCADE);"
    val tutoria = "CREATE TABLE TUTORIA(id_tutoria INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, materia TEXT, tema TEXT, inquietudes TEXT, estado TEXT);"
    val clase = "CREATE TABLE CLASE(id_clase INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, fecha TEXT, hora TEXT, duracion INTEGER, id_tutoria, id_estudiante, id_profesor, FOREIGN KEY (id_tutoria) REFERENCES TUTORIA (id_tutoria) ON DELETE CASCADE, FOREIGN KEY (id_estudiante) REFERENCES ESTUDIANTE (id_estudiante) ON DELETE CASCADE, FOREIGN KEY (id_profesor) REFERENCES PROFESOR (id_profesor) ON DELETE CASCADE);"
    val pago = "CREATE TABLE PAGO(id_pago INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, id_clase INTEGER, valor_ganacia INTEGER, valor_fondo INTEGER, FOREIGN KEY (id_clase) REFERENCES CLASE (id_clase) ON DELETE CASCADE);"
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL(persona)
            db?.execSQL(estudiante)
            db?.execSQL(profesor)
            db?.execSQL(tutoria)
            db?.execSQL(clase)
            db?.execSQL(pago)
        }catch (e: Exception)
        {
            System.out.println(e.message)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}