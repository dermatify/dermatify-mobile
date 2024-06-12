package com.bangkit.android.dermatify.ui.credits

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.databinding.FragmentCreditsBinding

class CreditsFragment : Fragment() {

    private val creditsViewModel: CreditsViewModel by viewModels()
    private var _binding: FragmentCreditsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreditsBinding.inflate(inflater, container, false)
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

            btnCheck.setOnClickListener {
                val intenBrowser = Intent(Intent.ACTION_VIEW, Uri.parse("https://freepik.com"))
                startActivity(intenBrowser)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}