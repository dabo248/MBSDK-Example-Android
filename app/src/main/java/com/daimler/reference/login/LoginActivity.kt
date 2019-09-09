package com.daimler.reference.login

import android.content.Context
import android.content.Intent
import com.daimler.reference.menu.MainActivity
import com.daimler.mbmobilesdk.login.MBLoginActivity

class LoginActivity : MBLoginActivity() {

    override fun onUserAuthenticated() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }

    companion object {

        fun getStartIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}