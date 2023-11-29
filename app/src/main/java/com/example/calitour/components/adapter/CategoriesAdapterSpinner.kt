package com.example.calitour.components.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

class CategoriesAdapterSpinner(context: Context, resource: Int, objects: Array<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        if (position == 0) {
            // Cambia el color del texto para el primer elemento para que funcione como hint
            (view as TextView).setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
        }
        return view
    }

    override fun isEnabled(position: Int): Boolean {
        // Deshabilita la primera posicion que es el hint
        return position != 0
    }
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)

        if (position == 0) {
            // Cambia el color del texto para el primer elemento en la lista desplegable
            (view as TextView).setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
        }

        return view
    }
}
