package com.prosdm64.scrollinfinito

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    // Declaración de variables que se inicializan más adelante
    private lateinit var listaTareas: MutableList<Tarea> // Lista de tareas
    private lateinit var tareaAdapter: TareaAdapter // Adaptador para el RecyclerView
    private lateinit var tareaInput: EditText // Campo de entrada de texto
    private lateinit var btnAnadirTarea: Button // Botón para añadir tareas
    private lateinit var recyclerViewTareas: RecyclerView // RecyclerView para mostrar la lista de tareas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Asocia el layout principal con esta actividad

        // Inicializamos la lista de tareas
        listaTareas = mutableListOf()

        // Enlazamos las vistas con los IDs del layout XML
        tareaInput = findViewById(R.id.tareaInput)
        btnAnadirTarea = findViewById(R.id.btnAnadirTarea)
        recyclerViewTareas = findViewById(R.id.recyclerViewTareas)

        // Inicialización del adaptador con las acciones de borrado y marcación de favoritas
        tareaAdapter = TareaAdapter(
            listaTareas,
            { position -> // Función para eliminar una tarea
                listaTareas.removeAt(position) // Eliminamos la tarea de la lista
                tareaAdapter.notifyItemRemoved(position) // Notificamos la eliminación
                tareaAdapter.notifyItemRangeChanged(position, listaTareas.size) // Actualizamos el rango
            },
            { position -> // Función para marcar o desmarcar una tarea como favorita
                val tarea = listaTareas[position]
                if (tarea.esFavorita) {
                    // Si la tarea ya es favorita, la desmarcamos y la devolvemos a su posición original
                    tarea.esFavorita = false
                    val nuevaPosicion = tarea.posicionOriginal
                    listaTareas.removeAt(position)
                    listaTareas.add(nuevaPosicion, tarea) // La movemos a su posición original
                    tareaAdapter.notifyItemMoved(position, nuevaPosicion) // Notificamos el movimiento
                } else {
                    // Si la tarea no es favorita, la marcamos como favorita y la movemos al principio de la lista
                    tarea.esFavorita = true
                    tarea.posicionOriginal = position // Guardamos la posición actual como original
                    listaTareas.removeAt(position)
                    listaTareas.add(0, tarea) // Movemos la tarea al principio de la lista
                    tareaAdapter.notifyItemMoved(position, 0) // Notificamos el movimiento
                }
                tareaAdapter.notifyDataSetChanged() // Notificamos que el conjunto de datos ha cambiado
            }
        )

        // Configuración del RecyclerView
        recyclerViewTareas.layoutManager = LinearLayoutManager(this) // Configuramos el layout lineal
        recyclerViewTareas.adapter = tareaAdapter // Asociamos el adaptador al RecyclerView

        // Listener para el botón de añadir tarea
        btnAnadirTarea.setOnClickListener {
            agregarTarea() // Llamada a la función para añadir una tarea
        }
    }

    // Función para agregar una nueva tarea a la lista
    private fun agregarTarea() {
        val nuevaTareaTexto = tareaInput.text.toString() // Obtenemos el texto del input
        if (!TextUtils.isEmpty(nuevaTareaTexto)) { // Verificamos que no esté vacío
            val nuevaTarea = Tarea(nuevaTareaTexto, false, listaTareas.size) // Creamos una nueva tarea con la posición inicial
            listaTareas.add(nuevaTarea) // Añadimos la nueva tarea a la lista
            tareaAdapter.notifyItemInserted(listaTareas.size - 1) // Notificamos la inserción al adaptador
            tareaInput.setText("") // Limpiamos el campo de texto
        }
    }
}
