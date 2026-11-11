
package com.example.a117a41micardex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Materia::class],
    version = 1,
    exportSchema = false
)
abstract class CardexDatabase : RoomDatabase() {

    abstract fun materiaDao(): MateriaDao

    companion object {

        @Volatile
        private var INSTANCIA: CardexDatabase? = null

        fun obtenerInstancia(context: Context): CardexDatabase {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    CardexDatabase::class.java,
                    "cardex_database"
                ).build()

                INSTANCIA = instancia
                instancia
            }
        }
    }
}
