package com.searchai.profile.editprofile.presentation.fragment

import android.app.Dialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.searchai.api.models.auth.response.Profile
import com.searchai.api.utils.Resource
import com.searchai.auth.presentation.AuthViewModel
import com.searchai.common.kotlinExtentions.BaseBottomSheet
import com.searchai.common.kotlinExtentions.constants.BottomSheetConst
import com.searchai.common.kotlinExtentions.constants.NetworkStatus
import com.searchai.common.kotlinExtentions.helper.SnackBarMessage
import com.searchai.profile.R
import com.searchai.profile.databinding.FragmentEditProfileBinding
import com.searchai.profile.editprofile.presentation.viewmodel.EditProfileViewModel
import com.searchai.profile.followandfollower.presentation.tablayout.FollowerTab
import com.searchai.profile.followandfollower.presentation.tablayout.FollowerTab.Companion
import com.searchai.profile.monetization.presentation.fragments.MonetizationFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : BaseBottomSheet<FragmentEditProfileBinding>(
    R.layout.fragment_edit_profile,
    FragmentEditProfileBinding::bind,
) {

    companion object {
        const val TAG = "EditProfile"
    }

    private val authViewModel by viewModels<AuthViewModel>()
    private val editProfileViewModel by viewModels<EditProfileViewModel>()
    private var profile: Profile? = null
    private var newProfile: Profile? = null
    private var areaOfExpertFragment: AreaOfExpertise? = null
    private var profileImageUri: Uri? = null

    var count = 0

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                profileImageUri = it
                binding.civProfilepicture.setImageURI(it)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Fetch User Profile and update UI
        viewLifecycleOwner.lifecycleScope.launch {
            profile = authViewModel.getCurrentUserProfile()
            profile?.let { populateUserProfile(it) }
            Log.d(TAG, profile.toString())
        }

        userEvents()
        addObservers()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            setUpFullScreen(bottomSheetDialog, BottomSheetConst.settingBottomSheetHeight)
        }
        (dialog as BottomSheetDialog).behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    count++
                    if (count % 2 == 0) {
                        dialog.dismiss()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit

        })
        dialog.behavior.isDraggable = false

        return dialog
    }

    private fun userEvents() {
        binding.saveButtonEditProfile.setOnClickListener {
            updateProfileRemote()
        }

        binding.editProfileImage.setOnClickListener {
            pickImage()
        }

        binding.expertise.setOnClickListener {
            openExpertiseEditor()
        }
    }

    private fun openExpertiseEditor(){
        areaOfExpertFragment = AreaOfExpertise()
        areaOfExpertFragment?.show(childFragmentManager,"SettingBottomSheet")
    }

    private fun pickImage() {
        pickImageLauncher.launch("image/*")
    }

    private fun populateUserProfile(profile: Profile) {
        profile.name.let { binding.uepUsername.setText(it) }
        profile.channelName.let { binding.uepEditChanelname.setText(it) }
        profile.bio.let { binding.tvBio.setText(it) }
        profile.areaOfExpert.let { binding.expertise.setText(it) }
        profile.email.let { binding.email.setText(it) }
        profile.webAddress.let { binding.tvWebsite.setText(it) }
        profile.location.let { binding.userLocation.text = it }
        profile.profilePicture.let { Picasso.get().load(it).into(binding.civProfilepicture) }
    }


    private fun saveUpdatedProfile(): Profile? {
        val fullName = binding.uepUsername.text.toString()
        val channelName = binding.uepEditChanelname.text.toString()
        val bio = binding.tvBio.text.toString()
        val expertise = binding.expertise.text.toString()
        val email = binding.email.text.toString()
        val website = binding.tvWebsite.text.toString()
        //val location = binding.userLocation.toString()
        val profileImageUrl = profileImageUri?.toString() ?: "existing_profile_image_url"

        var validationDone = true
        if (fullName.isEmpty()) {
            validationDone = false
            SnackBarMessage.makeSnackbar(requireView(), "Profile name cannot be empty")
        }

        if (bio.isEmpty()) {
            validationDone = false
            SnackBarMessage.makeSnackbar(requireView(), "Bio cannot be empty")
        }

        if (email.isEmpty()) {
            validationDone = false
            SnackBarMessage.makeSnackbar(requireView(), "Email cannot be empty")
        }

        if (validationDone) {
            val currentDate = editProfileViewModel.getCurrentDate()
            newProfile = profile?.let {
                Profile(
                    name = fullName,
                    channelName = channelName,
                    bio = bio,
                    areaOfExpert = expertise,
                    email = email,
                    webAddress = website,
                    location = it.location,
                    profilePicture = profileImageUrl,
                    followingCount = it.followingCount,
                    followerCount = it.followerCount,
                    createdAt = it.createdAt,
                    id = it.id,
                    provider = it.provider,
                    updatedAt = currentDate,
                    userId = it.userId,
                    verified = it.verified
                )
            }
        }

        return newProfile!!

    }

    private fun mapBodyProfile(
        channelName: String? = null,
        areaOfExpert: String? = null,
        name: String? = null,
        provider: String? = null,
        bio: String? = null,
        webAddress: String? = null,
        location: String? = null,
        profilePicture: String? = null
    ): Map<String, String?> {

        // Create a mutable map and add only the fields that are not null
        val fields = mutableMapOf<String, String?>()

        channelName?.let { fields["channel_name"] = it }
        areaOfExpert?.let { fields["area_of_expert"] = it }
        name?.let { fields["name"] = it }
        provider?.let { fields["provider"] = it }
        bio?.let { fields["bio"] = it }
        webAddress?.let { fields["web_address"] = it }
        location?.let { fields["location"] = it }
        profilePicture?.let { fields["profile_picture"] = it }

        return fields
    }

    private fun addObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                editProfileViewModel.update.collectLatest { response ->
                    when (response) {
                        is Resource.Loading -> {
                            SnackBarMessage.makeSnackbar(requireView(), "Loading")
                        }

                        is Resource.Error -> {
                            errorState(response)
                        }

                        is Resource.Idle -> {}
                        is Resource.Success -> {
                            if (response.data?.status == true) {
                                updateProfileStore()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun errorState(response: Resource.Error) {
        if (response.message?.contains(NetworkStatus.NoInternet.toString(), true) == true) {
            //show no internet
            SnackBarMessage.makeSnackbar(requireView(), "Check you internet connection")
        } else {
            Log.e(FollowerTab.TAG, response.message.toString())
            SnackBarMessage.makeSnackbar(requireView(), response.message.toString())
        }
    }

    private fun updateProfileRemote() {
        val updatedProfile = saveUpdatedProfile()
        if (updatedProfile != null) {
            val profileData = mapBodyProfile(
                name = updatedProfile.name,
                channelName = updatedProfile.channelName,
                bio = updatedProfile.bio,
                areaOfExpert = updatedProfile.areaOfExpert,
                webAddress = updatedProfile.webAddress,
                profilePicture = updatedProfile.profilePicture
            )
            editProfileViewModel.updateProfile(profileData)
        }
    }

    private fun updateProfileStore() {
        val updatedProfile = saveUpdatedProfile()

        viewLifecycleOwner.lifecycleScope.launch {
            if (updatedProfile != null) {
                authViewModel.saveProfile(updatedProfile)
            }
            Toast.makeText(requireContext(), "Profile saved successfully", Toast.LENGTH_SHORT)
                .show()
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        profile = null
        profileImageUri = null
    }

}