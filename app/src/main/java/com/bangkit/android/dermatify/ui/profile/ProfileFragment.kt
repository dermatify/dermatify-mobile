package com.bangkit.android.dermatify.ui.profile

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.databinding.FragmentProfileBinding
import com.bangkit.android.dermatify.util.gone
import com.bangkit.android.dermatify.util.invisible
import com.bangkit.android.dermatify.util.setUriToImageView
import com.bangkit.android.dermatify.util.showSnackbar
import com.bangkit.android.dermatify.util.uriToFile
import com.bangkit.android.dermatify.util.visible


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel by activityViewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private var userEmail = ""
    private var userName = ""
    private var userProfilePic = ""

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
        initLogoutObserver()
        initRenewObserver()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        requireActivity().window.statusBarColor = activity?.getColor(R.color.primary_blue) ?: Color.TRANSPARENT
        requireActivity().window.navigationBarColor = activity?.getColor(R.color.white_background) ?: Color.TRANSPARENT
    }

    private fun setupUI() {
        binding.apply {
            profileViewModel.getEmail().observe(viewLifecycleOwner) {
                userEmail = it
                tvEmail.text = userEmail
            }

            profileViewModel.getUserName().observe(viewLifecycleOwner) {
                userName = it
                tvName.text = userName
            }

            profileViewModel.getUserPic().observe(viewLifecycleOwner) { userPic ->
                if (userPic.isNotEmpty()) {
                    ivProfilePicPh.setUriToImageView(Uri.parse(userPic))
                    userProfilePic = userPic
                    ivProfilePicPh.visible()
                }
            }

            btnEdit.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(userEmail, userName, userProfilePic))
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
                profileViewModel.logout()
            }
        }
    }

    private fun initLogoutObserver() {
        profileViewModel.logoutResponse.observe(viewLifecycleOwner) { result ->
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
                        profileViewModel.renewAccessToken()
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
                null -> { }
            }
        }
    }

    private fun initRenewObserver() {
        profileViewModel.renewTokenResponse.observe(viewLifecycleOwner) { result ->
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
                is ApiResponse.Success -> {
                    profileViewModel.logout()
                }
                is ApiResponse.Loading -> { }
                null -> { }
            }
        }
    }
}