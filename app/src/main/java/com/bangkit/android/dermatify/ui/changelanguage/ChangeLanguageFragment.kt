package com.bangkit.android.dermatify.ui.changelanguage

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.data.local.SettingsPreferences
import com.bangkit.android.dermatify.data.local.dataStore
import com.bangkit.android.dermatify.databinding.FragmentChangeLanguageBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ChangeLanguageFragment : Fragment() {

    private var _binding: FragmentChangeLanguageBinding? = null
    private val binding get() = _binding!!

    private val changeLangViewModel by activityViewModels<ChangeLanguageViewModel> {
        ChangeLanguageViewModelFactory(SettingsPreferences.getInstance(requireActivity().application.dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = activity?.getColor(R.color.white_background) ?: Color.TRANSPARENT
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        requireActivity().window.statusBarColor = activity?.getColor(R.color.white_background) ?: Color.TRANSPARENT
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeLanguageBinding.inflate(inflater, container, false)
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

            changeLangViewModel.getCurrentLocale().observe(viewLifecycleOwner) { locale ->
                changeLangViewModel.currentLocale = locale

                if (changeLangViewModel.currentLocale == "en") {
                    rbEnglish.isChecked = true
                } else if (changeLangViewModel.currentLocale == "in") {
                    rbIndo.isChecked = true
                }
            }

            rbIndo.setOnClickListener {
                if (changeLangViewModel.currentLocale == "en") {
                    MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
                        .setTitle(getString(R.string.change_language_dialog_title))
                        .setMessage(getString(R.string.change_language_dialog_desc, rbIndo.text))
                        .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                            rbEnglish.isChecked = true
                            rbIndo.isChecked = false
                            dialog.dismiss()
                        }
                        .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("in")
                            AppCompatDelegate.setApplicationLocales(appLocale)
                            changeLangViewModel.updateLocale("in")
                            dialog.dismiss()
                            changeLangViewModel.getCurrentLocale()
                        }
                        .show()
                }
            }

            rbEnglish.setOnClickListener {
                if (changeLangViewModel.currentLocale == "in") {
                    MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
                        .setTitle(getString(R.string.change_language_dialog_title))
                        .setMessage(getString(R.string.change_language_dialog_desc, rbEnglish.text))
                        .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                            rbEnglish.isChecked = false
                            rbIndo.isChecked = true
                            dialog.dismiss()
                        }
                        .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("en")
                            AppCompatDelegate.setApplicationLocales(appLocale)
                            changeLangViewModel.updateLocale("en")
                            dialog.dismiss()
                            changeLangViewModel.getCurrentLocale()
                        }
                        .show()
                }
            }
        }
    }
}