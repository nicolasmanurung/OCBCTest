package com.test.ocbc.ui

import android.view.LayoutInflater
import com.test.ocbc.R
import com.test.ocbc.base.BaseActivityBinding
import com.test.ocbc.databinding.ActivityMainBinding
import com.test.ocbc.ui.dashboard.DashboardFragment
import com.test.ocbc.ui.login.LoginFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainNavigatorActivity : BaseActivityBinding<ActivityMainBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate

    override fun setupView(binding: ActivityMainBinding) {
        checkIsLoginValid()
    }

    private fun checkIsLoginValid() = CoroutineScope(Dispatchers.IO).launch {
        prefs.isUserLogin.collect {
            if (it == true) {
                withContext(Dispatchers.Main) {
                    if (!supportFragmentManager.isDestroyed) {
                        supportFragmentManager.beginTransaction().replace(
                            R.id.fragmentContainerMain,
                            DashboardFragment(),
                            "DashboardFragment"
                        ).commit()
                    }
                }
            }

            if (it == false) {
                withContext(Dispatchers.Main) {
                    if (!supportFragmentManager.isDestroyed) {
                        supportFragmentManager.beginTransaction().apply {
                            replace(
                                R.id.fragmentContainerMain,
                                LoginFragment(),
                                "LoginFragment"
                            )
                        }.commit()
                    }
                }
            }
        }
    }
}