package com.faircop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu

abstract class Menu : BasicActivity(), Menu {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }
}
