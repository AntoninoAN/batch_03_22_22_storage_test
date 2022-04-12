package com.example.storagetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storagetest.view.UserCreationFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, UserCreationFragment())
            .commit()

    }

}