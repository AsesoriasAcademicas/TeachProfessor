package com.asesoriasacademicasweb.asesoriasacademicas

import android.R.attr.data
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.asesoriasacademicasweb.asesoriasacademicas.Controlador.RegistrarseControlador
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Persona
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Profesor
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IRegistrarseVista
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


class RegistrarseActivity : AppCompatActivity(), IRegistrarseVista {

    val iRegistraseControlador = RegistrarseControlador(this)
    var stringNombre = ""
    var stringEmail = ""
    var stringPass = ""
    var stringRepetPass= ""
    var booleanCheck = false
    var request: RequestQueue? = null
    private val secretKeyAES = "asesoriasacademicas"
    private val saltAES = "teach2021"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        request = Volley.newRequestQueue(this)

        val btnInsertarPersona = findViewById<Button>(R.id.btn_registrarse_registro)
        btnInsertarPersona.setOnClickListener {

            var profesor = Profesor()
            val nombre = findViewById<TextInputEditText>(R.id.txt_nombre_registro)
            val email = findViewById<TextInputEditText>(R.id.txt_email_registro)
            val password = findViewById<TextInputEditText>(R.id.txt_password_registro)
            val repetPassword = findViewById<TextInputEditText>(R.id.txt_repet_pass_registro)
            val checkTerminosCondiciones: CheckBox? = findViewById(R.id.checkbox_meat)
            stringNombre = nombre?.text.toString().trim()
            stringEmail = email?.text.toString().trim()
            stringPass = password?.text.toString().trim()
            stringRepetPass = repetPassword?.text.toString().trim()
            booleanCheck = checkTerminosCondiciones?.isChecked()!!

            val passEncript = encriptar(stringPass)

            val intentRegistry = Intent(this, GestionarClaseActivity::class.java)

            if(iRegistraseControlador.onRegistry(this, stringNombre, stringEmail, stringPass, stringRepetPass, booleanCheck) == -1) {
                val persona = Persona(stringNombre, stringEmail, passEncript)

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
                                if(profesor.id == 0){
                                        var url = "https://webserviceasesoriasacademicas.000webhostapp.com/registrar_profesor.php?nombre=$stringNombre&email=$stringEmail" +
                                                "&telefono=&direccion=&password=$passEncript"
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
                                                        if (response.getString("success") == "1"){
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
                                                                        if (iRegistraseControlador.insertUser(this, persona, profesor.id) == 1) {
                                                                            intentRegistry.putExtra("email", stringEmail)
                                                                            var builder = AlertDialog.Builder(this)
                                                                            val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
                                                                            builder.setView(dialogView)
                                                                            builder.setCancelable(false)
                                                                            val alertDialog = builder.create()
                                                                            alertDialog.show()
                                                                            startActivity(intentRegistry)
                                                                        }
                                                                    } catch (e: JSONException) {
                                                                        e.printStackTrace()
                                                                    }
                                                                },
                                                                Response.ErrorListener { error ->
                                                                    Toast.makeText(this, "\n" + "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_SHORT).show();
                                                                })
                                                            request?.add(jsonObjectRequest)
                                                        } else if(response.getString("success") == "0") {
                                                            Toast.makeText(this, "\n" + "Ocurrió un error en el registro de su información!", Toast.LENGTH_SHORT).show()
                                                        }
                                                    } catch (e: JSONException) {
                                                        e.printStackTrace()
                                                    }
                                                },
                                                Response.ErrorListener { error ->
                                                    Toast.makeText(this, "\n" + "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_SHORT).show();
                                                })
                                        request?.add(jsonObjectRequest)
                                } else {
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
                                                    if (iRegistraseControlador.insertUser(this, persona, profesor.id) == 1) {
                                                        intentRegistry.putExtra("email", stringEmail)
                                                        startActivity(intentRegistry)
                                                    }
                                                } catch (e: JSONException) {
                                                    e.printStackTrace()
                                                }
                                            },
                                            Response.ErrorListener { error ->
                                                Toast.makeText(this, "\n" + "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_SHORT).show();
                                            })
                                    request?.add(jsonObjectRequest)
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        },
                        Response.ErrorListener { error ->
                            Toast.makeText(this, "\n" + "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_SHORT).show();
                        })
                request?.add(jsonObjectRequest)
            }
        }

        val btnCancelarRegistro = findViewById<Button>(R.id.btn_cancelar_registro)
        btnCancelarRegistro.setOnClickListener{
            val intentLogin = Intent(this, LoginActivity::class.java)
            var builder = AlertDialog.Builder(this)
            val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            val alertDialog = builder.create()
            alertDialog.show()
            startActivity(intentLogin)
        }
    }

    override fun onResume() {
        super.onResume()
        var builder = AlertDialog.Builder(this)
        val alertDialog = builder.create()
        alertDialog.dismiss()
    }

    override fun onLoginSuccess(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onLoginError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encriptar(mensajeAEncriptar: String): String {
        try {
            val iv = ByteArray(16)
            val ivParameterSpec = IvParameterSpec(iv)
            val secretKeyFactory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val keySpec: KeySpec = PBEKeySpec(secretKeyAES.toCharArray(), saltAES.toByteArray(), 65536, 256)
            val secretKeyTemp: SecretKey = secretKeyFactory.generateSecret(keySpec)
            val secretKey = SecretKeySpec(secretKeyTemp.getEncoded(), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
            return Base64.getEncoder().encodeToString(cipher.doFinal(mensajeAEncriptar.toByteArray()))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun desEncriptar(mensajeEncriptado: String): String {
        val iv = ByteArray(16)
        try {
            val ivParameterSpec = IvParameterSpec(iv)
            val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val keySpec: KeySpec = PBEKeySpec(secretKeyAES.toCharArray(), saltAES.toByteArray(), 65536, 256)
            val secretKeyTemp = secretKeyFactory.generateSecret(keySpec)
            val secretKey = SecretKeySpec(secretKeyTemp.encoded, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)
            return String(cipher.doFinal(Base64.getDecoder().decode(mensajeEncriptado)))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }
}