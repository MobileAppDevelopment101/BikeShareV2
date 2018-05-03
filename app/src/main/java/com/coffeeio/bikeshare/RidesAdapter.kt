package com.coffeeio.bikeshare

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.ride_list_item.view.*

class RidesAdapter(val users : ArrayList<String>, val times : ArrayList<String>, val durations : ArrayList<String>, val costs : ArrayList<String>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return users.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.ride_list_item, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.rideUser?.text = users.get(position)
        holder?.rideTime?.text = times.get(position)
        holder?.rideDuration?.text = durations.get(position)
        holder?.rideCost?.text = costs.get(position)
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val rideUser = view.ride_user
    val rideTime = view.ride_time
    val rideDuration = view.ride_duration
    val rideCost = view.ride_cost
}