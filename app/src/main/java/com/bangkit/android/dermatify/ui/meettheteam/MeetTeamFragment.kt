package com.bangkit.android.dermatify.ui.meettheteam

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.databinding.FragmentMeetTeamBinding
import com.bangkit.android.dermatify.util.Constants.GH_AWS
import com.bangkit.android.dermatify.util.Constants.GH_DMF
import com.bangkit.android.dermatify.util.Constants.GH_JABP
import com.bangkit.android.dermatify.util.Constants.GH_KJP
import com.bangkit.android.dermatify.util.Constants.GH_MNA
import com.bangkit.android.dermatify.util.Constants.GH_SA
import com.bangkit.android.dermatify.util.Constants.LI_AWS
import com.bangkit.android.dermatify.util.Constants.LI_DMF
import com.bangkit.android.dermatify.util.Constants.LI_JABP
import com.bangkit.android.dermatify.util.Constants.LI_KJP
import com.bangkit.android.dermatify.util.Constants.LI_MNA
import com.bangkit.android.dermatify.util.Constants.LI_NGA
import com.bangkit.android.dermatify.util.Constants.LI_SA

class MeetTeamFragment : Fragment() {

    private var _binding: FragmentMeetTeamBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMeetTeamBinding.inflate(inflater, container, false)
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

            ivGithubMl1.setOnClickListener {
                val webpage = Uri.parse(GH_MNA)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }

            ivLinkedinMl1.setOnClickListener {
                val webpage = Uri.parse(LI_MNA)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }

            ivLinkedinMl2.setOnClickListener {
                val webpage = Uri.parse(LI_NGA)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }

            ivGithubMl3.setOnClickListener {
                val webpage = Uri.parse(GH_SA)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }

            ivLinkedinMl3.setOnClickListener {
                val webpage = Uri.parse(LI_SA)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }

            ivGithubCc1.setOnClickListener {
                val webpage = Uri.parse(GH_AWS)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }

            ivLinkedinCc1.setOnClickListener {
                val webpage = Uri.parse(LI_AWS)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }

            ivGithubCc2.setOnClickListener {
                val webpage = Uri.parse(GH_DMF)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }

            ivLinkedinCc2.setOnClickListener {
                val webpage = Uri.parse(LI_DMF)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }

            ivGithubMd1.setOnClickListener {
                val webpage = Uri.parse(GH_JABP)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }

            ivLinkedinMd1.setOnClickListener {
                val webpage = Uri.parse(LI_JABP)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }

            ivGithubMd2.setOnClickListener {
                val webpage = Uri.parse(GH_KJP)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }

            ivLinkedinMd2.setOnClickListener {
                val webpage = Uri.parse(LI_KJP)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }
        }
    }
}