package com.daimler.reference.vehicledata.imageproviders

import android.util.SparseIntArray
import com.daimler.reference.R
import com.daimler.reference.vehicledata.vehiclestatus.VehicleStateMachine
import com.daimler.mbcarkit.business.model.vehicle.VehicleStatus

object IgnitionImageProvider : VehicleImageProvider {

    const val KEY_IMAGE = 0

    override fun getImages(status: VehicleStatus): SparseIntArray {
        val state = VehicleStateMachine.getIgnitionFlag(status.engine.ignitionState.value)
        val res = when (state) {
            VehicleStateMachine.FLAG_IGNITION_OFF -> R.drawable.ignition_off
            VehicleStateMachine.FLAG_IGNITION_ON -> R.drawable.ignition_on
            VehicleStateMachine.FLAG_IGNITION_LOCK -> R.drawable.ignition_lock
            VehicleStateMachine.FLAG_IGNITION_START -> R.drawable.ignition_start
            VehicleStateMachine.FLAG_IGNITION_ACCESSORY -> R.drawable.ignition_accessory
            VehicleStateMachine.FLAG_IGNITION_UNKNOWN -> 0
            else -> 0
        }
        return SparseIntArray(1).apply { put(KEY_IMAGE, res) }
    }
}