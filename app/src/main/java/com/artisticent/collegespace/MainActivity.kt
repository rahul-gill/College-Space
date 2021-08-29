package com.artisticent.collegespace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.artisticent.collegespace.remote.apis.CodeforcesApi
import dagger.hilt.EntryPoint
import javax.inject.Inject


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


}