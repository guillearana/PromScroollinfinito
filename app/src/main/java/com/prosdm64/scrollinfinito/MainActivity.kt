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
            { position ->
                listaTareas.removeAt(position)
                tareaAdapter.notifyItemRemoved(position)
                tareaAdapter.notifyItemRangeChanged(position, listaTareas.size)
            },
            { position ->
                val tarea = listaTareas[position]
                if (tarea.esFavorita) {
                    // Si es favorita, la desmarcamos y la devolvemos a su posición original
                    tarea.esFavorita = false
                    val nuevaPosicion = tarea.posicionOriginal
                    listaTareas.removeAt(position)
                    listaTareas.add(nuevaPosicion, tarea)
                    tareaAdapter.notifyItemMoved(position, nuevaPosicion)
                } else {
                    // Si no es favorita, la marcamos como favorita y la colocamos al principio
                    tarea.esFavorita = true
                    tarea.posicionOriginal = position // Guardar la posición original
                    listaTareas.removeAt(position)
                    listaTareas.add(0, tarea)
                    tareaAdapter.notifyItemMoved(position, 0)
                }
                tareaAdapter.notifyDataSetChanged()
            }
        )

        recyclerViewTareas.layoutManager = LinearLayoutManager(this)
        recyclerViewTareas.adapter = tareaAdapter

        btnAnadirTarea.setOnClickListener {
            agregarTarea()
        }
    }

    private fun agregarTarea() {
        val nuevaTareaTexto = tareaInput.text.toString()
        if (!TextUtils.isEmpty(nuevaTareaTexto)) {
            val nuevaTarea = Tarea(nuevaTareaTexto, false, listaTareas.size) // Al añadir, asignamos la posición inicial
            listaTareas.add(nuevaTarea)
            tareaAdapter.notifyItemInserted(listaTareas.size - 1)
            tareaInput.setText("")
        }
    }
}








