package com.daimler.reference.vehicledata.vehiclestatus

import android.content.Context
import androidx.annotation.DrawableRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.daimler.mbcarkit.business.model.vehicle.VehicleStatus
import com.daimler.reference.R
import com.daimler.reference.vehicledata.imageproviders.DoorImageProvider
import com.daimler.reference.vehicledata.imageproviders.IgnitionImageProvider
import com.daimler.reference.vehicledata.imageproviders.VehicleLockImageProvider
import kotlinx.android.synthetic.main.view_vehicle_status.view.*

class VehicleStatusView : FrameLayout {

    private var carSet = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_vehicle_status, this, true)
    }

    fun setVehicleStatus(status: VehicleStatus?) {
        applyCarImage(image_car)
        if (status != null) {
            applyDoors(image_door_front_left, image_door_front_right,
                    image_door_back_left, image_door_back_right, status)
            applyLockState(image_outline, image_lock, status, VehicleLockImageProvider)
            applyIgnitionState(image_ignition, status)
        }
    }

    private fun applyCarImage(view: ImageView) {
        if (!carSet) {
            view.setImageResource(R.drawable.cnd)
            carSet = true
        }
    }

    private fun applyDoors(
            frontLeft: ImageView,
            frontRight: ImageView,
            backLeft: ImageView,
            backRight: ImageView,
            status: VehicleStatus) {
        val images = DoorImageProvider.getImages(status)
        frontLeft.setOrClear(images.get(DoorImageProvider.KEY_FRONT_LEFT))
        frontRight.setOrClear(images.get(DoorImageProvider.KEY_FRONT_RIGHT))
        backLeft.setOrClear(images.get(DoorImageProvider.KEY_BACK_LEFT))
        backRight.setOrClear(images.get(DoorImageProvider.KEY_BACK_RIGHT))
    }

    private fun applyLockState(
            outlineView: ImageView,
            lockView: ImageView,
            state: VehicleStatus,
            provider: VehicleLockImageProvider
    ) {
        val images = provider.getImages(state)
        outlineView.setOrClear(images.get(VehicleLockImageProvider.KEY_OUTLINE))
        lockView.setOrClear(images.get(VehicleLockImageProvider.KEY_LOCK))
    }

    private fun applyIgnitionState(view: ImageView, status: VehicleStatus) {
        val images = IgnitionImageProvider.getImages(status)
        view.setOrClear(images.get(IgnitionImageProvider.KEY_IMAGE))
    }

    private fun ImageView.setOrClear(@DrawableRes res: Int) =
            if (res != 0) setImageResource(res) else setImageDrawable(null)
}