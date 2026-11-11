
package com.example.a117a41micardex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.a117a41micardex.data.CardexDatabase
import com.example.a117a41micardex.data.Materia
import com.example.a117a41micardex.data.MateriaDao
import com.example.a117a41micardex.data.PreferenciasAlumno
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferencias = PreferenciasAlumno(this)
        val dao = CardexDatabase.obtenerInstancia(this).materiaDao()

        setContent {
            MaterialTheme {
                PantallaCardex(preferencias, dao)
            }
        }
    }
}

@Composable
fun PantallaCardex(
    preferencias: PreferenciasAlumno,
    dao: MateriaDao
) {
    var nombreAlumno by remember {
        mutableStateOf(preferencias.obtenerNombre())
    }
    var numeroControl by remember {
        mutableStateOf(preferencias.obtenerNumeroControl())
    }

    var nombreMateria by remember { mutableStateOf("") }
    var semestreTexto by remember { mutableStateOf("") }
    var normalTexto by remember { mutableStateOf("") }
    var repeticionTexto by remember { mutableStateOf("") }
    var especialTexto by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    var semestreConsulta by remember { mutableStateOf("1") }
    var promedioGeneral by remember { mutableStateOf<Double?>(null) }
    var promedioSemestre by remember { mutableStateOf<Double?>(null) }
    var semestreSeleccionado by remember { mutableStateOf<Int?>(null) }

    val materias by dao.obtenerMaterias().collectAsState(
        initial = emptyList()
    )

    val flujoMateriasSemestre = remember(semestreSeleccionado) {
        semestreSeleccionado?.let {
            dao.obtenerMateriasPorSemestre(it)
        } ?: flowOf(emptyList())
    }

    val materiasDelSemestre by flujoMateriasSemestre.collectAsState(
        initial = emptyList()
    )

    val alcance = rememberCoroutineScope()

    LaunchedEffect(materias) {
        promedioGeneral = dao.obtenerPromedioGeneral()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Mi Cardex",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            "Datos del alumno",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = nombreAlumno,
            onValueChange = { nombreAlumno = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = numeroControl,
            onValueChange = { numeroControl = it },
            label = { Text("Número de control") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (
                    nombreAlumno.isNotBlank() &&
                    numeroControl.isNotBlank()
                ) {
                    preferencias.guardarDatos(
                        nombreAlumno.trim(),
                        numeroControl.trim()
                    )
                    mensaje = "Datos del alumno guardados"
                } else {
                    mensaje = "Completa los datos del alumno"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar datos del alumno")
        }

        HorizontalDivider()

        Text(
            "Registrar materia",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = nombreMateria,
            onValueChange = { nombreMateria = it },
            label = { Text("Nombre de la materia") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = semestreTexto,
            onValueChange = { semestreTexto = it },
            label = { Text("Número de semestre") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Text("Calificaciones (deja vacío si no aplica)")

        OutlinedTextField(
            value = normalTexto,
            onValueChange = { normalTexto = it },
            label = { Text("Curso Ordinario") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = repeticionTexto,
            onValueChange = { repeticionTexto = it },
            label = { Text("Repetición") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = especialTexto,
            onValueChange = { especialTexto = it },
            label = { Text("Especial") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val semestre = semestreTexto.toIntOrNull()
                val normal = normalTexto.toDoubleOrNull()
                val repeticion = repeticionTexto.toDoubleOrNull()
                val especial = especialTexto.toDoubleOrNull()

                val textosCalificacion = listOf(
                    normalTexto,
                    repeticionTexto,
                    especialTexto
                )

                val valoresCalificacion = listOf(
                    normal,
                    repeticion,
                    especial
                )

                val calificacionesValidas =
                    textosCalificacion.zip(valoresCalificacion).all {
                            (texto, valor) ->
                        texto.isBlank() ||
                                (valor != null && valor in 0.0..100.0)
                    }

                if (
                    nombreMateria.isBlank() ||
                    semestre == null ||
                    semestre < 1 ||
                    !calificacionesValidas
                ) {
                    mensaje =
                        "Revisa el nombre, semestre y calificaciones (0 a 100)"
                } else if (
                    normal == null &&
                    repeticion == null &&
                    especial == null
                ) {
                    mensaje = "Ingresa al menos una calificación"
                } else {
                    alcance.launch {
                        dao.insertarMateria(
                            Materia(
                                nombre = nombreMateria.trim(),
                                semestre = semestre,
                                calificacionNormal = normal,
                                calificacionRepeticion = repeticion,
                                calificacionEspecial = especial
                            )
                        )

                        nombreMateria = ""
                        semestreTexto = ""
                        normalTexto = ""
                        repeticionTexto = ""
                        especialTexto = ""
                        mensaje = "Materia guardada correctamente"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar materia")
        }

        if (mensaje.isNotBlank()) {
            Text(mensaje)
        }

        HorizontalDivider()

        Text(
            "Promedios académicos",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            "Promedio general: ${
                promedioGeneral?.let { "%.2f".format(it) }
                    ?: "Sin calificaciones"
            }"
        )

        OutlinedTextField(
            value = semestreConsulta,
            onValueChange = { semestreConsulta = it },
            label = { Text("Semestre a consultar") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val semestre = semestreConsulta.toIntOrNull()

                if (semestre == null || semestre < 1) {
                    mensaje = "Ingresa un número de semestre válido"
                    semestreSeleccionado = null
                    promedioSemestre = null
                } else {
                    alcance.launch {
                        promedioSemestre =
                            dao.obtenerPromedioPorSemestre(semestre)

                        semestreSeleccionado = semestre

                        mensaje = if (promedioSemestre == null) {
                            "Ese semestre no tiene calificaciones registradas"
                        } else {
                            "Consulta realizada para el semestre $semestre"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Consultar promedio por semestre")
        }

        if (semestreSeleccionado != null) {
            Text(
                "Resultados del semestre $semestreSeleccionado",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                "Promedio del semestre: ${
                    promedioSemestre?.let { "%.2f".format(it) }
                        ?: "Sin calificaciones"
                }"
            )

            if (materiasDelSemestre.isEmpty()) {
                Text("No hay materias registradas en este semestre.")
            } else {
                materiasDelSemestre.forEach { materia ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                materia.nombre,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                "Curso Ordinario: ${
                                    materia.calificacionNormal ?: "Sin calificación"
                                }"
                            )

                            Text(
                                "Repetición: ${
                                    materia.calificacionRepeticion
                                        ?: "Sin calificación"
                                }"
                            )

                            Text(
                                "Especial: ${
                                    materia.calificacionEspecial
                                        ?: "Sin calificación"
                                }"
                            )

                            val modalidad = when {
                                materia.calificacionEspecial != null ->
                                    "Especial"

                                materia.calificacionRepeticion != null ->
                                    "Repetición"

                                materia.calificacionNormal != null ->
                                    "Curso Ordinario"

                                else -> "Sin calificación"
                            }

                            Text(
                                "Modalidad considerada: $modalidad",
                                style = MaterialTheme.typography.labelLarge
                            )

                            TextButton(
                                onClick = {
                                    alcance.launch {
                                        dao.eliminarMateria(materia)
                                        mensaje = "Materia eliminada"
                                    }
                                }
                            ) {
                                Text("Eliminar materia")
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider()

        Text(
            "Historial académico completo",
            style = MaterialTheme.typography.titleLarge
        )

        if (materias.isEmpty()) {
            Text("Todavía no hay materias registradas.")
        } else {
            materias.forEach { materia ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            materia.nombre,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Semestre: ${materia.semestre}")
                        Text(
                            "Curso Ordinario: ${
                                materia.calificacionNormal ?: "—"
                            }"
                        )
                        Text(
                            "Repetición: ${
                                materia.calificacionRepeticion ?: "—"
                            }"
                        )
                        Text(
                            "Especial: ${
                                materia.calificacionEspecial ?: "—"
                            }"
                        )
                    }
                }
            }
        }
    }
}
