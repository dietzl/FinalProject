package com.cs492.ringmanager.ui

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs492.ringmanager.R
import com.cs492.ringmanager.data.LocationData

class LocationsFragment : Fragment(R.layout.locations_fragment) {

    private val viewModel: LocationsViewModel by viewModels()
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var locationListRV: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationAdapter = LocationAdapter(::onLocationItemClick)
        locationListRV = view.findViewById(R.id.rv_location_list)
        locationListRV.layoutManager = LinearLayoutManager(requireContext())
        locationListRV.setHasFixedSize(true)
        locationListRV.adapter = locationAdapter
        setHasOptionsMenu(true)

        //Update the list of locations if data changes
        viewModel.savedLocations.observe(viewLifecycleOwner) { locations ->
            locationAdapter.updateLocations(locations)
        }

        val fab: View = view.findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            val directions = LocationsFragmentDirections.navigateToAddLocationFrag()
            findNavController().navigate(directions)
        }
    }


    private fun onLocationItemClick(location: LocationData) {
        viewModel.removeLocation(location)
    }

}