package com.asesoriasacademicasweb.asesoriasacademicas

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.asesoriasacademicasweb.asesoriasacademicas.Controlador.FechaClaseControlador
import com.asesoriasacademicasweb.asesoriasacademicas.Model.IFechaClaseVista
import android.widget.CalendarView
import java.text.DateFormat
import java.util.*

class FechaClaseActivity: AppCompatActivity(), IFechaClaseVista {

    val iFechaClaseControlador = FechaClaseControlador(this)
    lateinit var calendarView: CalendarView

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fecha_clase)

        val calendar = Calendar.getInstance()
        calendarView = findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(year,month,dayOfMonth)
            val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
            val formattedDate = dateFormatter.format(calendar.time)
            val intentClass = Intent(this, GestionarClaseActivity::class.java)
            val email= getIntent().getStringExtra("email")
            intentClass.putExtra("email", email)
            intentClass.putExtra("fecha", formattedDate)
            startActivity(intentClass)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_popup, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intentEditarPerfil = Intent(this, EditarPerfilActivity::class.java)
        if (item.itemId == R.id.editar_perfil){
            var email= getIntent().getStringExtra("email")
            intentEditarPerfil.putExtra("email", email)
            startActivity(intentEditarPerfil)
        }

        val intentLogout = Intent(this, LoginActivity::class.java)
        if (item.itemId == R.id.logout){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Salir de la aplicación")
            builder.setMessage("¿Seguro que deseas salir de Teach?")
                    .setCancelable(false)
                    .setPositiveButton("Confirmar") { dialog, id ->
                        val email= getIntent().getStringExtra("email")
                        intentLogout.putExtra("email", email);
                        startActivity(intentLogout)
                    }
                    .setNegativeButton("Cancelar") { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }
        return true
    }

    override fun onManagementSuccess(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onManagementError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}