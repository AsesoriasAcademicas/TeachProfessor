package com.asesoriasacademicasweb.asesoriasacademicas

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.asesoriasacademicasweb.asesoriasacademicas.Controlador.GanaciasControladorar
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IGananciasVista
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class GananciasActivity: AppCompatActivity(), IGananciasVista {

    var request: RequestQueue? = null

    val iGananciasControlador = GanaciasControladorar(this)

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganacias)

        val calendar = Calendar.getInstance()
        val anio = calendar.get(Calendar.YEAR)
        val mes = calendar.get(Calendar.MONTH)
        val dia = calendar.get(Calendar.DAY_OF_MONTH)

        var fecha_inicio = findViewById<TextInputEditText>(R.id.txt_fecha_inicio_ganancias)
        fecha_inicio.requestFocus()
        var fecha_fin = findViewById<TextInputEditText>(R.id.txt_fecha_fin_ganancias)
        val ganancia: TextView? = findViewById<TextView>(R.id.txt_valor_ganancias)
        val fondo: TextView? = findViewById<TextView>(R.id.txt_valor_fondo)
        val total: TextView? = findViewById<TextView>(R.id.txt_valor_total)
        val stringEmail= intent.getStringExtra("email")

        ganancia?.setText("$ -")
        fondo?.setText("$ -")
        total?.setText("$ -")

        val formatofecha = SimpleDateFormat("dd/mm/yyyy")
        val lanzadorFechaInicio: ImageView = findViewById(R.id.img_fecha_inicio_ganancias)
        fecha_inicio = findViewById(R.id.txt_fecha_inicio_ganancias)
        lanzadorFechaInicio.setOnClickListener{
            fecha_inicio.requestFocus()
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, anio, mes, dia ->
                val fechaCalendario = "" + dia + "/" + (mes + 1) + "/" + anio
                val date: Date  = formatofecha.parse(fechaCalendario)
                fecha_inicio?.setText(formatofecha.format(date))
            }, anio, mes, dia)
            datePickerDialog.show()
        }

        val lanzadorFechaFin: ImageView = findViewById(R.id.img_fecha_fin_ganancias)
        fecha_fin = findViewById(R.id.txt_fecha_fin_ganancias)
        lanzadorFechaFin.setOnClickListener{
            fecha_fin.requestFocus()
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, anio, mes, dia ->
                val fechaCalendario = "" + dia + "/" + (mes + 1) + "/" + anio
                val date: Date  = formatofecha.parse(fechaCalendario)
                fecha_fin?.setText(formatofecha.format(date))
            }, anio, mes, dia)
            datePickerDialog.show()
        }

        val btnCancelarGanancias = findViewById<Button>(R.id.btn_cancelar_ganancias)
        btnCancelarGanancias.setOnClickListener{
            val email= intent.getStringExtra("email")
            val intentMain = Intent(this, EditarPerfilActivity::class.java)
            intentMain.putExtra("email", email)
            startActivity(intentMain)
        }

        val btnCalcularGanancias = findViewById<Button>(R.id.btn_calcular_ganancias)
        btnCalcularGanancias.setOnClickListener{
            request = Volley.newRequestQueue(this)
            val builder = AlertDialog.Builder(this)
            val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            val alertDialog = builder.create()
            alertDialog.show()

            if (isNetworkConnected(this)) {

                if(!fecha_inicio.text.toString().isEmpty() && !fecha_fin.text.toString().isEmpty()){
                    var url = "https://webserviceasesoriasacademicas.000webhostapp.com/ganancias_profesor.php?email=$stringEmail&fecha_inicio=${fecha_inicio.text}&fecha_fin=${fecha_fin.text}"
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
                                    val jsonArray = response.optJSONArray("ganancias")
                                    val jsonObjet = jsonArray.getJSONObject(0)
                                    val ganancia_clases = jsonObjet.getInt("GANANCIA")
                                    val fondo_clases= jsonObjet.getInt("FONDO")
                                    val total_clases = jsonObjet.getInt("TOTAL")
                                    ganancia?.text = "$ $ganancia_clases"
                                    fondo?.text = "$ $fondo_clases"
                                    total?.text = "$ $total_clases"
                                    alertDialog?.dismiss()
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            },
                            Response.ErrorListener { error ->
                                Toast.makeText(this, "\n" + "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_SHORT).show()
                                alertDialog?.dismiss()
                            })
                    request?.add(jsonObjectRequest)
                }
                else{
                    Toast.makeText(this, "\n" + "Por favor ingresa los rangos de fecha y vuelve a intentarlo!", Toast.LENGTH_SHORT).show()
                    alertDialog?.dismiss()
                }
            } else{
                Toast.makeText(this, "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            }
        }
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