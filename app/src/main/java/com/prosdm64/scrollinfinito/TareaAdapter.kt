package com.prosdm64.scrollinfinito

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TareaAdapter(
    private val listaTareas: MutableList<Tarea>,
    private val onTickClick: (Int) -> Unit,
    private val onStarClick: (Int) -> Unit
) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tarea_layout, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = listaTareas[position]
        holder.tareaTextView.text = tarea.texto

        // Cambiar el icono según si es favorita o no
        holder.starImageView.setImageResource(if (tarea.esFavorita) R.drawable.bahai_solid_24 else R.drawable.bahai_24)

        // Listener para el tick
        holder.tickImageView.setOnClickListener {
            onTickClick(position)
        }

        // Listener para la estrella
        holder.starImageView.setOnClickListener {
            onStarClick(position)
        }
    }

    override fun getItemCount(): Int {
        return listaTareas.size
    }

    inner class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tareaTextView: TextView = itemView.findViewById(R.id.tareaTexto)
        val tickImageView: ImageView = itemView.findViewById(R.id.tick)
        val starImageView: ImageView = itemView.findViewById(R.id.bahai)
    }
}






