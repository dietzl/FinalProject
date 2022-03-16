package com.cs492.ringmanager.ui

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cs492.ringmanager.R
import com.cs492.ringmanager.api.MovieGluService
import com.cs492.ringmanager.data.LocationDao
import com.cs492.ringmanager.data.LocationData
import com.cs492.ringmanager.data.LocationDatabase
import com.cs492.ringmanager.data.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.launch
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AddLocationFragment : Fragment(R.layout.add_locations_fragment){
    private val TAG = "AddLocationFragment"

    // Text boxes
    private lateinit var locationBoxET: EditText
    private lateinit var radiusBoxET: EditText

    // Location var
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationBoxET = view.findViewById(R.id.et_location_text)
        radiusBoxET = view.findViewById(R.id.et_radius_text)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext()) // Changed 'this' to requireContext

        val searchBtn: Button = view.findViewById(R.id.search_button)
        searchBtn.setOnClickListener {
            val locationName = locationBoxET.text.toString()
            val radiusString = radiusBoxET.text.toString()

            if(!TextUtils.isEmpty(locationName) && !(TextUtils.isEmpty(radiusString))) {
                val radiusFloat = radiusString.toFloat()
                if(radiusFloat >= 50) {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            val location = LocationData(
                                location!!.latitude,
                                location!!.longitude,
                                radiusFloat,
                                locationName
                            )
                            val repository = LocationRepository(
                                LocationDatabase.getInstance(requireContext()).locationDao(),
                                MovieGluService.create()
                            )
                            lifecycleScope.launch {
                                repository.addLocation(location)
                            }
                        }
                }
                else {
                    Snackbar.make(
                        requireView(),
                        R.string.invalid_user_value,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}