package com.asesoriasacademicasweb.asesoriasacademicas

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Persona
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IInicioVista
import org.json.JSONException


@Suppress("DEPRECATION")
class InicioActivity: AppCompatActivity(), IInicioVista {

    var request: RequestQueue? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        request = Volley.newRequestQueue(this)

        val stringEmail= getIntent().getStringExtra("email")

        var cardViewUser = findViewById<CardView>(R.id.cardUser)
        var cardViewHorario = findViewById<CardView>(R.id.cardHorario)
        var cardViewAlumnos = findViewById<CardView>(R.id.cardAlumnos)
        var persona = Persona()
        val nombre = findViewById<TextView>(R.id.txv_nombre_inicio)

        var builder = AlertDialog.Builder(this)
        val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()

        if (isNetworkConnected(this)) {
            var url = "https://webserviceasesoriasacademicas.000webhostapp.com/cargar_perfil.php?email=$stringEmail"
            url = url.replace(" ","%20")
            val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET,url,null,
                    Response.Listener { response ->
                        try {
                            val jsonArray = response.optJSONArray("user")
                            val jsonObjet = jsonArray.getJSONObject(0)
                            persona.nombre = jsonObjet.getString("nombre")
                            nombre?.setText(persona.nombre)
                            alertDialog?.dismiss()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(this, "\n" + "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss()
                    })
            request?.add(jsonObjectRequest)
        } else{
            Toast.makeText(this, "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_LONG).show()
            alertDialog.dismiss()
        }

        cardViewUser.setOnClickListener{
            val intentInsert = Intent(this, EditarPerfilActivity::class.java)
            intentInsert.putExtra("email", stringEmail);
            startActivity(intentInsert)
        }

        cardViewHorario.setOnClickListener{
            val intentInsert = Intent(this, FechaClaseActivity::class.java)
            intentInsert.putExtra("email", stringEmail);
            startActivity(intentInsert)
        }

        cardViewAlumnos.setOnClickListener{
            val intentInsert = Intent(this, GestionarAlumnosClase::class.java)
            intentInsert.putExtra("email", stringEmail);
            startActivity(intentInsert)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_popup, menu)
        return true
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Salir de la aplicación")
        builder.setMessage("¿Seguro que deseas salir de Teach?")
                .setCancelable(false)
                .setPositiveButton("Confirmar") { dialog, id ->
                    val intentLogout = Intent(this, LoginActivity::class.java)
                    startActivity(intentLogout)
                }
                .setNegativeButton("Cancelar") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intentEditarPerfil = Intent(this, EditarPerfilActivity::class.java)
        if (item.itemId == R.id.editar_perfil){
            var email= getIntent().getStringExtra("email")
            intentEditarPerfil.putExtra("email", email);
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

    override fun onLoginSuccess(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onLoginError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivityManager.activeNetworkInfo
        return !(info == null || !info.isConnected || !info.isAvailable)
    }
}