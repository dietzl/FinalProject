package com.cs492.ringmanager.ui

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cs492.ringmanager.R
import com.cs492.ringmanager.data.LocationData

class LocationAdapter(private val onClick: (LocationData) -> Unit)
    : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    var locationsList = listOf<LocationData>()

    fun updateLocations(locations: List<LocationData>){
        locationsList = locations
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_list_item, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this.locationsList[position])
    }

    override fun getItemCount(): Int {
        return locationsList.size
    }

    class ViewHolder(val locationView: View, val onClick: (LocationData) -> Unit)
        : RecyclerView.ViewHolder(locationView){
        private val locationTV: TextView = locationView.findViewById(R.id.tv_location_name)
        private var currentLocation: LocationData? = null

        init {
            locationView.setOnClickListener{
                currentLocation?.let(onClick)
            }
        }

        fun bind(location: LocationData){
            currentLocation = location
            locationTV.text = location.name
        }
    }
}