package com.asesoriasacademicasweb.asesoriasacademicas

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
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Estudiante
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IGestionarAlumnosVista
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class GestionarAlumnosClase: AppCompatActivity(), IGestionarAlumnosVista {

    var request: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestionar_alumnos)

        request = Volley.newRequestQueue(this)
        val builder = AlertDialog.Builder(this)
        val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()

        if (isNetworkConnected(this)) {

            var url = "https://webserviceasesoriasacademicas.000webhostapp.com/listar_alumnos.php"
            url = url.replace(" ","%20")
            url = url.replace("#","%23")
            url = url.replace("-","%2D")
            url = url.replace("á","%C3%A1")
            url = url.replace("é","%C3%A9")
            url = url.replace("í","%C3%AD")
            url = url.replace("ó","%C3%B3")
            url = url.replace("ú","%C3%BA")
            url = url.replace("°","%C2%B0")
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->
                    try {
                        val estudiantes: ArrayList<Estudiante> = ArrayList<Estudiante>()
                        var jsonObjet: JSONObject
                        val jsonArray = response.optJSONArray("listaAlumnos")
                        for (i in 0 until jsonArray.length()) {
                            val estudiante = Estudiante()
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
                            listView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                                val intentEditarAlumno =
                                        Intent(this, EditarAlumnoActivity::class.java)
                                println(estudiantes[position].email)
                                intentEditarAlumno.putExtra(
                                        "email_estudiante",
                                        estudiantes[position].email
                                )
                                val email = getIntent().getStringExtra("email")
                                intentEditarAlumno.putExtra("email", email)
                                startActivity(intentEditarAlumno)
                            }
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

        } else{
            Toast.makeText(this, "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_LONG).show()
            alertDialog.dismiss()
        }

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
            intentInicio.putExtra("email", email)
            startActivity(intentInicio)
        }
    }

    override fun onBackPressed() {
        val intentInicio = Intent(this, InicioActivity::class.java)
        val email= getIntent().getStringExtra("email")
        intentInicio.putExtra("email", email)
        startActivity(intentInicio)
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
            val email= intent.getStringExtra("email")
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

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivityManager.activeNetworkInfo
        return !(info == null || !info.isConnected || !info.isAvailable)
    }
}