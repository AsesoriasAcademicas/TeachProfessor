package com.asesoriasacademicasweb.asesoriasacademicas

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.asesoriasacademicasweb.asesoriasacademicas.Controlador.EditarClaseControlador
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Clase
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Estudiante
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Modelo
import com.asesoriasacademicasweb.asesoriasacademicas.Model.Tutoria
import com.asesoriasacademicasweb.asesoriasacademicas.Vista.IEditarClaseVista
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class EditarClaseActivity : AppCompatActivity(), IEditarClaseVista {

    var calendar = Calendar.getInstance()
    var anio = calendar.get(Calendar.YEAR)
    var mes = calendar.get(Calendar.MONTH)
    var dia = calendar.get(Calendar.DAY_OF_MONTH)

    var hora = calendar.get(Calendar.HOUR_OF_DAY)
    var minutos = calendar.get(Calendar.MINUTE)

    val iEditarClaseControlador = EditarClaseControlador(this)
    var request: RequestQueue? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_clase)

        request = Volley.newRequestQueue(this)

        val idClase= intent.getStringExtra("id_clase")
        var idestudiante = 0
        var precio: Int
        val obj = Modelo()
        var clase: Clase
        val tutoria = Tutoria()

        var fecha_fin = findViewById<TextInputEditText>(R.id.txt_fecha_fin_editar_clase)
        val checkBoxDays = findViewById<HorizontalScrollView>(R.id.checkbox_days_editar_clase)
        val fechaFin = findViewById<RelativeLayout>(R.id.fecha_fin_editar_clase)
        val radioButtonRecurrente= findViewById<MaterialRadioButton>(R.id.rbtn_recurrente_editar_clase)
        val radioButtonNoRecurrente= findViewById<MaterialRadioButton>(R.id.rbtn_no_recurrente_editar_clase)
        val radioGroup = findViewById<RadioButton>(R.id.radioGroup) as RadioGroup
        radioGroup.setOnCheckedChangeListener { group, ID ->
            when (ID) {
                R.id.rbtn_recurrente_editar_clase -> {
                    checkBoxDays.isVisible = true
                    fechaFin.isVisible = true
                }
                R.id.rbtn_no_recurrente_editar_clase -> {
                    checkBoxDays.isVisible = false
                    fechaFin.isVisible = false
                }
            }
        }

        val autotextView= findViewById<AutoCompleteTextView>(R.id.spn_materia_edit)
        autotextView.requestFocus()
        val autotextViewTema= findViewById<AutoCompleteTextView>(R.id.spn_tema_edit)
        val autotextViewAlumno= findViewById<AutoCompleteTextView>(R.id.spn_alumno)
        val inquietudes: EditText? = findViewById<EditText>(R.id.txt_inquietudes_editar_clase)
        var fecha = findViewById<TextInputEditText>(R.id.txt_fecha_editar_clase)
        fecha.inputType = InputType.TYPE_NULL
        val tiempo = findViewById<TextInputEditText>(R.id.txt_hora_editar_clase)
        tiempo.inputType = InputType.TYPE_NULL
        val estudiantes: ArrayList<Estudiante> = ArrayList<Estudiante>()
        val duracion= findViewById<TextInputEditText>(R.id.txt_duracion_editar_clase)
        val precioClase = findViewById<TextInputEditText>(R.id.txt_precio_editar_clase)

        clase = obj.buscarClase(this, idClase.toString())
        val builder = AlertDialog.Builder(this)
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
                                clase.idEstudiante = jsonObjet.getInt("id_estudiante")
                                precio = jsonObjet.getInt("precio")

                                autotextView.setText(clase.materia, false)
                                autotextViewTema.setText(clase.tema, false)
                                inquietudes?.setText(clase.inquietudes)
                                fecha?.setText(clase.fecha)
                                tiempo?.setText(clase.hora)
                                idestudiante = clase.idEstudiante
                                duracion?.setText(clase.duracion)
                                precioClase?.setText(Integer.toString(precio))
                                alertDialog.dismiss()
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        },
                        Response.ErrorListener { error ->
                            Toast.makeText(this, "\n" + "Ocurrió un error cargando la infomación de su clase!", Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                        })
                request?.add(jsonObjectRequest)
            }
            else{
                autotextView.setText(clase.materia, false)
                autotextViewTema.setText(clase.tema, false)
                inquietudes?.setText(clase.inquietudes)
                fecha?.setText(clase.fecha)
                tiempo?.setText(clase.hora)
                duracion?.setText(clase.duracion)
                alertDialog.dismiss()
            }

        } else{
            Toast.makeText(this, "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_LONG).show()
            alertDialog.dismiss()
        }

        var materiaSeleccionada = ""
        var temaSeleccionado = ""

        val materias = arrayOf(
                "Matemáticas 3°",
                "Matemáticas 4°",
                "Matemáticas 5°",
                "Matemáticas 6°",
                "Matemáticas 7°",
                "Matemáticas 8°",
                "Matemáticas 9°",
                "Matemáticas 10°",
                "Matemáticas 11°",
                "Geometría",
                "Probabilidad y Estadística",
                "Precálculo",
                "Física",
                "Herramientas Ofimáticas"
        )

        val matematicas3 = arrayOf(
                "Los números naturales",
                "Composición y descomposición",
                "Orden de números naturales",
                "Comparación y ordenación >, <,  =",
                "Noción de fracciones",
                "Representación gráfica de fracciones",
                "Fracciones equivalentes",
                "Adición, sustracción y sus propiedades",
                "Problemas de adición y sustracción",
                "Multiplicación, división y sus propiedades",
                "Mitad, tercio y cuarto",
                "Problemas de multiplicación y división"
        )

        val matematicas4 = arrayOf(
                "Los números naturales",
                "Adición llevando",
                "Sustracción llevando",
                "Multiplicación por 2 y 3 cifras",
                "División con ceros en el cociente",
                "División con divisores de 2 o más cifras",
                "Operaciones combinadas",
                "Los números fraccionarios",
                "Operaciones con fracciones",
                "Los números decimales",
                "Adición y sustracción",
                "Multiplicación y división",
                "Múltiplos y divisores"
        )

        val matematicas5 = arrayOf(
                "Los números naturales",
                "Adición y sustracción",
                "Multiplicación",
                "División con ceros en el cociente",
                "División con divisores de 2 y más cifras",
                "Operaciones combinadas",
                "Los números decimales",
                "Lectura y escritura de fracciones",
                "Comparación de fracciones",
                "Operaciones con fracciones",
                "Adición y sustracción",
                "Multiplicación y división",
                "Los números Romanos"
        )

        val matematicas6 = arrayOf(
                "Romano, binario y decimal",
                "Los números naturales",
                "Operaciones con números naturales",
                "Potenciación, radicación y logaritmación",
                "Múltiplos y divisores",
                "Descomposición factorial",
                "Los números enteros",
                "La recta numérica(Plano cartesiano)",
                "Operaciones con números enteros",
                "Ecuaciones simples",
                "Los números fraccionarios",
                "Operaciones con números fraccionarios",
                "Problemas verbales"
        )

        val matematicas7 = arrayOf(
                "Números enteros, polinomios aritméticos",
                "Expresiones con signos de agrupación",
                "Los números racionales",
                "Operaciones con numeros racionales",
                "Problemas verbales",
                "Los números decimales",
                "Operaciones con números decimales",
                "Planteamiento y resolución de problemas",
                "Proporcionalidad",
                "Magnitudes directamente proporcionales",
                "Magnitudes inversamente proporcionales",
                "Regla de tres simple",
                "Problemas de aplicación"
        )

        val matematicas8 = arrayOf(
                "Conjuntos numéricos",
                "Los números naturales",
                "Los números enteros",
                "Los números racionales",
                "Los números irracionales",
                "Expresiones algebraicas",
                "Monomios, trinomios y polinomios",
                "Operaciones con expresiones algebraicas",
                "Productos y cocientes notables",
                "Triángulo de Pascal",
                "Casos de factorización",
                "Fracciones algebraicas",
                "Sistema de ecuaciones lineales 2x2",
                "Solución para sistemas de ecuaciones",
                "Problemas de aplicación"
        )

        val matematicas9 = arrayOf(
                "Proporcionalidad, regla de tres simple",
                "Proporcionalidad, regla de tres compuesta",
                "Expresiones algebraicas",
                "Función lineal, ecuación de la recta",
                "Sistema de ecuaciones lineales 2x2",
                "Solución, sistemas de ecuaciones 2x2",
                "Sistema de ecuaciones lineales 3x3",
                "Solución, sistemas de ecuaciones 3x3",
                "Función cuadrática",
                "Raíces o soluciones de la función cuadrática",
                "Función exponencial y logarítmica",
                "Sucesiones y progresiones"
        )

        val matematicas10 = arrayOf(
                "Razones trigonométricas",
                "Triángulos rectángulos",
                "Funciones trigonométricas",
                "Gráfica de funciones trigonométricas",
                "Identidades trigonométricas",
                "Ecuaciones trigonométricas",
                "Teorema o ley del seno",
                "Teorema o ley del coseno",
                "Vectores en el plano y el espacio",
                "Producto punto y producto vectorial",
                "Geometría analítica",
                "Distancia entre dos puntos",
                "Ecuación de la recta",
                "La parábola, elipse e hipérbola"
        )

        val matematicas11 = arrayOf(
                "Proporcionalidad y regla de tres",
                "Funciones y gráficas",
                "Función lineal, cuadrática y polinómica",
                "Función parte entera y valor absoluto",
                "Función inversa y racional",
                "Función exponencial y logarítmica",
                "Fución periódica y trigonométrica",
                "Límites y continuidad",
                "Cálculo de límites, álgebra de límites",
                "Límite de funciones indeterminadas",
                "Continuidad y discontinuidad",
                "Derivadas, propiedades de las derivadas",
                "Regla de la cadena",
                "La antiderivada, integrales"
        )

        val geometria = arrayOf(
                "Recta, semirecta y segmento",
                "Ángulos",
                "Clasificación y relacion de ángulos",
                "Triángulos",
                "Clasificación y propiedades de los triángulos",
                "Teorema de Pitágoras",
                "Cuadriláteros y polígonos",
                "Circunferencia y círculo",
                "Perímetros y áreas",
                "Figuras Geométricas",
                "Semejanza de triángulos, teorema de Thales",
                "Áreas y volúmenes"
        )

        val probabilidadyEstadistica = arrayOf(
                "Teoria de conjuntos",
                "Operaciones de conjuntos",
                "Técnicas de conteo",
                "Principio multiplicativo y aditivo",
                "Permutaciones y combinaciones",
                "Probabilidad, términos básicos",
                "Probabilidad clásica",
                "Estadística descriptiva",
                "Medidas de tendencia central",
                "Media aritmética, mediana, moda, Qk, Dk, Pk"
        )

        val precalculo = arrayOf(
                "Función y relación",
                "Gráficar y tabular una función",
                "Operaciones con funciones",
                "Funciones compuestas",
                "Intervalos, intervalo cerrado y abierto",
                "Dominio y rango de una función",
                "Funciones polinominales",
                "Ceros de una función polinominal",
                "División sintética o Ruffini",
                "Funciones cuadráticas",
                "Funciones racionales",
                "Asíndotas de una función racional"
        )

        val fisica = arrayOf(
                "Magnitudes física y derivadas",
                "Sistema de conversión de unidades",
                "Mov. uniforme rectilíneo (MUR)",
                "Mov. uniforme rectilíneo acelerado (MURA)",
                "Caída libre",
                "Movimiento de proyectiles",
                "Movimiento circular uniforme",
                "Leyes de Newton",
                "Energía de un sistema",
                "Conservación de la energía"
        )

        val herramientasOfimaticas = arrayOf(
                "Microsoft Word",
                "Microsoft Excel",
                "Microsoft PowerPoint",
                "Microsoft Access"
        )

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, materias)
        val adapterMath3 = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, matematicas3)
        val adapterMath4 = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, matematicas4)
        val adapterMath5 = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, matematicas5)
        val adapterMath6 = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, matematicas6)
        val adapterMath7 = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, matematicas7)
        val adapterMath8 = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, matematicas8)
        val adapterMath9 = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, matematicas9)
        val adapterMath10 = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, matematicas10)
        val adapterMath11 = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, matematicas11)
        val adapterGeometry = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, geometria)
        val adapterStadistic = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, probabilidadyEstadistica)
        val adapterCalculus = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, precalculo)
        val adapterPhysical = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, fisica)
        val adapterToolsOffice = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, herramientasOfimaticas)
        autotextView.setAdapter(adapter)

        request = Volley.newRequestQueue(this)

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
                            val adaptador: ArrayAdapter<Estudiante> =
                                    ArrayAdapter(this, R.layout.activity_listview, R.id.label, estudiantes)
                            for (i in estudiantes) {
                                if (i.id == idestudiante) {
                                    autotextViewAlumno.setText(i.toString(), false)
                                }
                            }

                            if (adaptador.count != 0) {
                                autotextViewAlumno?.setAdapter(adaptador)
                                alertDialog.dismiss()

                            } else {
                                val mensajeClasesVacio: ArrayList<String> = ArrayList()
                                mensajeClasesVacio.add("No hay estudiantes registrados")
                                val adaptadorEmpty: ArrayAdapter<String> = ArrayAdapter<String>(
                                        this,
                                        R.layout.activity_listview,
                                        R.id.label_empty,
                                        mensajeClasesVacio
                                )
                                autotextViewAlumno?.setAdapter(adaptadorEmpty)
                                alertDialog.dismiss()
                            }
                        } catch (e: JSONException) {
                            val mensajeClasesVacio: ArrayList<String> = ArrayList()
                            mensajeClasesVacio.add("No hay estudiantes registrados")
                            val adaptadorEmpty: ArrayAdapter<String> = ArrayAdapter<String>(
                                    this,
                                    R.layout.activity_listview,
                                    R.id.label_empty,
                                    mensajeClasesVacio
                            )
                            autotextViewAlumno?.setAdapter(adaptadorEmpty)
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

        autotextView.onItemClickListener = AdapterView.OnItemClickListener{
            parent,view,position,id->
            materiaSeleccionada = parent.getItemAtPosition(position).toString()

            if (materiaSeleccionada.equals("Matemáticas 3°")){
                autotextViewTema.setAdapter(adapterMath3)
            } else if (materiaSeleccionada.equals("Matemáticas 4°")){
                autotextViewTema.setAdapter(adapterMath4)
            } else if (materiaSeleccionada.equals("Matemáticas 5°")){
                autotextViewTema.setAdapter(adapterMath5)
            } else if (materiaSeleccionada.equals("Matemáticas 6°")){
                autotextViewTema.setAdapter(adapterMath6)
            } else if (materiaSeleccionada.equals("Matemáticas 7°")){
                autotextViewTema.setAdapter(adapterMath7)
            } else if (materiaSeleccionada.equals("Matemáticas 8°")){
                autotextViewTema.setAdapter(adapterMath8)
            } else if (materiaSeleccionada.equals("Matemáticas 9°")){
                autotextViewTema.setAdapter(adapterMath9)
            } else if (materiaSeleccionada.equals("Matemáticas 10°")){
                autotextViewTema.setAdapter(adapterMath10)
            } else if (materiaSeleccionada.equals("Matemáticas 11°")){
                autotextViewTema.setAdapter(adapterMath11)
            } else if (materiaSeleccionada.equals("Geometría")){
                autotextViewTema.setAdapter(adapterGeometry)
            } else if (materiaSeleccionada.equals("Probabilidad y Estadística")){
                autotextViewTema.setAdapter(adapterStadistic)
            } else if (materiaSeleccionada.equals("Precálculo")){
                autotextViewTema.setAdapter(adapterCalculus)
            } else if (materiaSeleccionada.equals("Física")){
                autotextViewTema.setAdapter(adapterPhysical)
            } else if (materiaSeleccionada.equals("Herramientas Ofimáticas")){
                autotextViewTema.setAdapter(adapterToolsOffice)
            }
        }

        autotextViewTema.onItemClickListener = AdapterView.OnItemClickListener{
            parent,view,position,id->
            temaSeleccionado = parent.getItemAtPosition(position).toString()
            autotextView.clearFocus()
            autotextViewTema.requestFocus()
        }

        val btnEditarClase = findViewById<Button>(R.id.btn_guardar_editar_clase)
        btnEditarClase.setOnClickListener{

            val estudiante = Estudiante()
            val inquietudes: EditText? = findViewById(R.id.txt_inquietudes_editar_clase)
            fecha = findViewById(R.id.txt_fecha_editar_clase)
            val horaMinutos = findViewById<TextInputEditText>(R.id.txt_hora_editar_clase)
            val duracion: EditText? = findViewById(R.id.txt_duracion_editar_clase)

            var stringMateria = ""
            stringMateria = if (materiaSeleccionada != ""){
                materiaSeleccionada
            } else{
                clase.materia
            }

            var stringTema = ""
            stringTema = if (temaSeleccionado != ""){
                temaSeleccionado
            } else{
                clase.tema
            }
            val stringInquietudes = inquietudes?.text.toString().trim()
            val stringFecha = fecha?.text.toString().trim()
            val stringHoraMinutos = horaMinutos?.text.toString().trim()
            val stringDuracion = duracion?.text.toString().trim()
            var stringPrecio = precioClase?.text.toString().trim()

            System.out.println(stringMateria)
            System.out.println(stringTema)
            if(iEditarClaseControlador.onEditClass(this, stringFecha, stringHoraMinutos, stringDuracion, stringMateria, stringTema, stringInquietudes, clase.estado, estudiante.id) == -1) {
                tutoria.materia = stringMateria
                tutoria.tema = stringTema
                tutoria.inquietudes = stringInquietudes
                tutoria.estado = clase.estado
                clase.fecha = stringFecha
                clase.hora = stringHoraMinutos
                clase.duracion = stringDuracion

                if (iEditarClaseControlador.updateClase(this, tutoria, clase) == 1) {
                    val intentDetalleClase = Intent(this, PopupDetalleClaseActivity::class.java)
                    Toast.makeText(this, "Transaccion exitosa", Toast.LENGTH_SHORT).show()
                    val idBusqueda = clase.id.toString()
                    intentDetalleClase.putExtra("id_clase", idBusqueda);
                    val email = intent.getStringExtra("email")
                    val idClase = clase.id
                    val estadoClase = clase.estado
                    alertDialog.show()

                    if (isNetworkConnected(this)) {

                        var url = "https://webserviceasesoriasacademicas.000webhostapp.com/editar_clase.php?materia=$stringMateria&tema=$stringTema" +
                                "&inquietudes=$stringInquietudes&estado=$estadoClase&fecha=$stringFecha&hora=$stringHoraMinutos&duracion=$stringDuracion&precio=$stringPrecio&idClase=$idClase"
                        url = url.replace(" ","%20")
                        url = url.replace("#","%23")
                        url = url.replace("-","%2D")
                        url = url.replace("á","%C3%A1")
                        url = url.replace("é","%C3%A9")
                        url = url.replace("í","%C3%AD")
                        url = url.replace("ó","%C3%B3")
                        url = url.replace("ú","%C3%BA")
                        url = url.replace("°","%C2%B0")
                        println(url)
                        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null,
                                Response.Listener { response ->
                                    if (response.getString("success") == "1"){
                                        val fecha= intent.getStringExtra("fecha")
                                        intentDetalleClase.putExtra("email", email)
                                        intentDetalleClase.putExtra("fecha", fecha)
                                        alertDialog.dismiss()
                                        startActivity(intentDetalleClase)
                                    } else if(response.getString("error") == "0") {
                                        Toast.makeText(this, "\n" + "Ocurrió un error en la actualización de su clase!", Toast.LENGTH_SHORT).show()
                                        alertDialog.dismiss()
                                    }
                                },
                                Response.ErrorListener { error ->
                                    Toast.makeText(this, "\n" + "Ocurrió un error en la actualización de su clase!", Toast.LENGTH_SHORT).show()
                                    alertDialog.dismiss()
                                })
                        request?.add(jsonObjectRequest)

                    } else{
                        Toast.makeText(this, "Por favor verifica tu conexión a internet y vuelve a intentarlo!", Toast.LENGTH_LONG).show()
                        alertDialog.dismiss()
                    }
                } else {
                    Toast.makeText(this, "Transaccion fallida", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }
            }
        }

        val formatofecha: SimpleDateFormat = SimpleDateFormat("dd/mm/yyyy")
        val lanzadorFecha: ImageView = findViewById(R.id.img_fecha_editar_clase)
        fecha = findViewById(R.id.txt_fecha_editar_clase)
        lanzadorFecha.setOnClickListener{
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, anio, mes, dia ->
                val fechaCalendario = "" + dia + "/" + (mes + 1) + "/" + anio
                val date: Date  = formatofecha.parse(fechaCalendario)
                fecha?.setText(formatofecha.format(date))
            }, anio, mes, dia)
            datePickerDialog.show()
            autotextViewTema.clearFocus()
            fecha.requestFocus()
        }

        val lanzadorFechaFin: ImageView = findViewById(R.id.img_fecha_fin_editar_clase)
        fecha_fin = findViewById(R.id.txt_fecha_fin_editar_clase)
        lanzadorFechaFin.setOnClickListener{
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, anio, mes, dia ->
                val fechaCalendario = "" + dia + "/" + (mes + 1) + "/" + anio
                val date: Date  = formatofecha.parse(fechaCalendario)
                fecha_fin?.setText(formatofecha.format(date))
            }, anio, mes, dia)
            datePickerDialog.show()
        }

        val formatohora: SimpleDateFormat = SimpleDateFormat("h:mm")
        val lanzadorTiempo: ImageView = findViewById(R.id.img_hora_editar_clase)
        val horaMinutos = findViewById<TextInputEditText>(R.id.txt_hora_editar_clase)
        lanzadorTiempo.setOnClickListener{
            val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{ view, hora, minutos ->
                var am_pm = ""
                if (hora < 12){
                    am_pm = " AM"
                } else {
                    am_pm = " PM"
                }
                val horaReloj = "" + hora + ":" + minutos
                val date: Date  = formatohora.parse(horaReloj)
                horaMinutos?.setText(formatohora.format(date) + am_pm)
            }, hora, minutos, false)
            timePickerDialog.show()
            fecha.clearFocus()
            horaMinutos.requestFocus()
        }

        val btnCancelarEditarClase = findViewById<Button>(R.id.btn_cancelar_editar_clase)
        btnCancelarEditarClase.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cancelar edición de la clase")
            builder.setMessage("¿Seguro que deseas cancelar la edición de la clase?")
                    .setCancelable(false)
                    .setPositiveButton("Confirmar") { dialog, id ->
                        val intentCancelar = Intent(this, PopupDetalleClaseActivity::class.java)
                        val idBusqueda = clase.id.toString()
                        val email= intent.getStringExtra("email")
                        val fecha= intent.getStringExtra("fecha")
                        intentCancelar.putExtra("email", email)
                        intentCancelar.putExtra("id_clase", idBusqueda)
                        intentCancelar.putExtra("fecha", fecha)
                        startActivity(intentCancelar)
                    }
                    .setNegativeButton("Cancelar") { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }

        val btnDisminurDuracion = findViewById<ImageView>(R.id.img_disminuir_duracion_editar_clase)
        btnDisminurDuracion.setOnClickListener{
            if (duracion?.text.toString().isEmpty()){
                duracion?.setText("1")
            } else {
                val intDuracion = duracion?.text.toString().trim().toInt()
                val nuevaDuracion = intDuracion - 1
                if (nuevaDuracion >= 1) {
                    duracion?.setText(nuevaDuracion.toString())
                } else {
                    duracion?.setText("1")
                    Toast.makeText(this, "No es posible definir una clase menor a 1 hora", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val btnAumentarDuracion = findViewById<ImageView>(R.id.img_aumentar_duracion_editar_clase)
        btnAumentarDuracion.setOnClickListener{
            if (duracion?.text.toString().isEmpty()){
                duracion?.setText("1")
            } else {
                val intDuracion = duracion?.text.toString().trim().toInt()
                val nuevaDuracion = intDuracion + 1
                if(nuevaDuracion <= 6) {
                    duracion?.setText(nuevaDuracion.toString())
                } else {
                    duracion?.setText("6")
                    Toast.makeText(this, "No es posible definir una clase mayor a 6 horas", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cancelar edición de la clase")
        builder.setMessage("¿Seguro que deseas cancelar la edición de la clase?")
                .setCancelable(false)
                .setPositiveButton("Confirmar") { dialog, id ->
                    val intentLogout = Intent(this, PopupDetalleClaseActivity::class.java)
                    var idClase= intent.getStringExtra("id_clase")
                    val email= intent.getStringExtra("email")
                    val fecha= intent.getStringExtra("fecha")
                    intentLogout.putExtra("id_clase", idClase)
                    intentLogout.putExtra("email", email)
                    intentLogout.putExtra("fecha", fecha)
                    startActivity(intentLogout)
                }
                .setNegativeButton("Cancelar") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
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