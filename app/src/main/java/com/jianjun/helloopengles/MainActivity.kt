package com.jianjun.helloopengles

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jianjun.helloopengles.airhockey.AirHockeyActivity
import com.jianjun.helloopengles.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnTriangle.setOnClickListener(this)
        binding.btnAirHockey.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val intent = Intent()
        when (v) {
            binding.btnTriangle -> {
                intent.setClass(this, TriangleActivity::class.java)
            }
            binding.btnAirHockey -> {
                intent.setClass(this, AirHockeyActivity::class.java)
            }
            else -> return
        }
        startActivity(intent)
    }
}