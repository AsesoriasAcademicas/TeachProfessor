package com.asesoriasacademicasweb.asesoriasacademicas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Persona
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IEditarAlumnoVista
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException

class EditarAlumnoActivity: AppCompatActivity(), IEditarAlumnoVista {

    var request: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_alumno)

        request = Volley.newRequestQueue(this)

        val nombre = findViewById<TextInputEditText>(R.id.txt_nombre_editar_alumno)
        nombre.requestFocus()
        val telefono = findViewById<TextInputEditText>(R.id.txt_telefono_editar_alumno)
        val direccion = findViewById<TextInputEditText>(R.id.txt_direccion_editar_alumno)
        val email = findViewById<TextInputEditText>(R.id.txt_email_editar_alumno)

        var persona = Persona()
        val emailBuscado= getIntent().getStringExtra("email")
        val emailEstudiante= getIntent().getStringExtra("email_estudiante")

        var url = "https://webserviceasesoriasacademicas.000webhostapp.com/cargar_perfil.php?email=$emailEstudiante"
        println(url)
        url = url.replace(" ","%20")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,url,null,
            Response.Listener { response ->
                try {
                    val jsonArray = response.optJSONArray("user")
                    val jsonObjet = jsonArray.getJSONObject(0)
                    persona.nombre = jsonObjet.getString("nombre")
                    persona.telefono = jsonObjet.getString("telefono")
                    persona.email = jsonObjet.getString("email")
                    persona.direccion = jsonObjet.getString("direccion")
                    nombre?.setText(persona.nombre)
                    telefono?.setText(persona.telefono)
                    direccion?.setText(persona.direccion)
                    email?.setText(persona.email)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "\n" + "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_SHORT).show();
            })
        request?.add(jsonObjectRequest)

        val btnCancelarEditarAlumno = findViewById<Button>(R.id.btn_cancelar_editar_alumno)
        btnCancelarEditarAlumno.setOnClickListener {
            val intentInicio = Intent(this, GestionarAlumnosClase::class.java)
            val email= getIntent().getStringExtra("email")
            intentInicio.putExtra("email", email);
            var builder = AlertDialog.Builder(this)
            val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            val alertDialog = builder.create()
            alertDialog.show()
            startActivity(intentInicio)
        }
    }

    override fun onResume() {
        super.onResume()
        var builder = AlertDialog.Builder(this)
        val alertDialog = builder.create()
        alertDialog.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_popup, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intentEditarPerfil = Intent(this, EditarPerfilActivity::class.java)
        if (item.itemId == R.id.editar_perfil){
            val email= getIntent().getStringExtra("email")
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

    override fun onLoginSuccess(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onLoginError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}