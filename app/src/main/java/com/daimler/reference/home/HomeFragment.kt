package com.daimler.reference.home

import androidx.databinding.ViewDataBinding
import com.daimler.mbmobilesdk.menu.MBMobileSDKHomeFragment
import com.daimler.mbmobilesdk.utils.extensions.createAndroidViewModel
import com.daimler.mbuikit.components.fragments.MBBaseMenuFragment
import com.daimler.mbuikit.lifecycle.events.LiveEventObserver
import com.daimler.reference.BR
import com.daimler.reference.R
import com.daimler.reference.utils.extensions.showSimpleErrorDialog

class HomeFragment : MBBaseMenuFragment<HomeViewModel>(), MBMobileSDKHomeFragment {

    override fun createViewModel(): HomeViewModel = createAndroidViewModel(HomeViewModel::class.java)

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun getModelId(): Int = BR.model

    override fun getToolbarTitleRes(): Int = R.string.home_title

    override fun onBindingCreated(binding: ViewDataBinding) {
        super.onBindingCreated(binding)

        viewModel.commandSendError.observe(this, onCommandSendErrorEvent())
    }

    fun onVehicleSelected(finOrVin: String) {
        viewModel.vehicleSelected(finOrVin)
    }

    fun onVehicleDeleted(finOrVin: String) {
        viewModel.vehicleDeleted(finOrVin)
    }

    private fun onCommandSendErrorEvent() = LiveEventObserver<String> {
        showSimpleErrorDialog(it)
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }
}