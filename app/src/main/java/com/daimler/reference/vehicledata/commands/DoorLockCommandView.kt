package com.daimler.reference.vehicledata.commands

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.daimler.reference.R
import kotlinx.android.synthetic.main.view_door_lock_command.view.*

class DoorLockCommandView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var doorActionListener: DoorActionListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_door_lock_command, this, true)

        setCommandData(null)

        btn_lock.setOnClickListener {
            doorActionListener?.onLockDoors()
        }
    }

    fun setCommandData(data: CommandStatusData?) {
        tv_error_content.text = data?.error ?: "-"
        tv_state_content.text = data?.state ?: "-"
        tv_process_id_content.text = data?.processId ?: "-"
        tv_timestamp_content.text = data?.timestamp ?: "-"
    }

    fun setProcessing(processing: Boolean) {
        progress.visibility = if (processing) VISIBLE else GONE
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        btn_lock.isEnabled = enabled
    }

    interface DoorActionListener {
        fun onLockDoors()
    }
}