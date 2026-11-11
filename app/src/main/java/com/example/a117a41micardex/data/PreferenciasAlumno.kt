
package com.example.a117a41micardex.data

import android.content.Context

class PreferenciasAlumno(context: Context) {

    private val preferencias = context.getSharedPreferences(
        "datos_alumno",
        Context.MODE_PRIVATE
    )

    fun guardarDatos(nombre: String, numeroControl: String) {
        preferencias.edit()
            .putString("nombre", nombre)
            .putString("numero_control", numeroControl)
            .apply()
    }

    fun obtenerNombre(): String {
        return preferencias.getString("nombre", "") ?: ""
    }

    fun obtenerNumeroControl(): String {
        return preferencias.getString("numero_control", "") ?: ""
    }
}
