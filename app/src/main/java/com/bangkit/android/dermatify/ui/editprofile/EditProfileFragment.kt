package com.bangkit.android.dermatify.ui.editprofile

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.databinding.FragmentEditProfileBinding
import com.bangkit.android.dermatify.util.gone
import com.bangkit.android.dermatify.util.invisible
import com.bangkit.android.dermatify.util.showSnackbar

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: EditProfileFragmentArgs by navArgs()

    private val userEmail = navigationArgs.email
    private var userProfilePic: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                binding.root.showSnackbar(getString(R.string.camera_permission_granted), "success", binding.btnSave)
            } else {
                binding.root.showSnackbar(getString(R.string.camera_permission_denied), "error", binding.btnSave)
            }
        }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            userProfilePic = uri
            binding.apply {
                ivProfile.invisible()
                ivProfilePicPh.showImage()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = activity?.getColor(R.color.primary_blue) ?: Color.TRANSPARENT
        requireActivity().window.navigationBarColor = activity?.getColor(R.color.primary_blue) ?: Color.TRANSPARENT

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        if (!checkPermission(CAMERA_PERMISSION)) {
            requestPermissionLauncher.launch(CAMERA_PERMISSION)
        }

        binding.apply {
            tvEmail.text = userEmail

            topbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            btnChangeProfilePic.setOnClickListener {
                startGallery()
            }
        }
    }

    private fun ImageView.showImage() {
        userProfilePic?.let {
            this.setImageURI(it)
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

    companion object {
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }
}