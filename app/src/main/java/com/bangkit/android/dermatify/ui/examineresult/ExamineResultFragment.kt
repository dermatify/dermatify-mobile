package com.bangkit.android.dermatify.ui.examineresult

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.databinding.FragmentExamineResultBinding
import com.bangkit.android.dermatify.util.convertUriToString
import com.bangkit.android.dermatify.util.formatDate
import com.bangkit.android.dermatify.util.setUriToImageView
import com.bangkit.android.dermatify.util.visible

class ExamineResultFragment : Fragment() {

    private var _binding: FragmentExamineResultBinding? = null
    private val binding get() = _binding!!
    private val navigationArgs: ExamineResultFragmentArgs by navArgs()

    private lateinit var id: String
    private lateinit var createdAt: String
    private lateinit var diagnosis: String
    private lateinit var picUri: Uri

    private val resultViewModel by activityViewModels<ExamineResultViewModel> {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExamineResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchDataFromNavArgs()

        binding.apply {
            topbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            tvDate.text = createdAt
            ivPreview.setUriToImageView(picUri)
            cvDiagnosis.visible()
            cvTreatmentRecs.visible()
            when (diagnosis) {
                "Acne" -> {
                    cvAcnes.visible()
                    tvDiagnosisDesc.text = getString(R.string.acnes_diagnosis_desc)
                    tvTreatmentRecs.text = getString(R.string.acnes_treatment_desc)
                }

                "Redness" -> {
                    cvRedness.visible()
                    tvDiagnosisDesc.text = getString(R.string.redness_diagnosis_desc)
                    tvTreatmentRecs.text = getString(R.string.redness_treatment_desc)
                }

                "Eyebags" -> {
                    cvDarkCircles.visible()
                    tvDiagnosisDesc.text = getString(R.string.dark_circles_diagnosis_desc)
                    tvTreatmentRecs.text = getString(R.string.dark_circles_treatment_desc)
                }
            }

            btnExamineAgain.setOnClickListener {
                findNavController().navigate(R.id.action_examineResultFragment_to_examineFragment)
            }
            if (navigationArgs.isNew) {
                resultViewModel.insertScans(
                    img = picUri.convertUriToString(),
                    diagnosis = diagnosis,
                    desc = tvDiagnosisDesc.text.toString(),
                    date = createdAt)
            }
        }

    }

    private fun fetchDataFromNavArgs() {
        createdAt = if (navigationArgs.isNew) {
            formatDate(navigationArgs.createdAt)
        } else {
            navigationArgs.createdAt
        }
        id = navigationArgs.id
        picUri = Uri.parse(navigationArgs.picUri)
        diagnosis = navigationArgs.diagnosis
    }
}