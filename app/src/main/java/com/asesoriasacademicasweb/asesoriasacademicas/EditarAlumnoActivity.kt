package com.asesoriasacademicasweb.asesoriasacademicas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.asesoriasacademicasweb.asesoriasacademicas.Controlador.EditarAlumnoControlador
import com.asesoriasacademicasweb.asesoriasacademicas.Controlador.IngresarAlumnoControlador
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Persona
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IEditarAlumnoVista
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException

class EditarAlumnoActivity: AppCompatActivity(), IEditarAlumnoVista {

    val iEditarAlumnoControlador = EditarAlumnoControlador(this)
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
        var builder = AlertDialog.Builder(this)
        val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val alertDialog = builder.create()

        if(persona != null){
            var url = "https://webserviceasesoriasacademicas.000webhostapp.com/obtener_persona.php?email=$emailEstudiante"
            url = url.replace(" ","%20")
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null,
                    Response.Listener { response ->
                        try {
                            if (response.getString("success") == "1") {
                                var url = "https://webserviceasesoriasacademicas.000webhostapp.com/cargar_perfil.php?email=$emailEstudiante"
                                url = url.replace(" ","%20")
                                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null,
                                        Response.Listener { response ->
                                            try {
                                                val jsonArray = response.optJSONArray("user")
                                                val jsonObjet = jsonArray.getJSONObject(0)
                                                persona.nombre = jsonObjet.getString("nombre")
                                                persona.telefono = jsonObjet.getString("telefono")
                                                persona.direccion = jsonObjet.getString("direccion")
                                                persona.email = jsonObjet.getString("email")

                                                nombre?.setText(persona.nombre)
                                                telefono?.setText(persona.telefono)
                                                direccion?.setText(persona.direccion)
                                                email?.setText(persona.email)
                                                alertDialog.dismiss()
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
                                Toast.makeText(this, "\n" + "Ocurrio un error al cargar el perfil!", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(this, "\n" + "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss()
                    })
            request?.add(jsonObjectRequest)
        } else {
            nombre?.setText(persona.nombre)
            telefono?.setText(persona.telefono)
            direccion?.setText(persona.direccion)
            email?.setText(persona.email)
            alertDialog.dismiss()
        }

        val btnCancelarEditarAlumno = findViewById<Button>(R.id.btn_cancelar_editar_alumno)
        btnCancelarEditarAlumno.setOnClickListener {
            val intentInicio = Intent(this, GestionarAlumnosClase::class.java)
            val email= getIntent().getStringExtra("email")
            intentInicio.putExtra("email", email)
            startActivity(intentInicio)
        }

        val btnGuardarEditarAlumno = findViewById<Button>(R.id.btn_guardar_editar_alumno)
        btnGuardarEditarAlumno.setOnClickListener{

            val nombre: EditText? = findViewById(R.id.txt_nombre_editar_alumno)
            val telefono: EditText? = findViewById(R.id.txt_telefono_editar_alumno)
            val direccion: EditText? = findViewById(R.id.txt_direccion_editar_alumno)
            val email: EditText? = findViewById(R.id.txt_email_editar_alumno)
            val stringNombre = nombre?.text.toString().trim()
            val stringEmail = email?.text.toString().trim()
            val stringTelefono = telefono?.text.toString().trim()
            val stringDireccion = direccion?.text.toString().trim()

            val intentEditProfile = Intent(this, GestionarAlumnosClase::class.java)

            if(iEditarAlumnoControlador.onEditAlumno(this, stringNombre, stringEmail, stringTelefono, stringDireccion) == -1) {
                    alertDialog.show()
                    var url = "https://webserviceasesoriasacademicas.000webhostapp.com/editar_perfil.php?nombre=$stringNombre&email=$stringEmail" +
                            "&telefono=$stringTelefono&direccion=$stringDireccion"
                    url = url.replace(" ","%20")
                    url = url.replace("#","%23")
                    url = url.replace("-","%2D")
                    url = url.replace("á","%C3%A1")
                    url = url.replace("é","%C3%A9")
                    url = url.replace("í","%C3%AD")
                    url = url.replace("ó","%C3%B3")
                    url = url.replace("ú","%C3%BA")
                    url = url.replace("°","%C2%B0")
                    val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null,
                            Response.Listener { response ->
                                if (response.getString("success") == "1"){
                                    intentEditProfile.putExtra("email", emailBuscado!!)
                                    alertDialog.dismiss()
                                    startActivity(intentEditProfile)
                                } else if(response.getString("error") == "0") {
                                    Toast.makeText(this, "\n" + "Ocurrió un error en la actualización del alumno!", Toast.LENGTH_SHORT).show()
                                    alertDialog.dismiss()
                                }
                            },
                            Response.ErrorListener { error ->
                                Toast.makeText(this, "\n" + "Ocurrió un error en la actualización del alumno!", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss()
                            })
                    request?.add(jsonObjectRequest)
                }
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cancelar edición del alumno")
        builder.setMessage("¿Seguro que deseas cancelar la edición del alumno?")
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