package com.bangkit.android.dermatify.ui.editprofile

import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.databinding.FragmentEditProfileBinding
import com.bangkit.android.dermatify.util.convertUriToString
import com.bangkit.android.dermatify.util.invisible
import com.bangkit.android.dermatify.util.setUriToImageView
import com.bangkit.android.dermatify.util.showSnackbar
import com.bangkit.android.dermatify.util.uriToFile
import com.bangkit.android.dermatify.util.visible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: EditProfileFragmentArgs by navArgs()

    private val editProfileViewModel by activityViewModels<EditProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private var userEmail = ""
    private var userName = ""
    private var userProfilePic: Uri? = null
    private var newPic: String = ""
    private var oldPic: String = ""

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            if (newPic.isNotEmpty() && newPic != "removed") {
                deletePic(newPic)
            }


            userProfilePic = uri
            newPic = userProfilePic.convertUriToString()
            Log.d("Cilukba", "newPic: $newPic")
            binding.apply {
                ivProfile.invisible()
                ivProfilePicPh.setUriToImageView(uri)
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
        fetchDataFromNavArgs()

        binding.apply {
            tvEmail.text = userEmail
            edEditName.text = Editable.Factory.getInstance().newEditable(userName)

            Log.d("Cilukba", "isempty userpic: ${oldPic}")
            if (oldPic.isNotEmpty()) {
                ivProfilePicPh.setUriToImageView(userProfilePic)
                ivProfilePicPh.visible()
            }

            topbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            btnChangeProfilePic.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext(), R.style.EditProfile_MaterialAlertDialog)
                    .setTitle(getString(R.string.change_profile_photo))
                    .setNegativeButton(getString(R.string.remove_photo)) { dialog, _ ->
                        if (newPic.isNotEmpty() && newPic != "removed") {
                            ivProfilePicPh.setImageURI(null)
                            deletePic(newPic)
                            newPic = "removed"
                            oldPic = "removed"
                            ivProfile.visible()
                            ivProfilePicPh.invisible()
                        } else if (oldPic.isNotEmpty() && oldPic != "removed"){
                            ivProfilePicPh.setImageURI(null)
                            newPic = "removed"
                            ivProfile.visible()
                            ivProfilePicPh.invisible()
                        }
                    }
                    .setPositiveButton(getString(R.string.select_from_gallery)) { dialog, _ ->
                        startGallery()
                    }
                    .show()
            }

            edEditName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun afterTextChanged(view: Editable?) {
                    if (view.toString().isEmpty()) {
                        edEditName.setError(getString(R.string.name_error))
                        edEditName.background = ContextCompat.getDrawable(requireContext(), R.drawable.ed_underline_error)
                    } else {
                        edEditName.setError(null)
                        edEditName.background = ContextCompat.getDrawable(requireContext(), R.drawable.ed_underline)
                    }
                }

            })

            btnSave.setOnClickListener {
                val name = edEditName.text.toString()

                if (newPic != oldPic && newPic.isNotEmpty() && newPic != "removed") {
                    val file = userProfilePic?.uriToFile(requireContext())
                    val fileUri = Uri.fromFile(file)
                    userProfilePic = fileUri
                    newPic = userProfilePic.convertUriToString()
                }

                Log.d("Cilukba", "${userProfilePic.toString()} $newPic")
                if (edEditName.error != null || name.isEmpty()) {
                    root.showSnackbar(getString(R.string.name_error), "error", btnSave)
                } else if (userName != name) {
                    editProfileViewModel.saveUpdateName(name)
                    initObserverUpdateUserProfile(name)
                }


                if (oldPic != newPic && newPic.isNotEmpty()){
                    editProfileViewModel.saveUpdatePic(newPic)
                    initObserverUpdateUserProfile(name)
                    deletePic(oldPic)
                } else if (oldPic.isEmpty() || newPic.isEmpty() || name.isNotEmpty()){
                    findNavController().popBackStack()
                }
            }


        }
    }
    private fun deletePic(pic: String) {
        if (pic.isNotEmpty()) {
            val fileUri = Uri.parse(pic)
            val file = fileUri.path?.let { File(it) }
            val deleted = file?.delete()
            if (deleted != null && deleted) {
                Log.d("Cilukba", "delete $pic success")
            }
        }
    }

    private fun fetchDataFromNavArgs() {
        userEmail = navigationArgs.email
        userName = navigationArgs.name
        oldPic = navigationArgs.pic
        userProfilePic = Uri.parse(oldPic)
    }

    private fun checkPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun initObserverUpdateUserProfile(name: String) {
        editProfileViewModel.updateUserProfileRemotely(name).observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResponse.Success -> {
                    binding.apply {
                        root.showSnackbar(
                            getString(R.string.profile_success),
                            "success",
                            btnSave
                        )
                    }
                    findNavController().popBackStack()
                    Log.d("Cilukba", result.data.name)
                }
                is ApiResponse.Error -> {
                    binding.apply {
                        val msg = getString(R.string.error_occured)
                        root.showSnackbar(
                            message = msg,
                            type = "error",
                            anchorView = btnSave
                        )
                    }
                    initRenewObserver(name)
                    editProfileViewModel.renewAccessToken()
                }
                is ApiResponse.Loading -> {

                }
            }
        }
    }

    private fun initRenewObserver(name: String) {
        editProfileViewModel.renewAccessToken().observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResponse.Error -> {
                    if (result.errorMsg == "Seems you lost your connection. Please try again") {
                        val msg = getString(R.string.network_lost)
                        binding.apply {
                            root.showSnackbar(
                                message = msg,
                                type = "error",
                                anchorView = btnSave
                            )
                        }
                    }
                }
                is ApiResponse.Success -> {
                    editProfileViewModel.updateUserProfileRemotely(name)
                }
                is ApiResponse.Loading -> { }
            }
        }
    }

}