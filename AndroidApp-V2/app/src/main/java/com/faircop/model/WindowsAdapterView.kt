package com.faircop.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.faircop.R

class WindowAdapter : RecyclerView.Adapter<WindowAdapter.WindowViewHolder>() { // an adapter must implement RecyclerView.Adapter wich manage a RecyclerView.ViewHolder

    inner class WindowViewHolder(view: View) : RecyclerView.ViewHolder(view) { // we create a WindowViewHolder which is able to hold fields defined in layout activity_windows_item.xml. When you scroll through the list view, system does not recreate these fields. It will update the values via method "onBindViewHolder"
        val name: TextView = view.findViewById(R.id.txt_window_name)
        val room: TextView = view.findViewById(R.id.txt_window_room)
        val status: TextView = view.findViewById(R.id.txt_status)
    }

    private val items = mutableListOf<WindowDto>() // adapter has a mutable list to store elements to display

    fun update(windows: List<WindowDto>) {  // method used to update the list content. This method will be called when data will be ready
        items.clear()
        items.addAll(windows)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size // RecyclerView.Adapter abstract class asks you to implement a first method that returns the number of records

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WindowViewHolder { // RecyclerView.Adapter abstract class asks you to implement a second method used to initialize a ViewHolder
        val view = LayoutInflater.from(parent.context)                                    // we inflate activity_windows_item.xml layout
            .inflate(R.layout.activity_windows_item, parent, false)          // we send it to ViewHolder constructor
        return WindowViewHolder(view)
    }


    override fun onBindViewHolder(holder: WindowViewHolder, position: Int) { // RecyclerView.Adapter abstract class asks you to implement a last method to define what to do when position in the list changes
        val window = items[position]
        holder.apply {
            name.text = window.name
            status.text = window.status.toString()
            room.text = window.room.name
            //itemView.setOnClickListener { listener.onWindowSelected(window.id) } // listener is called when someone clicks on an item
        }
    }

    override fun onViewRecycled(holder: WindowViewHolder) { // it’s very important to clear OnClickListener when a view holder is recycled to prevent memory leaks
        super.onViewRecycled(holder)
        holder.apply {
            itemView.setOnClickListener(null)
        }

    }

}