package com.cs492.ringmanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.cs492.ringmanager.R
import com.cs492.ringmanager.data.LocationData
import com.google.android.material.snackbar.Snackbar

class LocationsFragment : Fragment(R.layout.locations_fragment) {

    private val viewModel: LocationsViewModel by viewModels()
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var locationListRV: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationAdapter = LocationAdapter(::onLocationItemClick)
        locationListRV = view.findViewById(R.id.rv_location_list)
        locationListRV.setHasFixedSize(true)
        locationListRV.adapter = locationAdapter
        setHasOptionsMenu(true)

        //Update the list of locations if data changes
        viewModel.savedLocations.observe(viewLifecycleOwner) { locations ->
            if (locations != null && !locations.isEmpty()) {
                locationAdapter.updateLocations(locations)
                locationListRV.visibility = View.VISIBLE
                locationListRV.scrollToPosition(0)
            }
        }

        val fab: View = view.findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            val directions = LocationsFragmentDirections.navigate
        }
    }


    private fun onLocationItemClick(location: LocationData) {
        viewModel.removeLocation(location)
    }

}