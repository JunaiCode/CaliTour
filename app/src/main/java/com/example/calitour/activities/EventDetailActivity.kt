package com.example.calitour.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calitour.databinding.ActivityEventDetailBinding

class EventDetailActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityEventDetailBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}