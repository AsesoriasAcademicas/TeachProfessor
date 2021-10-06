package com.asesoriasacademicasweb.asesoriasacademicas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Clase
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Estudiante
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IGestionarAlumnosVista
import org.json.JSONException
import org.json.JSONObject

class GestionarAlumnosClase: AppCompatActivity(), IGestionarAlumnosVista {


    var request: RequestQueue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestionar_alumnos)

        request = Volley.newRequestQueue(this)
        var builder = AlertDialog.Builder(this)
        val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()

        var url = "https://webserviceasesoriasacademicas.000webhostapp.com/listar_alumnos.php"
        url = url.replace(" ", "%20")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val estudiantes: ArrayList<Estudiante> = ArrayList<Estudiante>()
                    var jsonObjet: JSONObject
                    val jsonArray = response.optJSONArray("listaAlumnos")
                    for (i in 0 until jsonArray.length()) {
                        var estudiante = Estudiante()
                        jsonObjet = jsonArray.getJSONObject(i)
                        estudiante.id = jsonObjet.getInt("id_estudiante")
                        estudiante.nombre = jsonObjet.getString("nombre")
                        estudiante.email = jsonObjet.getString("email")
                        estudiante.telefono = jsonObjet.getString("telefono")
                        estudiante.direccion = jsonObjet.getString("direccion")
                        estudiante.contrasenia = jsonObjet.getString("password")
                        estudiantes.add(estudiante)
                    }
                    val listView: ListView? = findViewById(R.id.listView_class)
                    val adaptador: ArrayAdapter<Estudiante> =
                        ArrayAdapter(this, R.layout.activity_listview, R.id.label, estudiantes)

                    if (adaptador.count != 0) {
                        listView?.setAdapter(adaptador)
                        alertDialog.dismiss()
                        listView?.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                            val intentEditarAlumno =
                                Intent(this, EditarAlumnoActivity::class.java)
                            println(estudiantes[position].email)
                            intentEditarAlumno.putExtra(
                                "email_estudiante",
                                estudiantes[position].email
                            )
                            val email = getIntent().getStringExtra("email")
                            intentEditarAlumno.putExtra("email", email);
                            startActivity(intentEditarAlumno)
                        })
                    } else {
                        val mensajeClasesVacio: ArrayList<String> = ArrayList()
                        mensajeClasesVacio.add("No hay estudiantes registrados")
                        val adaptadorEmpty: ArrayAdapter<String> = ArrayAdapter<String>(
                            this,
                            R.layout.activity_listview,
                            R.id.label_empty,
                            mensajeClasesVacio
                        )
                        listView?.setAdapter(adaptadorEmpty)
                        alertDialog.dismiss()
                    }
                } catch (e: JSONException) {
                    val listViewEmpty: ListView? = findViewById(R.id.listView_class)
                    val mensajeClasesVacio: ArrayList<String> = ArrayList()
                    mensajeClasesVacio.add("No hay estudiantes registrados")
                    val adaptadorEmpty: ArrayAdapter<String> = ArrayAdapter<String>(
                        this,
                        R.layout.activity_listview,
                        R.id.label_empty,
                        mensajeClasesVacio
                    )
                    listViewEmpty?.setAdapter(adaptadorEmpty)
                    alertDialog.dismiss()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    this,
                    "\n" + "Ocurrió un error al cargar los estudiantes!",
                    Toast.LENGTH_SHORT
                ).show()
                alertDialog.dismiss()
            })
        request?.add(jsonObjectRequest)

        val btnAgregarAlumno = findViewById<Button>(R.id.btn_agregar_gestionar_alumno)
        btnAgregarAlumno.setOnClickListener{
            val intentClass = Intent(this, IngresarAlumnoActivity::class.java)
            val email= getIntent().getStringExtra("email")
            intentClass.putExtra("email", email)
            startActivity(intentClass)
        }

        val btnCancelarGestionarAlumno = findViewById<Button>(R.id.btn_cancelar_gestionar_alumno)
        btnCancelarGestionarAlumno.setOnClickListener {
            val intentInicio = Intent(this, InicioActivity::class.java)
            val email= getIntent().getStringExtra("email")
            intentInicio.putExtra("email", email);
            startActivity(intentInicio)
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
            intentEditarPerfil.putExtra("email", email);
            startActivity(intentEditarPerfil)
        }

        val intentLogout = Intent(this, LoginActivity::class.java)
        if (item.itemId == R.id.logout){
            val email= getIntent().getStringExtra("email")
            intentLogout.putExtra("email", email);
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