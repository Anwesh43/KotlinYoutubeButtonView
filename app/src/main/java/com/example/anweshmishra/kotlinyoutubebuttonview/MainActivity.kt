package com.example.anweshmishra.kotlinyoutubebuttonview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.youtubebuttonview.YoutubeButtonView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view : YoutubeButtonView = YoutubeButtonView.create(this)
        view.addYoutubeButtonListener {
            Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show()
        }
        fullScreen()
    }
}

fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}