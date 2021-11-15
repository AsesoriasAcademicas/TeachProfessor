package com.asesoriasacademicasweb.asesoriasacademicas

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.asesoriasacademicasweb.asesoriasacademicas.Controlador.FechaClaseControlador
import com.asesoriasacademicasweb.asesoriasacademicas.Model.IFechaClaseVista
import android.widget.CalendarView
import androidx.annotation.RequiresApi
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
            val dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT)
            val formattedDate = dateFormatter.format(calendar.time)
            val intentClass = Intent(this, GestionarClaseActivity::class.java)
            val email= intent.getStringExtra("email")
            intentClass.putExtra("email", email)
            intentClass.putExtra("fecha", formattedDate)
            startActivity(intentClass)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        updateConection(this)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateConection(context: Context){
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager?.let {
            it.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    /*val builderConection = AlertDialog.Builder(context)
                    val dialogViewConection: View = View.inflate(context, R.layout.activity_conection_in, null)
                    builderConection.setView(dialogViewConection)
                    builderConection.setCancelable(false)
                    val alertDialogConection = builderConection.create()
                    alertDialogConection.show()

                val timer2 = Timer()
                timer2.schedule(object : TimerTask() {
                    override fun run() {
                        alertDialogConection.dismiss()
                        timer2.cancel()
                    }
                }, 5000)*/

                }
                override fun onLost(network: Network) {
                    val builderConection = AlertDialog.Builder(context)
                    val dialogViewConection: View = View.inflate(context, R.layout.activity_conection_out, null)
                    builderConection.setView(dialogViewConection)
                    builderConection.setCancelable(false)
                    val alertDialogConection = builderConection.create()
                    alertDialogConection.show()
                    val timer2 = Timer()
                    timer2.schedule(object : TimerTask() {
                        override fun run() {
                            alertDialogConection.dismiss()
                            timer2.cancel()
                        }
                    }, 5000)
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_popup, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intentEditarPerfil = Intent(this, EditarPerfilActivity::class.java)
        if (item.itemId == R.id.editar_perfil){
            val email= getIntent().getStringExtra("email")
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
                        val email= intent.getStringExtra("email")
                        intentLogout.putExtra("email", email)
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