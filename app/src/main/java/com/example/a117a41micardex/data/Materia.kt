
package com.example.a117a41micardex.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materias")
data class Materia(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val semestre: Int,
    val calificacionNormal: Double?,
    val calificacionRepeticion: Double?,
    val calificacionEspecial: Double?
)
