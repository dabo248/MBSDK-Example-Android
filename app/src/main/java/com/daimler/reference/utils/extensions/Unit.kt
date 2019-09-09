package com.daimler.reference.utils.extensions

import android.content.Context
import com.daimler.mbcarkit.business.model.vehicle.unit.DistanceUnit
import com.daimler.mbcarkit.business.model.vehicle.unit.RatioUnit
import com.daimler.reference.R

fun RatioUnit.displayUnitAsString() =
    when (this) {
        RatioUnit.UNSPECIFIED_RATIO_UNIT -> ""
        RatioUnit.PERCENT -> "%"
    }

fun DistanceUnit.displayUnitAsString(context: Context) =
    when (this) {
        DistanceUnit.UNSPECIFIED_DISTANCE_UNIT -> ""
        DistanceUnit.KILOMETERS -> context.getString(R.string.unit_km)
        DistanceUnit.MILES -> context.getString(R.string.unit_miles)
    }