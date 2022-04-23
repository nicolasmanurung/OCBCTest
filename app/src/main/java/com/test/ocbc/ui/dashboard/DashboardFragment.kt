package com.test.ocbc.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import com.test.ocbc.base.BaseFragmentBinding
import com.test.ocbc.databinding.FragmentDashboardBinding

class DashboardFragment : BaseFragmentBinding<FragmentDashboardBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDashboardBinding =
        FragmentDashboardBinding::inflate

    override fun setupView(binding: FragmentDashboardBinding) {

    }
}