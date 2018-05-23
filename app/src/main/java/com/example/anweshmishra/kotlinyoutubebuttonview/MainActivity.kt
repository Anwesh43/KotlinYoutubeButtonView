package com.example.anweshmishra.kotlinyoutubebuttonview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.youtubebuttonview.YoutubeButtonView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        YoutubeButtonView.create(this)
    }
}
