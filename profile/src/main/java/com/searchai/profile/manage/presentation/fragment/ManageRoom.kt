package com.searchai.profile.manage.presentation.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.searchai.common.kotlinExtentions.BaseBottomSheet
import com.searchai.common.kotlinExtentions.constants.BottomSheetConst
import com.searchai.profile.R
import com.searchai.profile.databinding.FragmentManageBinding
import com.searchai.profile.manage.adapter.ManageViewPagerAdapter

class ManageRoom:BaseBottomSheet<FragmentManageBinding>(
    R.layout.fragment_manage,
    FragmentManageBinding::bind
) {
    var count = 0

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayout()
    }

    private fun setupTabLayout() {
        val pagerAdapter = ManageViewPagerAdapter(requireActivity())
        binding.viewPager2.adapter = pagerAdapter

        TabLayoutMediator(binding.manageRoomTab,binding.viewPager2){tab,poistion->
            tab.text = when(poistion){
                0 -> "Live"
                1 -> "Schedule"
                2 -> "Request"
                else -> {null}
            }
        }.attach()
    }
}