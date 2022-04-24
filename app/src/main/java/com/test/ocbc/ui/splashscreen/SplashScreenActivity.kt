package com.test.ocbc.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import com.test.ocbc.base.BaseActivityBinding
import com.test.ocbc.databinding.ActivitySplashScreenBinding
import com.test.ocbc.ui.MainNavigatorActivity
import com.test.ocbc.ui.dashboard.DashboardActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivityBinding<ActivitySplashScreenBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivitySplashScreenBinding =
        ActivitySplashScreenBinding::inflate

    override fun setupView(binding: ActivitySplashScreenBinding) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            val isLogin = prefs.isUserLogin.firstOrNull()
            if (isLogin == true) {
                startActivity(Intent(binding.root.context, DashboardActivity::class.java))
                finish()
            } else {
                startActivity(Intent(binding.root.context, MainNavigatorActivity::class.java))
                finish()
            }
        }
    }
}