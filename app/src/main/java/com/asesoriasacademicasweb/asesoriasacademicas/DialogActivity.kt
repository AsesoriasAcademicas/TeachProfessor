package com.asesoriasacademicasweb.asesoriasacademicas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity


@Suppress("DEPRECATION")
class DialogActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var builder = AlertDialog.Builder(this)
        val dialogView: View = View.inflate(this, R.layout.activity_dialog, null)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.show()

        val handler = Handler()
        handler.postDelayed({
            alertDialog.dismiss()
            val intentInsert = Intent(this, RegistrarseActivity::class.java)
            startActivity(intentInsert)
        }, 1000)
    }
}