package com.faircop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faircop.model.ApiServices
import com.faircop.model.WindowAdapter
import com.faircop.model.WindowService


class WindowActivity : BasicActivity()  {
    val windowService = WindowService() // we instantiate service created in first chapter of this lesson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window)

        val recyclerView = findViewById<RecyclerView>(R.id.list_windows) // we find the recycler view defined in layout by its id list_windows
        val adapter = WindowAdapter() // adapter is created and recycler view properties are defined

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        //adapter.update(windowService.findAll()) // on the last step we update adapter data
        runCatching { ApiServices().windowsApiService.findAll().execute() } // method execute run a synchronous call
            .onSuccess { adapter.update(it.body() ?: emptyList()) }  // we use runCatching to manage successes and failures. On success we update adapter with the result contained in body property. If this response is null the list is empty
            .onFailure {
                Toast.makeText(this, "Error on windows loading $it", Toast.LENGTH_LONG).show()  // on error we display a message in a Toast notation
            }
    }

    fun onWindowSelected(id: Long) {
        val intent = Intent(this, WindowActivity::class.java).putExtra(WINDOW_NAME_PARAM, id)
        startActivity(intent)
    }

    fun lifecycleScope.launch(context = Dispatchers.IO) { // method lifecycleScope.launch open a new directive. You must specify a context other than Dispatchers.Main (Main thread) for the code to be executed. Dispatchers.IO is dedicated to Input/Output tasks
        runCatching { ApiServices().windowsApiService.findAll().execute() } // you can call retrofit to read data
            .onSuccess {
                withContext(context = Dispatchers.Main) { // You cant' display something (result in list, error in toast notification) outside the main thread. withContext helps to reattach your code to another thread
                    adapter.update(it.body() ?: emptyList())
                }
            }
            .onFailure {
                withContext(context = Dispatchers.Main) { // You cant' display something (result in list, error in toast notification) outside the main thread. withContext helps to reattach your code to another thread
                    Toast.makeText(
                        applicationContext,
                        "Error on windows loading $it",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }


    val id = intent.getLongExtra(WINDOW_NAME_PARAM, 0)
    val window = windowService.findById(id)

    if (window != null) {
        findViewById<TextView>(R.id.txt_window_name).text = window.name
        findViewById<TextView>(R.id.txt_room_name).text = window.room.name
        findViewById<TextView>(R.id.txt_window_current_temperature).text = window.room.currentTemperature?.toString()
        findViewById<TextView>(R.id.txt_window_target_temperature).text = window.room.targetTemperature?.toString()
        findViewById<TextView>(R.id.txt_window_status).text = window.status.toString()
    }

