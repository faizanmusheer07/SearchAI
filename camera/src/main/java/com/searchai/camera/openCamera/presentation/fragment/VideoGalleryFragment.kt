package com.searchai.camera.openCamera.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleObserver
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.searchai.camera.databinding.FragmentGalleryBinding
import com.searchai.camera.openCamera.data.controller.GalleryVideoController
import com.searchai.common.viewmodel.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
/**
 * A simple [Fragment] subclass.
 * Use the [VideoGalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class VideoGalleryFragment : BottomSheetDialogFragment(), LifecycleObserver {

    private var _binding: FragmentGalleryBinding? = null
    val binding get() = _binding!!

    val galleryViewModel: GalleryViewModel by activityViewModels()
    private var controller: GalleryVideoController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, com.searchai.common.R.style.AppBottomSheetDialogTheme)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(
                inflater,
                com.searchai.camera.R.layout.fragment_gallery, container, false
        )
        binding.galleryViewModel = this.galleryViewModel//attach your viewModel to xml
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as BottomSheetDialog).behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onStart() {
        loadGallery(requireContext())
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    /** Load Gallery */
    private fun loadGallery(context: Context) {
        this.galleryViewModel.loadVideoPathList(context)
        val videoList = this.galleryViewModel.pathListVideo.value

        Log.i("VideoListFragment", videoList.toString())

        /**Setting up the Controller for the epoxy.*/
        controller = activity?.let {
            GalleryVideoController(
                    it,
                    this.galleryViewModel
            )
        }
        if (controller != null) {
            /** Setting up the Gallery Recycler View For Video Fetching */
            binding.recyclerView.setController(controller!!)
            if (videoList != null) {
                /**Sending the VideoList to the Controller by assigning the variable.*/
                controller!!.allVideo = videoList
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        controller?.context = null
        controller?.viewModel = null

        controller = null

        _binding = null
    }
}
