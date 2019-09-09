package com.daimler.reference.vehicledata.attributes

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.daimler.mbcarkit.business.model.vehicle.Tank
import com.daimler.mbcarkit.business.model.vehicle.VehicleAttribute
import com.daimler.mbcarkit.business.model.vehicle.unit.DistanceUnit
import com.daimler.mbcarkit.business.model.vehicle.unit.RatioUnit
import com.daimler.reference.R
import com.daimler.reference.utils.extensions.displayUnitAsString
import kotlinx.android.synthetic.main.view_tank_information.view.*

class TankInformationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_tank_information, this, true)
    }

    fun setTank(tank: Tank?) {
        view_liquid_range.updateDistanceValue(R.string.liquid_range, tank?.liquidRange)
        view_liquid_level.updateRatioValue(R.string.liquid_level, tank?.liquidLevel)

        view_gas_range.updateDistanceValue(R.string.gas_range, tank?.gasRange)
        view_gas_level.updateRatioValue(R.string.gas_level, tank?.gasLevel)

        view_electric_range.updateDistanceValue(R.string.electric_range, tank?.electricRange)
        view_electric_level.updateRatioValue(R.string.electric_level, tank?.electricLevel)

        view_adblue_level.updateRatioValue(R.string.adblue_level, tank?.adBlueLevel)
    }

    private fun VehicleAttributeView.updateRatioValue(
        @StringRes titleRes: Int,
        attribute: VehicleAttribute<*, RatioUnit>?
    ) {
        update(titleRes, attribute) {
            "${it.displayValue} ${it.displayUnit.displayUnitAsString()}"
        }
    }

    private fun VehicleAttributeView.updateDistanceValue(
        @StringRes titleRes: Int,
        attribute: VehicleAttribute<*, DistanceUnit>?
    ) {
        update(titleRes, attribute) {
            "${it.displayValue} ${it.displayUnit.displayUnitAsString(context)}"
        }
    }

    private fun <T : Enum<*>> VehicleAttributeView.update(
        @StringRes titleRes: Int,
        attribute: VehicleAttribute<*, T>?,
        formatter: (VehicleAttribute.Unit<T>) -> String
    ) {
        title = context.getString(titleRes)
        content = when {
            attribute == null -> "-"
            attribute.value == null -> "-"
            attribute.unit == null -> "${attribute.value}"
            else -> attribute.unit?.let {
                formatter.invoke(it)
            } ?: "-"
        }
    }
}