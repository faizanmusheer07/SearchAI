package com.searchai.camera.openCamera.presentation.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.searchai.camera.R
import com.searchai.camera.databinding.FragmentCameraOptionBottomSheetBinding
import com.searchai.common.kotlinExtentions.BaseBottomSheet
import com.searchai.common.kotlinExtentions.base.bottomsheet.BottomSheetLevelInterface
import com.searchai.common.kotlinExtentions.constants.BottomSheetConst
import com.searchai.common.viewmodel.GalleryViewModel
import com.searchai.myroom.createHub.presentation.viewmodels.CreateRoomViewmodel
import com.searchai.myroom.createRoom.presentation.fragments.CreateRoomFragment


class CameraOptionBottomSheetFragment :
    BaseBottomSheet<FragmentCameraOptionBottomSheetBinding>(R.layout.fragment_camera_option_bottom_sheet,FragmentCameraOptionBottomSheetBinding::bind),BottomSheetLevelInterface {

    var count = 0

    private val viewModel: GalleryViewModel by activityViewModels()
    private val createRoomViewModel : CreateRoomViewmodel by activityViewModels()

    @SuppressLint("NewApi")
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
                    if (count % 2 != 0) {
                        dismiss()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
        })
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.constraintLayout3.progress = 0f

        createRoomViewModel.bottomSheetDraggedState.observe(viewLifecycleOwner) { state->
            val shiftedState = (1.plus(state))/2f
            setMotionLevel(-shiftedState)

        }

        binding.selectStoryIcon.setOnClickListener {
            viewModel.setUploadType(0)
            viewModel.isOpenFirstTime = false
            findNavController().navigate(CameraOptionBottomSheetFragmentDirections.actionCameraOptionBottomSheetFragmentToCameraFragment())
            dismiss()
        }

        binding.selectStoryTextView.setOnClickListener {
            viewModel.setUploadType(0)
            viewModel.isOpenFirstTime = false
            findNavController().navigate(CameraOptionBottomSheetFragmentDirections.actionCameraOptionBottomSheetFragmentToCameraFragment())
            dismiss()
        }

        binding.selectCameraIcon.setOnClickListener {
            viewModel.setUploadType(1)
            viewModel.isOpenFirstTime = false
            findNavController().navigate(CameraOptionBottomSheetFragmentDirections.actionCameraOptionBottomSheetFragmentToCameraFragment())
            dismiss()
        }


        binding.selectCameraTextView.setOnClickListener {
            viewModel.setUploadType(1)
            viewModel.isOpenFirstTime = false
            findNavController().navigate(CameraOptionBottomSheetFragmentDirections.actionCameraOptionBottomSheetFragmentToCameraFragment())
            dismiss()
        }

        binding.createHubIcon.setOnClickListener {
            val bottomSheet = CreateRoomFragment()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

        binding.createHubTextView.setOnClickListener {
            val bottomSheet = CreateRoomFragment()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.roomNoticeDropdownIcon.setOnClickListener {
            if (viewModel.isOpenFirstTime) {
                viewModel.isOpenFirstTime = true
                findNavController().popBackStack()
            } else {
                dismiss()
            }
        }
    }

    override fun onSheet2Dismissed() {
        setLevel(-1)
    }

    override fun onSheet2Created() {
        setLevel(-2)
    }

    override fun onSheet1Dismissed() {
        setLevel(0)
    }

    override fun getHeightOfBottomSheet(height: Int) {
        binding.cameraOptionConstraintLayout.layoutParams.height = height + 10.toPx().toInt()
        setLevel(-1)
    }
}