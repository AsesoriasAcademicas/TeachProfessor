package com.asesoriasacademicasweb.asesoriasacademicas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
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
import com.asesoriasacademicasweb.asesoriasacademicas.Controlador.IngresarAlumnoControlador
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Persona
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IIngresarAlumnoVista
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException

class IngresarAlumnoActivity: AppCompatActivity(), IIngresarAlumnoVista {

    val iIngresarAlumnoControlador = IngresarAlumnoControlador(this)
    var request: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitar_alumno)

        request = Volley.newRequestQueue(this)

        val emailBuscado = getIntent().getStringExtra("email")
        val btnInsertarEstudiante = findViewById<Button>(R.id.btn_guardar_ingresar_alumno)
        btnInsertarEstudiante.setOnClickListener {

            val nombre = findViewById<TextInputEditText>(R.id.txt_nombre_ingresar_alumno)
            nombre.requestFocus()
            val telefono = findViewById<TextInputEditText>(R.id.txt_telefono_ingresar_alumno)
            val direccion = findViewById<TextInputEditText>(R.id.txt_direccion_ingresar_alumno)
            val email = findViewById<TextInputEditText>(R.id.txt_email_ingresar_alumno)
            val stringNombre = nombre?.text.toString().trim()
            val stringEmail = email?.text.toString().trim()
            val stringTelefono = telefono?.text.toString().trim()
            val stringDireccion = direccion?.text.toString().trim()

            var persona = Persona()
            val intentRegistry = Intent(this, GestionarAlumnosClase::class.java)
            var builder = AlertDialog.Builder(this)
            val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            val alertDialog = builder.create()

            if(iIngresarAlumnoControlador.onRegistry(this, stringNombre, stringEmail, stringTelefono, stringDireccion) == -1) {
                alertDialog.show()
                var url = "https://webserviceasesoriasacademicas.000webhostapp.com/registrar_usuario.php?nombre=$stringNombre&email=$stringEmail" +
                        "&telefono=$stringTelefono&direccion=$stringDireccion&password="
                println(url)
                url = url.replace(" ", "%20")
                url = url.replace("#", "%23")
                url = url.replace("-", "%2D")
                url = url.replace("á", "%C3%A1")
                url = url.replace("é", "%C3%A9")
                url = url.replace("í", "%C3%AD")
                url = url.replace("ó", "%C3%B3")
                url = url.replace("ú", "%C3%BA")
                url = url.replace("°", "%C2%B0")
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                        Response.Listener { response ->
                            try {
                                if (response.getString("success") == "1") {
                                    var url = "https://webserviceasesoriasacademicas.000webhostapp.com/obtener_estudiante.php?email=$emailBuscado"
                                    url = url.replace(" ", "%20")
                                    url = url.replace("#", "%23")
                                    url = url.replace("-", "%2D")
                                    url = url.replace("á", "%C3%A1")
                                    url = url.replace("é", "%C3%A9")
                                    url = url.replace("í", "%C3%AD")
                                    url = url.replace("ó", "%C3%B3")
                                    url = url.replace("ú", "%C3%BA")
                                    url = url.replace("°", "%C2%B0")
                                    val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                                            Response.Listener { response ->
                                                try {
                                                    val jsonArray = response.optJSONArray("user")
                                                    val jsonObjet = jsonArray.getJSONObject(0)
                                                    intentRegistry.putExtra("email", emailBuscado)
                                                    alertDialog.dismiss()
                                                    startActivity(intentRegistry)
                                                } catch (e: JSONException) {
                                                    e.printStackTrace()
                                                }
                                            },
                                            Response.ErrorListener { error ->
                                                Toast.makeText(this, "\n" + "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_SHORT).show();
                                                alertDialog.dismiss()
                                            })
                                    request?.add(jsonObjectRequest)
                                } else if (response.getString("success") == "0") {
                                    Toast.makeText(this, "\n" + "Ocurrió un error en el registro de su información!", Toast.LENGTH_SHORT).show()
                                    alertDialog.dismiss()
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        },
                        Response.ErrorListener { error ->
                            Toast.makeText(this, "\n" + "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                        })
                request?.add(jsonObjectRequest)
            }
        }

        val btnCancelarEditarPerfil = findViewById<Button>(R.id.btn_cancelar_ingresar_alumno)
        btnCancelarEditarPerfil.setOnClickListener{
            val intentMain = Intent(this, GestionarAlumnosClase::class.java)
            intentMain.putExtra("email", emailBuscado)
            startActivity(intentMain)
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cancelar ingreso del alumno")
        builder.setMessage("¿Seguro que deseas cancelar el ingreso del alumno?")
                .setCancelable(false)
                .setPositiveButton("Confirmar") { dialog, id ->
                    val intentLogout = Intent(this, GestionarAlumnosClase::class.java)
                    startActivity(intentLogout)
                }
                .setNegativeButton("Cancelar") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
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

    override fun onLoginSuccess(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onLoginError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}