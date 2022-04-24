package com.test.ocbc.ui.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.test.ocbc.base.BaseFragmentBinding
import com.test.ocbc.data.source.network.request.RegistrationRequest
import com.test.ocbc.databinding.FragmentRegisterBinding
import com.test.ocbc.ui.OCBCViewModel
import com.test.ocbc.utils.ViewUtil.showSnackImageToast

class RegisterFragment : BaseFragmentBinding<FragmentRegisterBinding>() {
    private val viewModel: OCBCViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRegisterBinding =
        FragmentRegisterBinding::inflate

    override fun setupView(binding: FragmentRegisterBinding) {
        observeRegistration()
        observeLoading()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.etUsername.doAfterTextChanged {
            binding.textFieldUsername.error = null
        }

        binding.etPassword.doAfterTextChanged {
            binding.textFieldPassword.error = null
        }

        binding.etRePassword.doAfterTextChanged {
            if (it.toString() != binding.etPassword.text.toString()) {
                binding.textFieldRePassword.error = "Confirm password not match!"
            } else {
                binding.textFieldRePassword.error = null
            }
        }

        binding.btnRegistration.setOnClickListener {
            if (isAllFilledCorrect()) {
                val request = RegistrationRequest(
                    password = binding.etPassword.text.toString(),
                    username = binding.etUsername.text.toString()
                )
                viewModel.postUserRegistration(request)
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
        if (binding.etRePassword.text.isNullOrEmpty()) {
            isAllFilled = false
            binding.textFieldRePassword.error = "Confirm Password is required"
        }
        if (binding.etRePassword.text.toString() != binding.etPassword.text.toString()) {
            isAllFilled = false
            binding.textFieldRePassword.error = "Confirm password not match!"
        }

        return isAllFilled
    }

    private fun observeRegistration() {
        viewModel.postUserRegistration.observe(viewLifecycleOwner) { response ->
            if (response?.status == "success") {
                requireActivity().showSnackImageToast("Register success!")
                findNavController().popBackStack()
            }
        }

        viewModel.postUserRegistrationThrowable.observe(viewLifecycleOwner) { response ->
            when (response) {
                403 -> {
                    binding.textFieldUsername.error = "Username already exist"
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