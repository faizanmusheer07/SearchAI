package com.searchai.profile.setting.presentation.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.searchai.auth.presentation.AuthViewModel
import com.searchai.common.kotlinExtentions.BaseBottomSheet
import com.searchai.common.kotlinExtentions.constants.BottomSheetConst
import com.searchai.profile.R
import com.searchai.profile.databinding.FragmentSettingBinding
import com.searchai.profile.discovery.presentation.fragments.DiscoveryFragment
import com.searchai.profile.editprofile.presentation.fragment.EditProfileFragment
import com.searchai.profile.monetization.presentation.fragments.MonetizationFragment
import com.searchai.profile.paypayout.presentation.fragments.Pay_PayoutFragment
import com.searchai.profile.setting.domain.constant.Constants
import com.searchai.profile.setting.domain.models.earningCounts
import com.searchai.profile.setting.domain.models.otherSetting
import com.searchai.profile.setting.domain.models.switchSetting
import com.searchai.profile.setting.domain.models.textSetting
import com.searchai.profile.setting.presentation.epoxy.SettingController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseBottomSheet<FragmentSettingBinding>(
    R.layout.fragment_setting,
    FragmentSettingBinding::bind
) {

    private lateinit var controller:SettingController
    private var editProfileFragment: EditProfileFragment? = null
    private var discoveryFragment: DiscoveryFragment? = null
    private var monetizationFragment : MonetizationFragment? = null
    private var payPayoutFragment : Pay_PayoutFragment? = null
    var count = 0
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEpoxy()
    }

    private fun setupEpoxy() {
        controller = SettingController(
            onClickCallback = { data->
                when (data) {
                    Constants.EditProfile.displayName -> openEditProfile()
                    Constants.Discovery.displayName -> openDiscovery()
                    Constants.Monetization.displayName -> openMonetization()
                    Constants.PaymentAndPayouts.displayName -> openPayPayOut()
                    Constants.Logout.displayName -> logout()
                }
            }
        )

        binding.rvSettings.setController(controller)
        binding.rvSettings.layoutManager = LinearLayoutManager(
            requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvSettings.addItemDecoration(DividerItemDecoration(requireActivity(),RecyclerView.VERTICAL))
        controller.textSetting = textSetting
        controller.otherSetting = otherSetting
        controller.switchSetting = switchSetting
        controller.paymentCount = earningCounts
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            setUpFullScreen(bottomSheetDialog, BottomSheetConst.settingBottomSheetHeight)
        }
        (dialog as BottomSheetDialog).behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if(newState == BottomSheetBehavior.STATE_SETTLING) {
                    count++
                    if(count % 2 == 0){
                        dialog.dismiss()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit

        })
        dialog.behavior.isDraggable = false
        return dialog
    }

    private fun openEditProfile(){
            editProfileFragment = EditProfileFragment()
            editProfileFragment?.show(childFragmentManager,"SettingBottomSheet")
    }

    private fun openDiscovery(){
            discoveryFragment = DiscoveryFragment()
            discoveryFragment?.show(childFragmentManager,"SettingBottomSheet")
    }
    private fun openMonetization(){
            monetizationFragment = MonetizationFragment()
            monetizationFragment?.show(childFragmentManager,"SettingBottomSheet")
    }
    private fun openPayPayOut(){
            payPayoutFragment = Pay_PayoutFragment()
            payPayoutFragment?.show(childFragmentManager,"SettingBottomSheet")
    }

    private fun logout(){
        authViewModel.signOut()
        if (!authViewModel.isUserLoggedIn()){
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editProfileFragment = null
        discoveryFragment = null
        monetizationFragment = null
        payPayoutFragment = null
    }
}