package com.example.calitour.components.views

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.calitour.databinding.ProductItemComponentBinding

class ProductItemView(root:View): ViewHolder(root) {
    val binding = ProductItemComponentBinding.bind(root)
    val image = binding.productItemIV
}