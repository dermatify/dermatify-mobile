package com.bangkit.android.dermatify.ui.profile

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.databinding.FragmentProfileBinding
import com.bangkit.android.dermatify.util.gone
import com.bangkit.android.dermatify.util.showSnackbar
import com.bangkit.android.dermatify.util.visible


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel by activityViewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = activity?.getColor(R.color.primary_blue) ?: Color.TRANSPARENT
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        requireActivity().window.statusBarColor = activity?.getColor(R.color.primary_blue) ?: Color.TRANSPARENT
        requireActivity().window.navigationBarColor = activity?.getColor(R.color.white_background) ?: Color.TRANSPARENT
    }

    private fun setupUI() {
        binding.apply {
            profileViewModel.getEmail().observe(viewLifecycleOwner) {
                tvEmail.text = it
            }

            topbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            btnEdit.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
            }

            btnChangeLang.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_changeLanguageFragment)
            }

            btnCredits.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_creditsFragment)
            }

            btnMeetTeam.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_meetTeamFragment)
            }

            btnLogout.setOnClickListener {
                initLogoutObserver()
            }
        }
    }

    private fun initLogoutObserver() {
        profileViewModel.logout().observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResponse.Loading -> {
                    binding.apply {
                        btnLogout.isEnabled = false
                        progbarLogout.visible()
                    }
                }
                is ApiResponse.Success -> {
                    findNavController().navigate(R.id.action_profileFragment_to_onboardingFragment)
                }
                is ApiResponse.Error -> {
                    if (result.errorMsg == "Invalid token!") {
                        Log.d("Cilukba", "Renew token")
                        initRenewObserver()
                    } else {
                        val msg = if (result.errorMsg == "Seems you lost your connection. Please try again") {
                            getString(R.string.network_lost)
                        } else {
                            getString(R.string.error_occured)
                        }
                        binding.apply {
                            root.showSnackbar(
                                message = msg,
                                type = "error",
                                anchorView = btnLogout
                            )
                        }
                    }
                    binding.apply {
                        btnLogout.isEnabled = true
                        progbarLogout.gone()
                    }
                }
            }
        }
    }

    private fun initRenewObserver() {
        profileViewModel.renewAccessToken().observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResponse.Error -> {
                    if (result.errorMsg.contains("Logout")) {
                        findNavController().navigate(R.id.action_profileFragment_to_onboardingFragment)
                    } else if (result.errorMsg == "Seems you lost your connection. Please try again") {
                        val msg = getString(R.string.network_lost)
                        binding.apply {
                            root.showSnackbar(
                                message = msg,
                                type = "error",
                                anchorView = btnLogout
                            )
                        }
                    }
                }
                is ApiResponse.Success -> { }
                is ApiResponse.Loading -> { }
            }
        }
    }
}