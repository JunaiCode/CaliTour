package com.example.calitour.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.calitour.components.adapter.ProductItemAdapter
import com.example.calitour.databinding.EntityProductsFragmentBinding
import com.example.calitour.viewmodel.EntityViewModel

class ProductListFragment: Fragment() {

    private lateinit var adapter: ProductItemAdapter
    private val vm: EntityViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: EntityProductsFragmentBinding = EntityProductsFragmentBinding.inflate(inflater, container, false)
        adapter = ProductItemAdapter()
        binding.productsRV.adapter = adapter
        vm.products.observe(viewLifecycleOwner){products ->
            adapter.addAll(products)
        }
        vm.loadProducts()
        return binding.root
    }
}