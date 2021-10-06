package com.asesoriasacademicasweb.asesoriasacademicas

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.asesoriasacademicasweb.asesoriasacademicas.Controlador.GanaciasControladorar
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Clase
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Profesor
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IGananciasVista
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*

class GananciasActivity: AppCompatActivity(), IGananciasVista {

    var request: RequestQueue? = null

    val iGananciasControlador = GanaciasControladorar(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganacias)

        var calendar = Calendar.getInstance()
        var anio = calendar.get(Calendar.YEAR)
        var mes = calendar.get(Calendar.MONTH)
        var dia = calendar.get(Calendar.DAY_OF_MONTH)

        var fecha_inicio = findViewById<TextInputEditText>(R.id.txt_fecha_inicio_ganancias)
        fecha_inicio.requestFocus()
        var fecha_fin = findViewById<TextInputEditText>(R.id.txt_fecha_fin_ganancias)
        val ganancia: TextView? = findViewById<TextView>(R.id.txt_valor_ganancias)
        val fondo: TextView? = findViewById<TextView>(R.id.txt_valor_fondo)
        val total: TextView? = findViewById<TextView>(R.id.txt_valor_total)
        val stringEmail= getIntent().getStringExtra("email")

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
            val email= getIntent().getStringExtra("email")
            val intentMain = Intent(this, EditarPerfilActivity::class.java)
            intentMain.putExtra("email", email)
            startActivity(intentMain)
        }

        val btnCalcularGanancias = findViewById<Button>(R.id.btn_calcular_ganancias)
        btnCalcularGanancias.setOnClickListener{
            request = Volley.newRequestQueue(this)
            var builder = AlertDialog.Builder(this)
            val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            val alertDialog = builder.create()
            alertDialog.show()

            if(!fecha_inicio.text.toString().isEmpty() && !fecha_fin.text.toString().isEmpty()){
                var url = "https://webserviceasesoriasacademicas.000webhostapp.com/ganancias_profesor.php?email=$stringEmail&fecha_inicio=${fecha_inicio.text}&fecha_fin=${fecha_fin.text}"
                url = url.replace(" ","%20")

                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null,
                        Response.Listener { response ->
                            try {
                                val jsonArray = response.optJSONArray("ganancias")
                                val jsonObjet = jsonArray.getJSONObject(0)
                                var ganancia_clases = jsonObjet.getInt("GANANCIA")
                                var fondo_clases= jsonObjet.getInt("FONDO")
                                var total_clases = jsonObjet.getInt("TOTAL")
                                ganancia?.setText("$ " + ganancia_clases.toString())
                                fondo?.setText("$ " + fondo_clases.toString())
                                total?.setText("$ " + total_clases.toString())
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
        }
    }

    override fun onLoginSuccess(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onLoginError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}