package com.example.calitour.components.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.calitour.R
import com.example.calitour.components.views.ProductItemView
import com.example.calitour.model.entity.EntityProductFirestore

class ProductItemAdapter: Adapter<ProductItemView>() {

    private val products = ArrayList<EntityProductFirestore>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemView {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.product_item_component, null, false)
        return ProductItemView(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductItemView, position: Int) {
        val data = products[position]
        Glide.with(holder.itemView.context)
            .load(data.image)
            .into(holder.image)
    }


    fun addAll(item: List<EntityProductFirestore>){
        products.clear()
        products.addAll(item)
        notifyDataSetChanged()

    }
}
