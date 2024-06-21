package com.bangkit.android.dermatify.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.navigation.fragment.findNavController
import com.bangkit.android.dermatify.BuildConfig
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.databinding.FragmentLoginBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import java.security.SecureRandom
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.activityViewModels
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.util.closeKeyboard
import com.bangkit.android.dermatify.util.gone
import com.bangkit.android.dermatify.util.showSnackbar
import com.bangkit.android.dermatify.util.visible
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel


    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val url = "http://dermatify.daffafaizan.com/auth/google"

    private val loginViewModel by activityViewModels<LoginViewModel> {
        ViewModelFactory.getInstance(requireActivity().application)
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
//        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
//        binding.btnLogin.setOnClickListener {
//            val email = binding.edtEmailLogin.text.toString()
//            val password = binding.edtPasswordLogin.text.toString()
//            viewModel.login(email, password)
//        }
//        viewModel.loginSuccess.observe(viewLifecycleOwner, {isSuccess ->
//            if (isSuccess) {
//
//            } else {
//                Snackbar.make(view, "Login failed. Invalid email or password", Snackbar.LENGTH_SHORT).show()
//            }
//        })
//        binding.edtPasswordLogin.addTextChangedListener {
//            val password = it.toString()
//            if (password.length < 8) {
//                binding.edtPasswordLogin.error = "Password must be at least 8 characters"
//            } else {
//                binding.edtPasswordLogin.error = null
//            }
//        }
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding  = null
    }

    private fun initUI() {
        binding.apply {

            edtPasswordLogin.fragmentType = LOGIN
            topbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            btnLogin.setOnClickListener {
                val email = edtEmailLogin.text.toString()
                val password = edtPasswordLogin.text.toString()
                if (edtEmailLogin.error == null &&
                    edtPasswordLogin.error == null &&
                    email.isNotEmpty() &&
                    password.isNotEmpty()
                ) {
                    initLoginObserver(email, password)
                }
                this@LoginFragment.closeKeyboard()
            }

            btnToRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

        }
    }

    private fun initLoginObserver(email: String, password: String) {
        loginViewModel.login(email, password).observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResponse.Loading -> {
                    binding.apply {
                        progbarLogin.visible()
                        btnLogin.isEnabled = false
                    }
                }
                is ApiResponse.Error -> {
                    val msg = if (result.errorMsg.contains("Invalid credential") ||
                        result.errorMsg.contains("User not found")
                    ) {
                        getString(R.string.invalid_creds)
                    } else if (result.errorMsg == "Seems you lost your connection. Please try again") {
                        getString(R.string.network_lost)
                    } else {
                        getString(R.string.error_occured)
                    }
                    binding.apply {
                        progbarLogin.gone()
                        btnLogin.isEnabled = true
                        root.showSnackbar(
                            message = msg,
                            type = "error",
                            anchorView = tvRegister
                        )
                    }
                }
                is ApiResponse.Success -> {
                    binding.progbarLogin.gone()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
        }
    }

    companion object {
        const val LOGIN = "LOGIN"
    }

}