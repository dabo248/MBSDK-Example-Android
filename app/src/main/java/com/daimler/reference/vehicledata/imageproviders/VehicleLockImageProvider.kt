package com.daimler.reference.vehicledata.imageproviders

import android.util.SparseIntArray
import com.daimler.reference.R
import com.daimler.reference.vehicledata.vehiclestatus.VehicleStateMachine
import com.daimler.mbcarkit.business.model.vehicle.VehicleStatus

object VehicleLockImageProvider : VehicleImageProvider {

    const val KEY_OUTLINE = 0
    const val KEY_LOCK = 1

    private const val AMOUNT_IMAGES = 2

    override fun getImages(status: VehicleStatus): SparseIntArray {
        val array = SparseIntArray(AMOUNT_IMAGES)
        val state = VehicleStateMachine.getVehicleLockFlag(status.vehicle.doorLockState.value)
        val images = getImageResources(state)
        array.put(KEY_OUTLINE, images.first)
        array.put(KEY_LOCK, images.second)
        return array
    }

    private fun getImageResources(state: Int) =
            when (state) {
                VehicleStateMachine.FLAG_CAR_LOCKED -> R.drawable.green_outline to R.drawable.lock_green
                VehicleStateMachine.FLAG_CAR_UNLOCKED -> R.drawable.red_outline to R.drawable.lock_red
                VehicleStateMachine.FLAG_CAR_UNKNOWN -> 0 to 0
                else -> Pair(0, 0)
            }
}