package com.bangkit.android.dermatify.ui.examine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.android.dermatify.databinding.FragmentExamineBinding

class ExamineFragment : Fragment() {

    private var _binding: FragmentExamineBinding? = null
    private val binding = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExamineBinding.inflate(inflater, container, false)
        return binding.root
    }

}