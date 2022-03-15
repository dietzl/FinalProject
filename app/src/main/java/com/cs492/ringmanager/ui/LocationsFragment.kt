package com.cs492.ringmanager.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.cs492.ringmanager.R
import com.cs492.ringmanager.data.LocationData

class LocationsFragment : Fragment(R.layout.locations_fragment) {

    private lateinit var viewModel: LocationsViewModel
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
        viewModel.locations.observe(viewLifecycleOwner) { locations ->
            if (locations != null && !locations.isEmpty()) {
                locationAdapter.updateLocations(locations)
                locationListRV.visibility = View.VISIBLE
                locationListRV.scrollToPosition(0)
            }
        }
    }


    private fun onLocationItemClick(location: LocationData) {
        viewModel.removeLocation(location)
    }

}