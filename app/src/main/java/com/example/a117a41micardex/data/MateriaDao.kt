
package com.example.a117a41micardex.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MateriaDao {

    // Consultar todas las materias
    @Query("SELECT * FROM materias ORDER BY semestre, nombre")
    fun obtenerMaterias(): Flow<List<Materia>>

    // Registrar una materia
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMateria(materia: Materia)

    // Eliminar una materia
    @Delete
    suspend fun eliminarMateria(materia: Materia)

    // Consultar una materia por su identificador
    @Query("SELECT * FROM materias WHERE id = :id")
    suspend fun obtenerMateriaPorId(id: Int): Materia?

    // Promedio general
    // Se considera el intento más reciente registrado:
    // Especial, si existe; de lo contrario, Repetición o Normal.
    @Query("""
        SELECT AVG(
            COALESCE(
                calificacionEspecial,
                calificacionRepeticion,
                calificacionNormal
            )
        )
        FROM materias
    """)
    suspend fun obtenerPromedioGeneral(): Double?

    // Promedio por semestre
    @Query("""
        SELECT AVG(
            COALESCE(
                calificacionEspecial,
                calificacionRepeticion,
                calificacionNormal
            )
        )
        FROM materias
        WHERE semestre = :semestre
    """)
    suspend fun obtenerPromedioPorSemestre(
        semestre: Int
    ): Double?


    // Consultar las materias de un semestre específico
    @Query("SELECT * FROM materias WHERE semestre = :semestre ORDER BY nombre")
    fun obtenerMateriasPorSemestre(semestre: Int): Flow<List<Materia>>

}
