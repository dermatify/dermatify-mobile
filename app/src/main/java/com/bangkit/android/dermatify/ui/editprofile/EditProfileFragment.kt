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
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.databinding.FragmentEditProfileBinding
import com.bangkit.android.dermatify.util.convertUriToString
import com.bangkit.android.dermatify.util.invisible
import com.bangkit.android.dermatify.util.setUriToImageView
import com.bangkit.android.dermatify.util.showSnackbar
import com.bangkit.android.dermatify.util.uriToFile
import com.bangkit.android.dermatify.util.visible
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
            val file = uri.uriToFile(requireContext())
            val fileUri = Uri.fromFile(file)
            userProfilePic = fileUri
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

            if (oldPic.isNotEmpty()) {
                ivProfilePicPh.setUriToImageView(userProfilePic)
                ivProfile.invisible()
                ivProfilePicPh.visible()
            }  else {
                ivProfile.visible()
                ivProfilePicPh.invisible()
            }

            topbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            btnChangeProfilePic.setOnClickListener {
                startGallery()
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
                Log.d("Cilukba", "${userProfilePic.toString()} $newPic")
                if (edEditName.error != null) {
                    root.showSnackbar(getString(R.string.name_error), "error", btnSave)
                } else if (userName != name || oldPic != newPic){
                    editProfileViewModel.saveUpdate(name, newPic)
                    root.showSnackbar(getString(R.string.profile_success), "success", btnSave)
                    findNavController().popBackStack()
                }
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

}