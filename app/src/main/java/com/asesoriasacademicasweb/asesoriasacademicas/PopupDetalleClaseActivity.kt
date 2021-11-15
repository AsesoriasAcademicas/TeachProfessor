package com.asesoriasacademicasweb.asesoriasacademicas

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.asesoriasacademicasweb.asesoriasacademicas.Controlador.GestionarClaseControlador
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Clase
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IGestionarClaseVista
import org.json.JSONException
import java.util.*

@Suppress("DEPRECATION")
class PopupDetalleClaseActivity : AppCompatActivity(), IGestionarClaseVista {

    val iGestionarClaseControlador = GestionarClaseControlador(this)

    var request: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_detalle_class)

        request = Volley.newRequestQueue(this)

        val intentListadoClases = Intent(this, GestionarClaseActivity::class.java)
        val idClase= intent.getStringExtra("id_clase")
        val stringFecha= intent.getStringExtra("fecha")
        var clase = Clase()
        val materia: TextView? = findViewById<TextView>(R.id.txv_materia_detalle_clase)
        val tema: TextView? = findViewById<TextView>(R.id.txv_tema_detalle_clase)
        val fecha: TextView? = findViewById<TextView>(R.id.txv_fecha_detalle_clase)
        val hora: TextView? = findViewById<TextView>(R.id.txv_hora_detalle_clase)
        val duracion: TextView? = findViewById<TextView>(R.id.txv_duracion_detalle_clase)
        val estudiante: TextView? = findViewById<TextView>(R.id.txv_estudiante_detalle_clase)
        val estado: Switch = findViewById(R.id.swt_estado_solicitar_clase)

        clase = iGestionarClaseControlador.findClass(this, idClase.toString())
        var builder = AlertDialog.Builder(this)
        val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()

        if (isNetworkConnected(this)) {

            if(clase.id == 0){
                var url = "https://webserviceasesoriasacademicas.000webhostapp.com/cargar_clase.php?idClase=$idClase"
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
                                val jsonArray = response.optJSONArray("class")
                                val jsonObjet = jsonArray.getJSONObject(0)
                                clase.id = jsonObjet.getInt("id_clase")
                                clase.fecha = jsonObjet.getString("fecha")
                                clase.hora = jsonObjet.getString("hora")
                                clase.duracion = jsonObjet.getString("duracion")
                                clase.materia = jsonObjet.getString("materia")
                                clase.tema = jsonObjet.getString("tema")
                                clase.inquietudes = jsonObjet.getString("inquietudes")
                                clase.estado = jsonObjet.getString("estado")
                                estudiante?.setText(jsonObjet.getString("nombre"))

                                materia?.setText(clase.materia)
                                tema?.setText(clase.tema)

                                if(clase.estado.equals("activo")){
                                    estado.setChecked(true)
                                    estado.setTextOn("Activa")
                                } else {
                                    estado.setChecked(false)
                                    estado.setTextOff("Inactiva")
                                }

                                fecha?.setText(clase.fecha)
                                hora?.setText(clase.hora)
                                duracion?.setText(clase.duracion)
                                alertDialog.dismiss()

                                estado.setOnCheckedChangeListener{buttonView, isChecked ->
                                    if (isChecked){
                                        if(iGestionarClaseControlador.changeStatus(this, "activo", idClase.toString()) == 1) {
                                            val estado = "activo"
                                            alertDialog.show()

                                            var url = "https://webserviceasesoriasacademicas.000webhostapp.com/editar_estado.php?idClase=$idClase&estado=$estado"
                                            url = url.replace(" ","%20")
                                            url = url.replace("#","%23")
                                            url = url.replace("-","%2D")
                                            url = url.replace("á","%C3%A1")
                                            url = url.replace("é","%C3%A9")
                                            url = url.replace("í","%C3%AD")
                                            url = url.replace("ó","%C3%B3")
                                            url = url.replace("ú","%C3%BA")
                                            url = url.replace("°","%C2%B0")
                                            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                                                    Response.Listener { response ->
                                                        if (response.getString("success") == "1") {
                                                            Toast.makeText(this, "La clase se cambió a estado ACTIVO", Toast.LENGTH_SHORT).show()
                                                            alertDialog.dismiss()
                                                        } else if (response.getString("error") == "0") {
                                                            Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show()
                                                            alertDialog.dismiss()
                                                        }
                                                    },
                                                    Response.ErrorListener { error ->
                                                        Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show();
                                                        alertDialog.dismiss()
                                                    })
                                            request?.add(jsonObjectRequest)
                                        }
                                    } else {
                                        if(iGestionarClaseControlador.changeStatus(this, "inactivo", idClase.toString()) == 1) {
                                            val estado = "inactivo"
                                            alertDialog.show()

                                            var url = "https://webserviceasesoriasacademicas.000webhostapp.com/editar_estado.php?idClase=$idClase&estado=$estado"
                                            url = url.replace(" ","%20")
                                            url = url.replace("#","%23")
                                            url = url.replace("-","%2D")
                                            url = url.replace("á","%C3%A1")
                                            url = url.replace("é","%C3%A9")
                                            url = url.replace("í","%C3%AD")
                                            url = url.replace("ó","%C3%B3")
                                            url = url.replace("ú","%C3%BA")
                                            url = url.replace("°","%C2%B0")
                                            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                                                    Response.Listener { response ->
                                                        if (response.getString("success") == "1") {
                                                            Toast.makeText(this, "La clase se cambió a estado INACTIVO", Toast.LENGTH_SHORT).show()
                                                            alertDialog.dismiss()
                                                        } else if (response.getString("error") == "0") {
                                                            Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show()
                                                            alertDialog.dismiss()
                                                        }
                                                    },
                                                    Response.ErrorListener { error ->
                                                        Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show()
                                                        alertDialog.dismiss()
                                                    })
                                            request?.add(jsonObjectRequest)
                                        }
                                    }
                                    val email= getIntent().getStringExtra("email")
                                    intentListadoClases.putExtra("email", email);
                                    intentListadoClases.putExtra("fecha", stringFecha);
                                    alertDialog.dismiss()
                                    startActivity(intentListadoClases)
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        },
                        Response.ErrorListener { error ->
                            Toast.makeText(this, "\n" + "Error de registro!", Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                        })
                request?.add(jsonObjectRequest)
            }
            else{
                materia?.setText(clase.materia)
                tema?.setText(clase.tema)

                if(clase.estado.equals("activo")){
                    estado.setChecked(true)
                    estado.setTextOn("Activa")
                } else {
                    estado.setChecked(false)
                    estado.setTextOff("Inactiva")
                }

                fecha?.setText(clase.fecha)
                hora?.setText(clase.hora)
                duracion?.setText(clase.duracion)
                alertDialog.dismiss()

                estado.setOnCheckedChangeListener{buttonView, isChecked ->
                    if (isChecked){
                        if(iGestionarClaseControlador.changeStatus(this, "activo", idClase.toString()) == 1) {
                            val estado = "activo"
                            alertDialog.show()

                            var url = "https://webserviceasesoriasacademicas.000webhostapp.com/editar_estado.php?idClase=$idClase&estado=$estado"
                            url = url.replace(" ","%20")
                            url = url.replace("#","%23")
                            url = url.replace("-","%2D")
                            url = url.replace("á","%C3%A1")
                            url = url.replace("é","%C3%A9")
                            url = url.replace("í","%C3%AD")
                            url = url.replace("ó","%C3%B3")
                            url = url.replace("ú","%C3%BA")
                            url = url.replace("°","%C2%B0")
                            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                                    Response.Listener { response ->
                                        if (response.getString("success") == "1") {
                                            Toast.makeText(this, "La clase se cambió a estado ACTIVO", Toast.LENGTH_SHORT).show()
                                            alertDialog.dismiss()
                                        } else if (response.getString("error") == "0") {
                                            Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show()
                                            alertDialog.dismiss()
                                        }
                                    },
                                    Response.ErrorListener { error ->
                                        Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show()
                                        alertDialog.dismiss()
                                    })
                            request?.add(jsonObjectRequest)
                        }
                    } else {
                        if(iGestionarClaseControlador.changeStatus(this, "inactivo", idClase.toString()) == 1) {
                            val estado = "inactivo"
                            alertDialog.show()

                            var url = "https://webserviceasesoriasacademicas.000webhostapp.com/editar_estado.php?idClase=$idClase&estado=$estado"
                            url = url.replace(" ","%20")
                            url = url.replace("#","%23")
                            url = url.replace("-","%2D")
                            url = url.replace("á","%C3%A1")
                            url = url.replace("é","%C3%A9")
                            url = url.replace("í","%C3%AD")
                            url = url.replace("ó","%C3%B3")
                            url = url.replace("ú","%C3%BA")
                            url = url.replace("°","%C2%B0")
                            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                                    Response.Listener { response ->
                                        if (response.getString("success") == "1") {
                                            Toast.makeText(this, "La clase se cambió a estado INACTIVO", Toast.LENGTH_SHORT).show()
                                            alertDialog.dismiss()
                                        } else if (response.getString("error") == "0") {
                                            Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show()
                                            alertDialog.dismiss()
                                        }
                                    },
                                    Response.ErrorListener { error ->
                                        Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show()
                                        alertDialog.dismiss()
                                    })
                            request?.add(jsonObjectRequest)
                        }
                    }
                    val email= getIntent().getStringExtra("email")
                    intentListadoClases.putExtra("email", email);
                    alertDialog.dismiss()
                    startActivity(intentListadoClases)
                }
            }
        } else{
            Toast.makeText(this, "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_LONG).show()
            alertDialog.dismiss()
        }

        val btnBuscarClase = findViewById<Button>(R.id.btn_buscar_detalle_clase)
        btnBuscarClase.setOnClickListener {
            val intentEditarClase = Intent(this, EditarClaseActivity::class.java)
            val idBusqueda = clase.id.toString()
            intentEditarClase.putExtra("id_clase", idBusqueda)
            val email= intent.getStringExtra("email")
            intentEditarClase.putExtra("email", email)
            intentEditarClase.putExtra("fecha", stringFecha)
            alertDialog.show()

            if (isNetworkConnected(this)) {
                var url = "https://webserviceasesoriasacademicas.000webhostapp.com/cargar_clase.php?idClase=$idClase"
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
                                val jsonArray = response.optJSONArray("class")
                                val jsonObjet = jsonArray.getJSONObject(0)
                                clase.id = jsonObjet.getInt("id_clase")
                                clase.fecha = jsonObjet.getString("fecha")
                                clase.hora = jsonObjet.getString("hora")
                                clase.duracion = jsonObjet.getString("duracion")
                                clase.materia = jsonObjet.getString("materia")
                                clase.tema = jsonObjet.getString("tema")
                                clase.inquietudes = jsonObjet.getString("inquietudes")
                                clase.estado = jsonObjet.getString("estado")

                                if (clase.estado.equals("activo")) {
                                    alertDialog.dismiss()
                                    startActivity(intentEditarClase)
                                } else{
                                    Toast.makeText(this, "No es posible editar una clase con estado INACTIVO", Toast.LENGTH_SHORT).show()
                                    alertDialog.dismiss()
                                }

                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        },
                        Response.ErrorListener { error ->
                            Toast.makeText(this, "\n" + "Ocurrió un error cargando la infomación de su clase!", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss()
                        })
                request?.add(jsonObjectRequest)
            } else{
                Toast.makeText(this, "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            }
        }

        val btnListadoClases = findViewById<Button>(R.id.btn_volver_detalle_clase)
        btnListadoClases.setOnClickListener {
            val email= getIntent().getStringExtra("email")
            intentListadoClases.putExtra("email", email)
            intentListadoClases.putExtra("fecha", stringFecha)
            startActivity(intentListadoClases)
        }
    }

    override fun onBackPressed() {
        val intentListadoClases = Intent(this, GestionarClaseActivity::class.java)
        var idClase= getIntent().getStringExtra("id_clase")
        val email= getIntent().getStringExtra("email")
        val fecha= getIntent().getStringExtra("fecha")
        intentListadoClases.putExtra("id_clase", idClase);
        intentListadoClases.putExtra("email", email);
        intentListadoClases.putExtra("fecha", fecha);
        startActivity(intentListadoClases)
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
            var email= intent.getStringExtra("email")
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
                        val email= intent.getStringExtra("email")
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