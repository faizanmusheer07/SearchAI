package com.searchai.myroom.scheduleRoom.presentation.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.searchai.common.kotlinExtentions.base.bottomsheet.BaseBottomSheet
import com.searchai.common.kotlinExtentions.base.bottomsheet.BottomSheetLevelInterface
import com.searchai.myroom.R
import com.searchai.myroom.databinding.FragmentScheduleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class ScheduleFragment(
    private val levelInterface: BottomSheetLevelInterface
) : BaseBottomSheet<FragmentScheduleBinding>(
    R.layout.fragment_select_room_category,
    FragmentScheduleBinding::inflate
) {
    companion object {
        const val TAG = "ScheduleFragment"
        var settingBottomSheetHeight: Int = 80
    }

    var count = 0
    var str  = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        levelInterface.onSheet2Created()

        (view.parent as View).setBackgroundColor(Color.TRANSPARENT)
        with(binding) {
            backButton.setOnClickListener {
                navigateToRoom()
            }

            doneButton.setOnClickListener {
                /** add the datepicker date in the viewmodel*/
//                roomSharedViewModel.time.value = datepicker.date.time
                str = datepicker.date.time
                navigateToRoom()
            }

//            roomSharedViewModel.roomTitle.observe(viewLifecycleOwner) {
//                roomTitle.text = it
//            }
        }
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            setUpFullScreen(bottomSheetDialog, settingBottomSheetHeight + 12)
        }

        (dialog as BottomSheetDialog).behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    count++
                    if (count % 2 == 0) {
                        dismiss()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
        })
        dialog.behavior.isDraggable = false
        return dialog
    }

    private fun navigateToRoom() {
        Log.d(TAG,str.toString())
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        levelInterface.onSheet2Dismissed()
    }

}