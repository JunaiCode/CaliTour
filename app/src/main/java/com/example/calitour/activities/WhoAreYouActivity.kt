package com.example.calitour.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calitour.R
import com.example.calitour.databinding.ActivityWhoAreYouBinding

class WhoAreYouActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWhoAreYouBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhoAreYouBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_who_are_you)
    }
}