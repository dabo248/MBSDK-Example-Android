package com.daimler.reference.vehicledata.imageproviders

import android.util.SparseIntArray
import com.daimler.mbcarkit.business.model.vehicle.VehicleStatus

interface VehicleImageProvider {

    fun getImages(status: VehicleStatus): SparseIntArray
}