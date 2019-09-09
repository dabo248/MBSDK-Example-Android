package com.daimler.reference.menu

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.daimler.mbmobilesdk.app.MBMobileSDK
import com.daimler.mbmobilesdk.menu.LoginMenuProvider
import com.daimler.mbmobilesdk.menu.MBMobileSDKActivity
import com.daimler.mbmobilesdk.utils.extensions.newLine
import com.daimler.mbcarkit.business.model.vehicle.VehicleInfo
import com.daimler.mbuikit.menu.MBMenuItem
import com.daimler.reference.BuildConfig
import com.daimler.reference.home.HomeFragment
import com.daimler.reference.login.LoginActivity

class MainActivity : MBMobileSDKActivity() {

    override fun getContentFragment(): Fragment? = HomeFragment.newInstance()

    override fun getInitialMenuItem(): MBMenuItem? = LoginMenuProvider.HOME

    override fun getFooterText(): String =
        StringBuilder().apply {
            append("Version ${BuildConfig.VERSION_NAME}, \"Build ${BuildConfig.VERSION_CODE}")
            newLine()
            append("${MBMobileSDK.selectedRegion()}-${MBMobileSDK.selectedStage()}")
        }.toString()

    override fun onLogout(success: Boolean) {
        startActivity(LoginActivity.getStartIntent(this))
        finish()
    }

    override fun onVehicleSelected(vehicle: VehicleInfo) {
        super.onVehicleSelected(vehicle)
        contentFragmentAs<HomeFragment>()?.onVehicleSelected(vehicle.finOrVin)
    }

    override fun onVehicleDeleted(finOrVin: String) {
        super.onVehicleDeleted(finOrVin)
        contentFragmentAs<HomeFragment>()?.onVehicleDeleted(finOrVin)
    }

    override fun handleMenuItemClick(item: MBMenuItem, alreadySelected: Boolean): Boolean {
        return when {
            super.handleMenuItemClick(item, alreadySelected) -> true
            item == LoginMenuProvider.HOME -> {
                closeDrawer {
                    replaceContentFragment(HomeFragment.newInstance())
                }
                true
            }
            else -> false
        }
    }

    private inline fun <reified T : Fragment> contentFragmentAs(): T? =
        currentContentFragment() as? T

    companion object {

        fun getStartIntent(context: Context): Intent =
            Intent(context, MainActivity::class.java)
    }
}