package com.test.ocbc.ui

import android.view.LayoutInflater
import com.test.ocbc.base.BaseActivityBinding
import com.test.ocbc.databinding.ActivityMainBinding

class MainNavigatorActivity : BaseActivityBinding<ActivityMainBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate

    override fun setupView(binding: ActivityMainBinding) {

    }
}