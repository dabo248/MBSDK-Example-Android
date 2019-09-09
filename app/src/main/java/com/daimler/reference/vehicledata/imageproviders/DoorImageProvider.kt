package com.daimler.reference.vehicledata.imageproviders

import androidx.annotation.DrawableRes
import android.util.SparseIntArray
import com.daimler.mbcarkit.business.model.vehicle.Door
import com.daimler.mbcarkit.business.model.vehicle.VehicleStatus
import com.daimler.reference.R
import com.daimler.reference.vehicledata.vehiclestatus.VehicleStateMachine

object DoorImageProvider : VehicleImageProvider {

    const val KEY_FRONT_LEFT = 0
    const val KEY_FRONT_RIGHT = 1
    const val KEY_BACK_LEFT = 2
    const val KEY_BACK_RIGHT = 3

    private const val AMOUNT_DOOR_STATES = 9
    private const val AMOUNT_DOORS = 4

    override fun getImages(status: VehicleStatus): SparseIntArray =
            SparseIntArray(AMOUNT_DOORS).apply {
                put(KEY_FRONT_LEFT, DoorImageHolder.FrontLeft.getImage(status.doors.frontLeft))
                put(KEY_FRONT_RIGHT, DoorImageHolder.FrontRight.getImage(status.doors.frontRight))
                put(KEY_BACK_LEFT, DoorImageHolder.BackLeft.getImage(status.doors.rearLeft))
                put(KEY_BACK_RIGHT, DoorImageHolder.BackRight.getImage(status.doors.rearRight))
            }

    /**
     * Holds images for different door status.
     *
     * @param ooRes open and unlocked
     * @param ocRes open and locked
     * @param ouRes open and unknown
     * @param coRes closed and unlocked
     * @param ccRes closed and locked
     * @param cuRes closed and unknown
     * @param uoRes unknown and unlocked
     * @param ucRes unknown and locked
     * @param uuRes unknown and unknown
     */
    private sealed class DoorImageHolder(
            @DrawableRes ooRes: Int,
            @DrawableRes ocRes: Int,
            @DrawableRes ouRes: Int,
            @DrawableRes coRes: Int,
            @DrawableRes ccRes: Int,
            @DrawableRes cuRes: Int,
            @DrawableRes uoRes: Int,
            @DrawableRes ucRes: Int,
            @DrawableRes uuRes: Int
    ) {

        private val imageMap = SparseIntArray(AMOUNT_DOOR_STATES).apply {
            put(flag(VehicleStateMachine.FLAG_DOOR_OPEN, VehicleStateMachine.FLAG_LOCK_UNLOCKED), ooRes)
            put(flag(VehicleStateMachine.FLAG_DOOR_OPEN, VehicleStateMachine.FLAG_LOCK_LOCKED), ocRes)
            put(flag(VehicleStateMachine.FLAG_DOOR_OPEN, VehicleStateMachine.FLAG_LOCK_UNKNOWN), ouRes)

            put(flag(VehicleStateMachine.FLAG_DOOR_CLOSED, VehicleStateMachine.FLAG_LOCK_UNLOCKED), coRes)
            put(flag(VehicleStateMachine.FLAG_DOOR_CLOSED, VehicleStateMachine.FLAG_LOCK_LOCKED), ccRes)
            put(flag(VehicleStateMachine.FLAG_DOOR_CLOSED, VehicleStateMachine.FLAG_LOCK_UNKNOWN), cuRes)

            put(flag(VehicleStateMachine.FLAG_DOOR_UNKNOWN, VehicleStateMachine.FLAG_LOCK_UNLOCKED), uoRes)
            put(flag(VehicleStateMachine.FLAG_DOOR_UNKNOWN, VehicleStateMachine.FLAG_LOCK_LOCKED), ucRes)
            put(flag(VehicleStateMachine.FLAG_DOOR_UNKNOWN, VehicleStateMachine.FLAG_LOCK_UNKNOWN), uuRes)
        }

        @DrawableRes
        fun getImage(door: Door) = imageMap[VehicleStateMachine.getDoorFlags(door)]

        private fun flag(vararg flags: Int) = flags.or()

        object FrontLeft : DoorImageHolder(
                R.drawable.dflor, R.drawable.dflog, R.drawable.dflo,
                R.drawable.dflcr, R.drawable.dflcg, R.drawable.dflc,
                R.drawable.dflcr, R.drawable.dflcg, R.drawable.dflc
        )

        object FrontRight : DoorImageHolder(
                R.drawable.dfror, R.drawable.dfrog, R.drawable.dfro,
                R.drawable.dfrcr, R.drawable.dfrcg, R.drawable.dfrc,
                R.drawable.dfrcr, R.drawable.dfrcg, R.drawable.dfrc
        )

        object BackLeft : DoorImageHolder(
                R.drawable.dblor, R.drawable.dblog, R.drawable.dblo,
                R.drawable.dblcr, R.drawable.dblcg, R.drawable.dblc,
                R.drawable.dblcr, R.drawable.dblcg, R.drawable.dblc
        )

        object BackRight : DoorImageHolder(
                R.drawable.dbror, R.drawable.dbrog, R.drawable.dbro,
                R.drawable.dbrcr, R.drawable.dbrcg, R.drawable.dbrc,
                R.drawable.dbrcr, R.drawable.dbrcg, R.drawable.dbrc
        )

        private fun IntArray.or(): Int {
            var result = 0
            forEach { result = result.or(it) }
            return result
        }
    }
}