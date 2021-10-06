package com.asesoriasacademicasweb.asesoriasacademicas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.asesoriasacademicasweb.asesoriasacademicas.Controlador.HolaClaseControlador
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IHoraClaseVista
import java.util.*


class HoraClaseAcivity: AppCompatActivity(), IHoraClaseVista {

    val iHoraClaseControlador = HolaClaseControlador(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hora_clase)

        val timePicker: TimePicker = findViewById(R.id.timePicker)

        val btnGuardar = findViewById<Button>(R.id.btn_guardar_hora_clase)
        btnGuardar.setOnClickListener{

            var hour = timePicker.hour
            val minute = timePicker.minute
            var am_pm = ""
                if(hour > 12) {
                    am_pm = "PM"
                    hour = hour - 12;
                }
                else
                {
                    am_pm="AM"
                }

            val hourMin = hour.toString() + ":" + minute + " " + am_pm
            val intentClass = Intent(this, SolicitarClaseActivity::class.java)
            val fecha= getIntent().getStringExtra("fecha")
            val email= getIntent().getStringExtra("email")
            intentClass.putExtra("email", email);
            intentClass.putExtra("fecha", fecha);
            intentClass.putExtra("hora", hourMin);
            startActivity(intentClass)
        }

        /*val listView: ListView? = findViewById(R.id.listHour_class)
        val hoursDay = ArrayList<String>()
        hoursDay.add("06:00 A.M")
        hoursDay.add("07:00 A.M")
        hoursDay.add("08:00 A.M")
        hoursDay.add("09:00 A.M")
        hoursDay.add("10:00 A.M")
        hoursDay.add("11:00 A.M")
        hoursDay.add("12:00 M")
        hoursDay.add("01:00 P.M")
        hoursDay.add("02:00 P.M")
        hoursDay.add("03:00 P.M")
        hoursDay.add("04:00 P.M")
        hoursDay.add("05:00 P.M")
        hoursDay.add("06:00 P.M")
        hoursDay.add("07:00 P.M")
        hoursDay.add("08:00 P.M")
        hoursDay.add("09:00 P.M")
        hoursDay.add("10:00 P.M")
        val adaptador: ArrayAdapter<String> = ArrayAdapter(this, R.layout.activity_listview, R.id.label, hoursDay)
        listView?.setAdapter(adaptador)

        listView?.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            val hour = hoursDay[position]
            val intentClass = Intent(this, SolicitarClaseActivity::class.java)
            val fecha= getIntent().getStringExtra("fecha")
            val email= getIntent().getStringExtra("email")
            intentClass.putExtra("email", email);
            intentClass.putExtra("fecha", fecha);
            intentClass.putExtra("hora", hour.toString());
            startActivity(intentClass)
        })*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_popup, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intentEditarPerfil = Intent(this, EditarPerfilActivity::class.java)
        if (item.itemId == R.id.editar_perfil){
            var email= getIntent().getStringExtra("email")
            intentEditarPerfil.putExtra("email", email);
            var builder = AlertDialog.Builder(this)
            val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            val alertDialog = builder.create()
            alertDialog.show()
            startActivity(intentEditarPerfil)
        }

        val intentLogout = Intent(this, LoginActivity::class.java)
        if (item.itemId == R.id.logout){
            val email= getIntent().getStringExtra("email")
            intentLogout.putExtra("email", email);
            var builder = AlertDialog.Builder(this)
            val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            val alertDialog = builder.create()
            alertDialog.show()
            startActivity(intentLogout)
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