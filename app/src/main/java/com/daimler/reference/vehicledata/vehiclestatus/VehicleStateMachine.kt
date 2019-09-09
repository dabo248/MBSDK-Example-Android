package com.daimler.reference.vehicledata.vehiclestatus

import com.daimler.mbcarkit.business.model.vehicle.*

object VehicleStateMachine {

    const val FLAG_DOOR_OPEN = 0x00000004
    const val FLAG_DOOR_CLOSED = 0x00000002
    const val FLAG_DOOR_UNKNOWN = 0x00000001

    const val FLAG_LOCK_LOCKED = 0x000000040
    const val FLAG_LOCK_UNLOCKED = 0x000000020
    const val FLAG_LOCK_UNKNOWN = 0x000000010

    const val FLAG_CAR_LOCKED = 0x00000400
    const val FLAG_CAR_UNLOCKED = 0x00000200
    const val FLAG_CAR_UNKNOWN = 0x00000100

    const val FLAG_IGNITION_OFF = 0x40000000
    const val FLAG_IGNITION_ON = 0x20000000
    const val FLAG_IGNITION_LOCK = 0x10000000
    const val FLAG_IGNITION_START = 0x08000000
    const val FLAG_IGNITION_ACCESSORY = 0x04000000
    const val FLAG_IGNITION_UNKNOWN = 0x02000000

    fun getDoorFlags(door: Door) =
            getDoorStateFlag(door.state.value).or(getDoorLockStateFlag(door.lockStatus.value))

    fun getVehicleLockFlag(status: DoorLockState?) =
            when (status) {
                DoorLockState.UNLOCKED -> FLAG_CAR_UNLOCKED
                DoorLockState.LOCKED_INTERNAL -> FLAG_CAR_LOCKED
                DoorLockState.LOCKED_EXTERNAL -> FLAG_CAR_LOCKED
                DoorLockState.UNLOCKED_SELECTIVE -> FLAG_CAR_UNLOCKED
                else -> FLAG_CAR_UNKNOWN
            }

    fun getIgnitionFlag(status: IgnitionState?) =
            when (status) {
                IgnitionState.LOCK -> FLAG_IGNITION_LOCK
                IgnitionState.OFF -> FLAG_IGNITION_OFF
                IgnitionState.ACCESSORY -> FLAG_IGNITION_ACCESSORY
                IgnitionState.ON -> FLAG_IGNITION_ON
                IgnitionState.START -> FLAG_IGNITION_START
                else -> FLAG_IGNITION_UNKNOWN
            }

    private fun getDoorStateFlag(state: OpenStatus?) =
            when (state) {
                OpenStatus.CLOSED -> FLAG_DOOR_CLOSED
                OpenStatus.OPENED -> FLAG_DOOR_OPEN
                else -> FLAG_DOOR_UNKNOWN
            }

    private fun getDoorLockStateFlag(state: LockStatus?) =
            when (state) {
                LockStatus.LOCKED -> FLAG_LOCK_LOCKED
                LockStatus.UNLOCKED -> FLAG_LOCK_UNLOCKED
                LockStatus.UNKNOWN -> FLAG_LOCK_UNKNOWN
                else -> FLAG_LOCK_UNKNOWN
            }
}