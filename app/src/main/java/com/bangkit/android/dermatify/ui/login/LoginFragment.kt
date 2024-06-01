package com.bangkit.android.dermatify.ui.login

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = activity?.getColor(R.color.primary_blue) ?: Color.TRANSPARENT
        requireActivity().window.navigationBarColor = activity?.getColor(R.color.primary_blue) ?: Color.TRANSPARENT
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailLogin.text.toString()
            val password = binding.edtPasswordLogin.text.toString()
            viewModel.login(email, password)
        }
        viewModel.loginSuccess.observe(viewLifecycleOwner, {isSuccess ->
            if (isSuccess) {

            } else {
                Snackbar.make(view, "Login failed. Invalid email or password", Snackbar.LENGTH_SHORT).show()
            }
        })
        binding.edtPasswordLogin.addTextChangedListener {
            val password = it.toString()
            if (password.length < 8) {
                binding.edtPasswordLogin.error = "Password must be at least 8 characters"
            } else {
                binding.edtPasswordLogin.error = null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding  = null
    }

}