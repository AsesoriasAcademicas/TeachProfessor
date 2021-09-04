package com.asesoriasacademicasweb.asesoriasacademicas

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.asesoriasacademicasweb.asesoriasacademicas.Controlador.LoginControlador
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.ILoginVista
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


class LoginActivity : AppCompatActivity(), ILoginVista{

    val iLoginControlador = LoginControlador(this)
    var stringEmail = ""
    var stringPass = ""
    var request: RequestQueue? = null
    private val secretKeyAES = "asesoriasacademicas"
    private val saltAES = "teach2021"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(1000)
        setTheme(R.style.Theme_AsesoriasAcademicas)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        request = Volley.newRequestQueue(this)

        val email = findViewById<TextInputEditText>(R.id.txt_email_login)
        email.requestFocus()
        val password: EditText? = findViewById(R.id.txt_password_login)

        val btnRegistration = findViewById<Button>(R.id.btn_registrarse_login)
        btnRegistration.setOnClickListener{
            var builder = AlertDialog.Builder(this)
            val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            val alertDialog = builder.create()
            alertDialog.show()
            val intentInsert = Intent(this, RegistrarseActivity::class.java)
            startActivity(intentInsert)
        }

        val btnLogin = findViewById<Button>(R.id.btn_iniciar_sesion_login)
        btnLogin.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            val alertDialog = builder.create()
            alertDialog.show()

                stringEmail = email?.text.toString().trim()
                stringPass = password?.text.toString().trim()

                val passEncript = encriptar(stringPass)

                val intentLogin = Intent(this, InicioActivity::class.java)
                var url =
                    "https://webserviceasesoriasacademicas.000webhostapp.com/obtener_persona.php?email=$stringEmail"
                url = url.replace(" ", "%20")
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener { response ->
                        try {
                            if (response.getString("success") == "1") {
                                if (iLoginControlador.onLogin(
                                        this,
                                        stringEmail,
                                        passEncript
                                    ) == -1
                                ) {
                                    intentLogin.putExtra("email", stringEmail)
                                    startActivity(intentLogin)
                                } else {
                                    var url =
                                        "https://webserviceasesoriasacademicas.000webhostapp.com/login.php?email=$stringEmail&password=$passEncript"
                                    url = url.replace(" ", "%20")
                                    val jsonObjectRequest =
                                        JsonObjectRequest(Request.Method.GET, url, null,
                                            Response.Listener { response ->
                                                try {
                                                    if (response.getString("success") == "1") {
                                                        intentLogin.putExtra("email", stringEmail)
                                                        startActivity(intentLogin)
                                                    } else if (response.getString("success") == "0") {
                                                        Toast.makeText(
                                                            this,
                                                            "\n" + "¡La contraseña ingresada es incorrecta!. Por favor inténtalo de nuevo.",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        alertDialog.dismiss()
                                                    }
                                                } catch (e: JSONException) {
                                                    e.printStackTrace()
                                                }
                                            },
                                            Response.ErrorListener { error ->
                                                Toast.makeText(
                                                    this,
                                                    "\n" + "Por favor verifica tu conexión a internet y vuelve a intentarlo!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                alertDialog.dismiss()
                                            })
                                    request?.add(jsonObjectRequest)
                                }
                            } else if (response.getString("success") == "0") {
                                Toast.makeText(
                                    this,
                                    "\n" + "¡No existe un usuario con el email ingresado!. Regístrate para iniciar sesión.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                alertDialog.dismiss()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(
                            this,
                            "\n" + "Por favor verifica tu conexión a internet y vuelve a intentarlo!",
                            Toast.LENGTH_SHORT
                        ).show()
                        alertDialog.dismiss()
                    })
                request?.add(jsonObjectRequest)
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