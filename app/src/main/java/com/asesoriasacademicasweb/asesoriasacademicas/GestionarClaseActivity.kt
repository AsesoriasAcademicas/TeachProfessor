package com.asesoriasacademicasweb.asesoriasacademicas

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.asesoriasacademicasweb.asesoriasacademicas.Controlador.GestionarClaseControlador
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Clase
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Profesor
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IGestionarClaseVista
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class GestionarClaseActivity : AppCompatActivity(), IGestionarClaseVista {

    val iGestionarClaseControlador = GestionarClaseControlador(this)

    var request: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestionar_clase)

        request = Volley.newRequestQueue(this)

        var profesor = Profesor()
        val stringEmail= getIntent().getStringExtra("email")
        val stringFecha= getIntent().getStringExtra("fecha")
        val tituloFecha: TextView? = findViewById<TextView>(R.id.txt_fecha_gestionar_clase)

        tituloFecha?.setText(stringFecha)
        var builder = AlertDialog.Builder(this)
        val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()

        if (isNetworkConnected(this)) {
            var url = "https://webserviceasesoriasacademicas.000webhostapp.com/obtener_profesor.php?email=$stringEmail"
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
                    try {
                        val jsonArray = response.optJSONArray("user")
                        val jsonObjet = jsonArray.getJSONObject(0)
                        profesor.id = jsonObjet.getInt("id_profesor")
                        val clases: ArrayList<Clase> = iGestionarClaseControlador.getClass(this, profesor.id.toString())

                        if(clases.size > 0){
                            val listView: ListView? = findViewById(R.id.listView_class)
                            val adaptador: ArrayAdapter<Clase> = ArrayAdapter(this, R.layout.activity_listview, R.id.label, clases)

                            if (adaptador.count != 0) {
                                listView?.setAdapter(adaptador)
                                alertDialog?.dismiss()
                                listView?.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
                                    val intentPopupDetalleClass = Intent(this, PopupDetalleClaseActivity::class.java)
                                    intentPopupDetalleClass.putExtra("id_clase", clases[position].id.toString());
                                    val email = getIntent().getStringExtra("email")
                                    intentPopupDetalleClass.putExtra("email", email);
                                    startActivity(intentPopupDetalleClass)
                                })
                            } else {
                                val mensajeClasesVacio: ArrayList<String> = ArrayList()
                                mensajeClasesVacio.add("No tiene clases")
                                val adaptadorEmpty: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.activity_listview, R.id.label_empty, mensajeClasesVacio)
                                listView?.setAdapter(adaptadorEmpty)
                                alertDialog.dismiss()
                            }
                        } else {
                            var url = "https://webserviceasesoriasacademicas.000webhostapp.com/listar_clases_profesor.php?email=$stringEmail"
                            url = url.replace(" ", "%20")
                            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                                Response.Listener { response ->
                                    try {
                                        val clases: ArrayList<Clase> = ArrayList<Clase>()
                                        var jsonObjet: JSONObject
                                        val jsonArray = response.optJSONArray("listaClases")
                                        for (i in 0 until jsonArray.length()) {
                                            var clase = Clase()
                                            jsonObjet = jsonArray.getJSONObject(i)
                                            clase.id = jsonObjet.getInt("id_clase")
                                            clase.fecha = jsonObjet.getString("fecha")
                                            clase.hora = jsonObjet.getString("hora")
                                            clase.duracion = jsonObjet.getString("duracion")
                                            clase.materia = jsonObjet.getString("materia")
                                            clase.tema = jsonObjet.getString("tema")
                                            clase.inquietudes = jsonObjet.getString("inquietudes")
                                            clase.estado = jsonObjet.getString("estado")
                                            if(clase.fecha.equals(stringFecha)) {
                                                clases.add(clase)
                                            }
                                        }
                                        val listView: ListView? = findViewById(R.id.listView_class)
                                        val adaptador: ArrayAdapter<Clase> =
                                            ArrayAdapter(this, R.layout.activity_listview, R.id.label, clases)

                                        if (adaptador.count != 0) {
                                            listView?.setAdapter(adaptador)
                                            alertDialog.dismiss()
                                            listView?.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
                                                val intentPopupDetalleClass = Intent(this, PopupDetalleClaseActivity::class.java)
                                                intentPopupDetalleClass.putExtra(
                                                    "id_clase",
                                                    clases[position].id.toString()
                                                );
                                                val email = getIntent().getStringExtra("email")
                                                intentPopupDetalleClass.putExtra("email", email);
                                                intentPopupDetalleClass.putExtra("fecha", stringFecha);
                                                startActivity(intentPopupDetalleClass)
                                            })
                                        } else {
                                            val mensajeClasesVacio: ArrayList<String> = ArrayList()
                                            mensajeClasesVacio.add("No tiene clases")
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
                                        mensajeClasesVacio.add("No tiene clases")
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
                                        "\n" + "Ocurrió un error al cargar las clases!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    alertDialog.dismiss()
                                })
                            request?.add(jsonObjectRequest)
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

        } else{
            Toast.makeText(this, "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_LONG).show()
            alertDialog.dismiss()
        }

        val btnAgregarClase = findViewById<Button>(R.id.btn_agregar_gestionar_clase)
        btnAgregarClase.setOnClickListener{
            val intentClass = Intent(this, HoraClaseAcivity::class.java)
            val email= getIntent().getStringExtra("email")
            val fecha= getIntent().getStringExtra("fecha")
            intentClass.putExtra("email", email)
            intentClass.putExtra("fecha", fecha);
            startActivity(intentClass)
        }

        val btnCancelarGestionarClase = findViewById<Button>(R.id.btn_cancelar_gestionar_clase)
        btnCancelarGestionarClase.setOnClickListener {
            val intentInicio = Intent(this, InicioActivity::class.java)
            val email= getIntent().getStringExtra("email")
            intentInicio.putExtra("email", email);
            intentInicio.putExtra("fecha",stringFecha)
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

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivityManager.activeNetworkInfo
        return !(info == null || !info.isConnected || !info.isAvailable)
    }
}