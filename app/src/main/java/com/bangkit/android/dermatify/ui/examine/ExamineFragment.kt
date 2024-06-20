package com.bangkit.android.dermatify.ui.examine

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.databinding.FragmentExamineBinding
import com.bangkit.android.dermatify.util.convertUriToString
import com.bangkit.android.dermatify.util.getImageUri
import com.bangkit.android.dermatify.util.invisible
import com.bangkit.android.dermatify.util.reduceFileImage
import com.bangkit.android.dermatify.util.setUriToImageView
import com.bangkit.android.dermatify.util.showSnackbar
import com.bangkit.android.dermatify.util.showSnackbarWithActionBtn
import com.bangkit.android.dermatify.util.uriToFile
import com.bangkit.android.dermatify.util.visible
import java.io.File

class ExamineFragment : Fragment() {

    private var _binding: FragmentExamineBinding? = null
    private val binding get() = _binding!!

    private var picUri: Uri? = null
    private var newPic: String = ""
    private var file: File? = null

    private val examineViewModel by activityViewModels<ExamineViewModel> {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            picUri = uri
            Log.d("Cilukba", "newPic: $newPic")
            showImage()
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            picUri?.let {
                showImage()
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                binding.root.showSnackbar(getString(R.string.permission_granted), "success")
            } else {
                binding.root.showSnackbar(getString(R.string.permission_denied), "error")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExamineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initAnalyzeObserver()
    }

    private fun initUI() {
        if (!checkPermission(CAMERA_PERMISSION)) {
            requestPermissionLauncher.launch(CAMERA_PERMISSION)
        }

        binding.apply {
            topbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            btnGallery.setOnClickListener {
                startGallery()
            }

            btnCam.setOnClickListener {
                startCamera()
            }

            btnAnalyze.setOnClickListener {
                picUri?.let {
                    file = picUri?.uriToFile(requireContext())?.reduceFileImage()
                    newPic = Uri.fromFile(file).convertUriToString()
//                    examineViewModel.analyzePic(file)
                    findNavController().navigate(R.id.action_examineFragment_to_examineResultFragment)
                }

            }
        }
    }

    private fun initAnalyzeObserver() {
        examineViewModel.analyzeReponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResponse.Error -> {
                    binding.apply {
                        btnAnalyze.text = getString(R.string.analyze)
                        tvAnalyzing.invisible()
                        progbarAnalyze.invisible()
                        btnAnalyze.isEnabled = true

                        btnAnalyze.showSnackbarWithActionBtn(
                            message = getString(R.string.error_occured),
                            type = "error",
                            actionMsg = getString(R.string.try_again),
                            onClick = {
                                file?.let {
                                    examineViewModel.analyzePic(file)
                                }
                            }
                        )
                    }
                }
                is ApiResponse.Loading -> {
                    binding.apply {
                        btnAnalyze.text = ""
                        tvAnalyzing.visible()
                        progbarAnalyze.visible()
                        btnAnalyze.isEnabled = false
                    }
                }
                is ApiResponse.Success -> {

                }
                null -> { }
            }
        }
    }

    private fun checkPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        picUri = getImageUri(requireContext())
        launcherIntentCamera.launch(picUri)
    }

    private fun showImage() {
        binding.apply {
            tvHeaderTitle.invisible()
            animIllus.invisible()
            ivPreview.setUriToImageView(picUri)
            ivPreview.visible()
            tvHeaderTitleAfterPhoto.visible()
            btnAnalyze.visible()
        }
    }

    companion object {
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }

}