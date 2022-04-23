package com.test.ocbc.ui

import android.view.LayoutInflater
import com.test.ocbc.base.BaseActivityBinding
import com.test.ocbc.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainNavigatorActivity : BaseActivityBinding<ActivityMainBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate

    override fun setupView(binding: ActivityMainBinding) {
        CoroutineScope(Dispatchers.IO).launch {
            prefs.authToken.collect {
                withContext(Dispatchers.Main) {
                    if (it.isNullOrEmpty()) {
                        // TODO("Navigate to LoginFragment")
                    }
                }
            }
        }
    }
}