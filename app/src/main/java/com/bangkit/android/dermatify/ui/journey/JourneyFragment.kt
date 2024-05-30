package com.bangkit.android.dermatify.ui.journey

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.databinding.FragmentJourneyBinding

class JourneyFragment : Fragment() {

    private var _binding: FragmentJourneyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJourneyBinding.inflate(inflater, container, false)
        return binding.root
    }
}