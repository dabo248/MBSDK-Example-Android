package com.daimler.reference.vehicledata.attributes

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.daimler.reference.R
import kotlinx.android.synthetic.main.view_vehicle_attribute.view.*

class VehicleAttributeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var title: String? = null
        set(value) {
            field = value
            tv_title.text = value
        }

    var content: String? = null
        set(value) {
            field = value
            tv_content.text = content?.let { it } ?: "-"
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_vehicle_attribute, this, true)
    }
}