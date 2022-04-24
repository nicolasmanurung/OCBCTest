package com.test.ocbc.ui.login

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.test.ocbc.R
import com.test.ocbc.base.BaseFragmentBinding
import com.test.ocbc.data.source.network.request.LoginRequest
import com.test.ocbc.databinding.FragmentLoginBinding
import com.test.ocbc.ui.OCBCViewModel
import com.test.ocbc.ui.dashboard.DashboardActivity
import com.test.ocbc.ui.register.RegisterFragment

class LoginFragment : BaseFragmentBinding<FragmentLoginBinding>() {
    private val viewModel: OCBCViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding =
        FragmentLoginBinding::inflate

    override fun setupView(binding: FragmentLoginBinding) {
        observeLogin()
        observeLoading()

        binding.etUsername.doAfterTextChanged {
            binding.textFieldUsername.error = null
        }

        binding.etPassword.doAfterTextChanged {
            binding.textFieldPassword.error = null
        }

        binding.btnLogin.setOnClickListener {
            if (isAllFilledCorrect()) {
                val loginRequest = LoginRequest(
                    username = binding.etUsername.text.toString(),
                    password = binding.etPassword.text.toString()
                )
                viewModel.login(loginRequest)
            }
        }

        binding.btnRegistration.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerMain, RegisterFragment(), "RegisterFragment")
                .addToBackStack("RegisterFragment").commit()
        }
    }

    private fun isAllFilledCorrect(): Boolean {
        var isAllFilled = true
        if (binding.etUsername.text.isNullOrEmpty()) {
            isAllFilled = false
            binding.textFieldUsername.error = "Username is required"
        }
        if (binding.etPassword.text.isNullOrEmpty()) {
            isAllFilled = false
            binding.textFieldPassword.error = "Password is required"
        }
        return isAllFilled
    }

    private fun observeLogin() {
        viewModel.loginAuth.observe(viewLifecycleOwner) {
            if (it?.status == "success") {
                startActivity(Intent(binding.root.context, DashboardActivity::class.java))
                requireActivity().finish()
            }
        }

        viewModel.loginAuthThrowable.observe(viewLifecycleOwner) {
            when (it) {
                404 -> {
                    binding.textFieldUsername.error = "User not found"
                    binding.textFieldPassword.error = "User not found"
                }
                401 -> {
                    binding.textFieldPassword.error = "Wrong password"
                }
            }
        }
    }

    private fun observeLoading() {
        viewModel.showingLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progressDialog.setRefreshing(true)
            } else {
                progressDialog.setRefreshing(false)
            }
        }
    }
}