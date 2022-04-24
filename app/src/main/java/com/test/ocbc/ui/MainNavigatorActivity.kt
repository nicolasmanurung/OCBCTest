package com.test.ocbc.ui

import android.content.Intent
import android.view.LayoutInflater
import androidx.fragment.app.commit
import com.test.ocbc.R
import com.test.ocbc.base.BaseActivityBinding
import com.test.ocbc.databinding.ActivityMainBinding
import com.test.ocbc.ui.dashboard.DashboardActivity
import com.test.ocbc.ui.login.LoginFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainNavigatorActivity : BaseActivityBinding<ActivityMainBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate

    override fun setupView(binding: ActivityMainBinding) {
        checkIsLoginValid()

        supportFragmentManager.commit {
            replace(R.id.fragmentContainerMain, LoginFragment(), "LoginFragment")
            setReorderingAllowed(true)
        }
    }

    private fun checkIsLoginValid() = CoroutineScope(Dispatchers.Main).launch {
        val isUserLogin = prefs.isUserLogin.firstOrNull()
        if (isUserLogin == true) {
            startActivity(
                Intent(
                    binding.root.context,
                    DashboardActivity::class.java
                )
            )
            finish()
        }
    }
}