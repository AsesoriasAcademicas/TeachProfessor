package com.asesoriasacademicasweb.asesoriasacademicas

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.asesoriasacademicasweb.asesoriasacademicas.Controlador.EditarPerfilControlador
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Persona
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IEditarPerfilVista
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

@Suppress("DEPRECATION")
class EditarPerfilActivity : AppCompatActivity(), IEditarPerfilVista {

    val iEditarPerfilControlador = EditarPerfilControlador(this)
    var request: RequestQueue? = null

    private val secretKeyAES = "asesoriasacademicas"
    private val saltAES = "teach2021"

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        request = Volley.newRequestQueue(this)

        val nombre = findViewById<TextInputEditText>(R.id.txt_nombre_editar_perfil)
        nombre.requestFocus()
        val telefono = findViewById<TextInputEditText>(R.id.txt_telefono_editar_perfil)
        val direccion = findViewById<TextInputEditText>(R.id.txt_direccion_editar_perfil)
        val password = findViewById<TextInputEditText>(R.id.txt_password_editar_perfil)
        val repetPassword = findViewById<TextInputEditText>(R.id.txt_repet_password_editar_perfil)

        var persona = Persona()
        val emailBuscado= getIntent().getStringExtra("email")

        persona = iEditarPerfilControlador.getUser(this, "" + emailBuscado)

        var builder = AlertDialog.Builder(this)
        val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()

        if (isNetworkConnected(this)) {

        if(persona != null){
            var url = "https://webserviceasesoriasacademicas.000webhostapp.com/obtener_persona.php?email=$emailBuscado"
            url = url.replace(" ","%20")
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null,
                    Response.Listener { response ->
                        try {
                            if (response.getString("success") == "1") {
                                var url = "https://webserviceasesoriasacademicas.000webhostapp.com/cargar_perfil.php?email=$emailBuscado"
                                url = url.replace(" ","%20")
                                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null,
                                        Response.Listener { response ->
                                            try {
                                                val jsonArray = response.optJSONArray("user")
                                                val jsonObjet = jsonArray.getJSONObject(0)
                                                persona.nombre = jsonObjet.getString("nombre")
                                                persona.telefono = jsonObjet.getString("telefono")
                                                persona.email = jsonObjet.getString("email")
                                                persona.direccion = jsonObjet.getString("direccion")
                                                persona.contrasenia = jsonObjet.getString("password")

                                                nombre?.setText(persona.nombre)
                                                telefono?.setText(persona.telefono)
                                                direccion?.setText(persona.direccion)

                                                val passDesEncript = desEncriptar(persona.contrasenia)

                                                password?.setText(passDesEncript)
                                                repetPassword?.setText(passDesEncript)
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
            password?.setText(persona.contrasenia)
            repetPassword?.setText(persona.contrasenia)
            alertDialog.dismiss()
        }

        } else{
            Toast.makeText(this, "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_LONG).show()
            alertDialog.dismiss()
        }

        val btnCancelarEditarPerfil = findViewById<Button>(R.id.btn_cancelar_editar_perfil)
        btnCancelarEditarPerfil.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cancelar edición de perfil")
            builder.setMessage("¿Seguro que deseas cancelar la edición del perfil?")
                    .setCancelable(false)
                    .setPositiveButton("Confirmar") { dialog, id ->
                        val intentMain = Intent(this, InicioActivity::class.java)
                        val email= getIntent().getStringExtra("email")
                        intentMain.putExtra("email", emailBuscado);
                        startActivity(intentMain)
                    }
                    .setNegativeButton("Cancelar") { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }

        val btnGuardarEditarPerfil = findViewById<Button>(R.id.btn_guardar_editar_perfil)
        btnGuardarEditarPerfil.setOnClickListener{

            val nombre: EditText? = findViewById(R.id.txt_nombre_editar_perfil)
            val telefono: EditText? = findViewById(R.id.txt_telefono_editar_perfil)
            val direccion: EditText? = findViewById(R.id.txt_direccion_editar_perfil)
            val password: EditText? = findViewById(R.id.txt_password_editar_perfil)
            val repetPassword: EditText? = findViewById(R.id.txt_repet_password_editar_perfil)
            val stringNombre = nombre?.text.toString().trim()
            val stringEmail = persona.email
            val stringTelefono = telefono?.text.toString().trim()
            val stringDireccion = direccion?.text.toString().trim()
            val stringPass = password?.text.toString().trim()
            val stringRepetPass = repetPassword?.text.toString().trim()

            val passEncript = encriptar(stringPass)

            val intentEditProfile = Intent(this, InicioActivity::class.java)

            if(iEditarPerfilControlador.onEditProfile(this, stringNombre, stringEmail, stringTelefono, stringDireccion, stringPass, stringRepetPass) == -1) {
                val persona = Persona(stringNombre, stringEmail, stringTelefono, stringDireccion, passEncript, "Estudiante")
                if (iEditarPerfilControlador.updateProfile(this, persona) == 1) {
                    alertDialog.show()
                    var url = "https://webserviceasesoriasacademicas.000webhostapp.com/editar_perfil.php?nombre=$stringNombre&email=$stringEmail" +
                            "&telefono=$stringTelefono&direccion=$stringDireccion&password=$passEncript"
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
                                    intentEditProfile.putExtra("email", stringEmail!!)
                                    alertDialog.dismiss()
                                    startActivity(intentEditProfile)
                                } else if(response.getString("error") == "0") {
                                    Toast.makeText(this, "\n" + "Ocurrió un error en la actualización de su perfil!", Toast.LENGTH_SHORT).show()
                                    alertDialog.dismiss()
                                }
                            },
                            Response.ErrorListener { error ->
                                Toast.makeText(this, "\n" + "Ocurrió un error en la actualización de su perfil!", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss()
                            })
                    request?.add(jsonObjectRequest)
                }
            }
        }

        val btnGnancias = findViewById<ImageView>(R.id.img_moneda_editar_perfil)
        btnGnancias.setOnClickListener{
            val email= getIntent().getStringExtra("email")
            val intentGanancias = Intent(this, GananciasActivity::class.java)
            intentGanancias.putExtra("email", email);
            startActivity(intentGanancias)
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cancelar edición de perfil")
        builder.setMessage("¿Seguro que deseas cancelar la edición del perfil?")
                .setCancelable(false)
                .setPositiveButton("Confirmar") { dialog, id ->
                    val intentLogout = Intent(this, InicioActivity::class.java)
                    val email= getIntent().getStringExtra("email")
                    intentLogout.putExtra("email", email);
                    startActivity(intentLogout)
                }
                .setNegativeButton("Cancelar") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
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