package com.nokhyun.reactive

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.nokhyun.reactive.databinding.ActivityMainBinding

fun Any.logger(log: () -> Any?) {
    Log.e("Log", log().toString())
}

class MainActivity : AppCompatActivity() {

    private val rxKotlinExam: IExam = RxKotlinExam()
    private val rxJavaExam: IExam = RxJavaExam()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this@MainActivity
        setContentView(binding.root)
        lifecycle.addObserver(rxKotlinExam)
        lifecycle.addObserver(rxJavaExam)

        binding.btnRxJava.setOnClickListener {
            rxJavaExam.start()
        }

        binding.btnRxKotlin.setOnClickListener {
            rxKotlinExam.start()
        }
    }

    override fun onDestroy() {
        lifecycle.removeObserver(rxKotlinExam)
        lifecycle.removeObserver(rxJavaExam)
        super.onDestroy()
    }
}