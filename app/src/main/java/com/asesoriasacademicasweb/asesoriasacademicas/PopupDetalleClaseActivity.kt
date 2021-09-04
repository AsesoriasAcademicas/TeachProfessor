package com.asesoriasacademicasweb.asesoriasacademicas

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.asesoriasacademicasweb.asesoriasacademicas.Controlador.GestionarClaseControlador
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Clase
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IGestionarClaseVista
import org.json.JSONException

class PopupDetalleClaseActivity : AppCompatActivity(), IGestionarClaseVista {

    val iGestionarClaseControlador = GestionarClaseControlador(this)

    var request: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_detalle_class)

        request = Volley.newRequestQueue(this)

        val intentListadoClases = Intent(this, GestionarClaseActivity::class.java)
        val idClase= getIntent().getStringExtra("id_clase")
        var clase = Clase()
        val materia: TextView? = findViewById<TextView>(R.id.txv_materia_detalle_clase)
        val tema: TextView? = findViewById<TextView>(R.id.txv_tema_detalle_clase)
        val fecha: TextView? = findViewById<TextView>(R.id.txv_fecha_detalle_clase)
        val hora: TextView? = findViewById<TextView>(R.id.txv_hora_detalle_clase)
        val duracion: TextView? = findViewById<TextView>(R.id.txv_duracion_detalle_clase)
        val estado: Switch = findViewById(R.id.swt_estado_solicitar_clase)

        clase = iGestionarClaseControlador.findClass(this, idClase.toString())

        if(clase.id == 0){
            var url = "https://webserviceasesoriasacademicas.000webhostapp.com/cargar_clase.php?idClase=$idClase"
            url = url.replace(" ","%20")
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

                            estado.setOnCheckedChangeListener{buttonView, isChecked ->
                                if (isChecked){
                                    if(iGestionarClaseControlador.changeStatus(this, "activo", idClase.toString()) == 1) {
                                        var estado = "activo"
                                        var url = "https://webserviceasesoriasacademicas.000webhostapp.com/editar_estado.php?idClase=$idClase&estado=$estado"
                                        url = url.replace(" ", "%20")
                                        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                                                Response.Listener { response ->
                                                    if (response.getString("success") == "1") {
                                                        Toast.makeText(this, "La clase se cambió a estado ACTIVO", Toast.LENGTH_SHORT).show()
                                                    } else if (response.getString("error") == "0") {
                                                        Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                Response.ErrorListener { error ->
                                                    Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show();
                                                })
                                        request?.add(jsonObjectRequest)
                                    }
                                } else {
                                    if(iGestionarClaseControlador.changeStatus(this, "inactivo", idClase.toString()) == 1) {
                                        var estado = "inactivo"
                                        var url = "https://webserviceasesoriasacademicas.000webhostapp.com/editar_estado.php?idClase=$idClase&estado=$estado"
                                        url = url.replace(" ", "%20")
                                        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                                                Response.Listener { response ->
                                                    if (response.getString("success") == "1") {
                                                        Toast.makeText(this, "La clase se cambió a estado INACTIVO", Toast.LENGTH_SHORT).show()
                                                    } else if (response.getString("error") == "0") {
                                                        Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                Response.ErrorListener { error ->
                                                    Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show();
                                                })
                                        request?.add(jsonObjectRequest)
                                    }
                                }
                                val email= getIntent().getStringExtra("email")
                                intentListadoClases.putExtra("email", email);
                                startActivity(intentListadoClases)
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(this, "\n" + "Error de registro!", Toast.LENGTH_SHORT).show();
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

            estado.setOnCheckedChangeListener{buttonView, isChecked ->
                if (isChecked){
                    if(iGestionarClaseControlador.changeStatus(this, "activo", idClase.toString()) == 1) {
                        var estado = "activo"
                        var url = "https://webserviceasesoriasacademicas.000webhostapp.com/editar_estado.php?idClase=$idClase&estado=$estado"
                        url = url.replace(" ", "%20")
                        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                                Response.Listener { response ->
                                    if (response.getString("success") == "1") {
                                        Toast.makeText(this, "La clase se cambió a estado ACTIVO", Toast.LENGTH_SHORT).show()
                                    } else if (response.getString("error") == "0") {
                                        Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                Response.ErrorListener { error ->
                                    Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show();
                                })
                        request?.add(jsonObjectRequest)
                    }
                } else {
                    if(iGestionarClaseControlador.changeStatus(this, "inactivo", idClase.toString()) == 1) {
                        var estado = "inactivo"
                        var url = "https://webserviceasesoriasacademicas.000webhostapp.com/editar_estado.php?idClase=$idClase&estado=$estado"
                        url = url.replace(" ", "%20")
                        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                                Response.Listener { response ->
                                    if (response.getString("success") == "1") {
                                        Toast.makeText(this, "La clase se cambió a estado INACTIVO", Toast.LENGTH_SHORT).show()
                                    } else if (response.getString("error") == "0") {
                                        Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                Response.ErrorListener { error ->
                                    Toast.makeText(this, "\n" + "Ocurrió un error en el cambio de estado!", Toast.LENGTH_SHORT).show();
                                })
                        request?.add(jsonObjectRequest)
                    }
                }
                val email= getIntent().getStringExtra("email")
                intentListadoClases.putExtra("email", email);
                startActivity(intentListadoClases)
            }
        }

        val btnBuscarClase =findViewById<Button>(R.id.btn_buscar_detalle_clase)
        btnBuscarClase.setOnClickListener {
            val intentEditarClase = Intent(this, EditarClaseActivity::class.java)
            val idBusqueda = clase.id.toString()
            intentEditarClase.putExtra("id_clase", idBusqueda)
            val email= getIntent().getStringExtra("email")
            intentEditarClase.putExtra("email", email);

            var url = "https://webserviceasesoriasacademicas.000webhostapp.com/cargar_clase.php?idClase=$idClase"
            url = url.replace(" ","%20")
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
                                var builder = AlertDialog.Builder(this)
                                val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
                                builder.setView(dialogView)
                                builder.setCancelable(false)
                                val alertDialog = builder.create()
                                alertDialog.show()
                                startActivity(intentEditarClase)
                            } else{
                                Toast.makeText(this, "No es posible editar una clase con estado INACTIVO", Toast.LENGTH_SHORT).show()
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(this, "\n" + "Ocurrió un error cargando la infomación de su clase!", Toast.LENGTH_SHORT).show();
                    })
            request?.add(jsonObjectRequest)
        }

        val btnListadoClases = findViewById<Button>(R.id.btn_volver_detalle_clase)
        btnListadoClases.setOnClickListener {
            val email= getIntent().getStringExtra("email")
            intentListadoClases.putExtra("email", email);
            var builder = AlertDialog.Builder(this)
            val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            val alertDialog = builder.create()
            alertDialog.show()
            startActivity(intentListadoClases)
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