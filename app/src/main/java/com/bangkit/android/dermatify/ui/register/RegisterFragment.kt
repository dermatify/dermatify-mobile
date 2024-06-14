package com.bangkit.android.dermatify.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.databinding.FragmentRegisterBinding
import com.bangkit.android.dermatify.util.closeKeyboard
import com.bangkit.android.dermatify.util.gone
import com.bangkit.android.dermatify.util.showSnackbar
import com.bangkit.android.dermatify.util.visible

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel by activityViewModels<RegisterViewModel> {
        RegViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.apply {
            topbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            btnRegisterReg.setOnClickListener {
                val name = edtNameReg.text.toString()
                val email = edtEmailReg.text.toString()
                val password = edtPasswordReg.text.toString()
                if (edtNameReg.error == null &&
                    edtEmailReg.error == null &&
                    edtPasswordReg.error == null &&
                    name.isNotEmpty() &&
                    email.isNotEmpty() &&
                    password.isNotEmpty()
                ) {
                  initRegisterObserver(name, email, password)
                  this@RegisterFragment.closeKeyboard()
                }
            }

            btnToLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    private fun initRegisterObserver(name: String, email: String, password: String) {
        registerViewModel.register(name, email, password).observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResponse.Loading -> {
                    binding.apply {
                        progbarRegister.visible()
                        btnRegisterReg.isEnabled = false
                    }
                }
                is ApiResponse.Error -> {
                    binding.apply {
                        progbarRegister.gone()
                        btnRegisterReg.isEnabled = true
                        root.showSnackbar(
                            message = result.errorMsg,
                            type = "error",
                            anchorView = tvLogin
                        )
                    }
                }
                is ApiResponse.Success -> {
                    binding.apply {
                        progbarRegister.gone()
                        root.showSnackbar(
                            message = getString(R.string.register_success),
                            type = "success",
                            anchorView = tvLogin
                        )
                    }
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }

        }
    }

}