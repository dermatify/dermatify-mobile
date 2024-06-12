package com.bangkit.android.dermatify.ui.profile

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.databinding.FragmentProfileBinding

private var _binding: FragmentProfileBinding? = null
private val binding get() = _binding!!


class ProfileFragment : Fragment() {

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
        }
    }
}