package com.asesoriasacademicasweb.asesoriasacademicas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IInicioVista

class InicioActivity: AppCompatActivity(), IInicioVista {

    var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val stringEmail= getIntent().getStringExtra("email")

        var cardViewHorario = findViewById<CardView>(R.id.cardHorario)
        var cardViewAlumnos = findViewById<CardView>(R.id.cardAlumnos)
        var builder = AlertDialog.Builder(this)
        val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        alertDialog = builder.create()

        cardViewHorario.setOnClickListener{
            alertDialog?.show()
            val intentInsert = Intent(this, GestionarClaseActivity::class.java)
            intentInsert.putExtra("email", stringEmail);
            startActivity(intentInsert)
        }

        cardViewAlumnos.setOnClickListener{
            alertDialog?.show()
            val intentInsert = Intent(this, GestionarAlumnosClase::class.java)
            intentInsert.putExtra("email", stringEmail);
            startActivity(intentInsert)
        }
    }

    override fun onPause() {
        super.onPause()
        alertDialog?.dismiss()
    }

    override fun onResume() {
        super.onResume()
        alertDialog?.dismiss()
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

        val intentLogout = Intent(this, InicioActivity::class.java)
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

    override fun onLoginSuccess(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onLoginError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}