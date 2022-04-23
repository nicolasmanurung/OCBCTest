package com.test.ocbc.ui.login

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.test.ocbc.base.BaseFragmentBinding
import com.test.ocbc.data.source.network.request.LoginRequest
import com.test.ocbc.databinding.FragmentLoginBinding
import com.test.ocbc.ui.OCBCViewModel

class LoginFragment : BaseFragmentBinding<FragmentLoginBinding>() {
    private val viewModel: OCBCViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding =
        FragmentLoginBinding::inflate

    override fun setupView(binding: FragmentLoginBinding) {
        observeLogin()

        binding.etUsername.doAfterTextChanged {
            binding.textFieldUsername.error = null
        }

        binding.etPassword.doAfterTextChanged {
            binding.textFieldPassword.error = null
        }

        binding.btnLogin.setOnClickListener {
            progressDialog.setRefreshing(true)
            if (isAllFilledCorrect()) {
                val loginRequest = LoginRequest(
                    username = binding.etUsername.text.toString(),
                    password = binding.etPassword.text.toString()
                )
                viewModel.login(loginRequest)
            } else {
                progressDialog.setRefreshing(false)
            }
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
                progressDialog.setRefreshing(false)
                Toast.makeText(binding.root.context, it.username, Toast.LENGTH_LONG).show()
            } else {
                progressDialog.setRefreshing(false)
            }
        }

        viewModel.loginAuthThrowable.observe(viewLifecycleOwner) {
            progressDialog.setRefreshing(false)
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
}