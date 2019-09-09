package com.daimler.reference.utils.extensions

import androidx.fragment.app.Fragment
import com.daimler.mbuikit.components.dialogfragments.MBDialogFragment
import com.daimler.reference.R

fun Fragment.showSimpleErrorDialog(msg: String) {
    activity?.let {
        MBDialogFragment.Builder().apply {
            withMessage(msg)
            withPositiveButton(getString(R.string.general_ok))
        }.build().show(it.supportFragmentManager, null)
    }
}