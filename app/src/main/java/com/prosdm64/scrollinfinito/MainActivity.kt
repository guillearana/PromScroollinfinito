package com.prosdm64.scrollinfinito

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var listaTareas: MutableList<Tarea>
    private lateinit var tareaAdapter: TareaAdapter
    private lateinit var tareaInput: EditText
    private lateinit var btnAnadirTarea: Button
    private lateinit var recyclerViewTareas: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listaTareas = mutableListOf()

        tareaInput = findViewById(R.id.tareaInput)
        btnAnadirTarea = findViewById(R.id.btnAnadirTarea)
        recyclerViewTareas = findViewById(R.id.recyclerViewTareas)

        tareaAdapter = TareaAdapter(
            listaTareas,
            { position -> // Manejo del clic en el tick
                listaTareas.removeAt(position)
                tareaAdapter.notifyItemRemoved(position)
                tareaAdapter.notifyItemRangeChanged(position, listaTareas.size)
            },
            { position -> // Manejo del clic en la estrella
                val tarea = listaTareas[position]
                if (tarea.esFavorita) {
                    // Si ya es favorita, la quitamos
                    tarea.esFavorita = false
                    // Volver a añadir a la lista sin moverla
                    tareaAdapter.notifyItemChanged(position)
                } else {
                    // Si no es favorita, la marcamos como favorita
                    tarea.esFavorita = true
                    listaTareas.removeAt(position) // Remover de la posición actual
                    listaTareas.add(0, tarea) // Agregar como favorita al inicio
                    tareaAdapter.notifyItemMoved(position, 0) // Notificar el movimiento
                    tareaAdapter.notifyItemChanged(0) // Notificar que el primer item ha cambiado
                }
            }
        )

        recyclerViewTareas.layoutManager = LinearLayoutManager(this)
        recyclerViewTareas.adapter = tareaAdapter

        btnAnadirTarea.setOnClickListener {
            agregarTarea()
        }
    }

    private fun agregarTarea() {
        val nuevaTarea = tareaInput.text.toString()
        if (!TextUtils.isEmpty(nuevaTarea)) {
            listaTareas.add(Tarea(nuevaTarea)) // Agregar tarea como objeto Tarea
            tareaAdapter.notifyItemInserted(listaTareas.size - 1)
            tareaInput.setText("")
        }
    }
}







