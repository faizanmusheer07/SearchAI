package com.searchai.profile.tablayouts.video.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.searchai.api.utils.Resource
import com.searchai.auth.presentation.AuthViewModel
import com.searchai.common.kotlinExtentions.constants.NetworkStatus
import com.searchai.common.kotlinExtentions.helper.SnackBarMessage
import com.searchai.profile.databinding.FragmentVideoTabBinding
import com.searchai.profile.tablayouts.video.domain.model.VideoModel
import com.searchai.profile.tablayouts.video.presentation.epoxy.VideoController
import com.searchai.profile.tablayouts.video.presentation.viewmodel.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Video : Fragment() {
    companion object {
        private const val TAG = "VideoFragment"
    }

    private var _binding: FragmentVideoTabBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<VideoViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var controller: VideoController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoTabBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            val profile = authViewModel.getCurrentUserProfile()
            Log.d(TAG, "Profile data: $profile")
            if (profile != null) {
                viewModel.fetchVideoStream(profile.userId)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller = VideoController()
        binding.rvProfileVideo.setController(controller)
        binding.rvProfileVideo.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false)
        binding.rvProfileVideo.addItemDecoration(
            DividerItemDecoration(requireActivity(), RecyclerView.VERTICAL)
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect { response->
                    when(response){
                        is Resource.Error -> {
                            hideShimmer()
                            //  errorState(response)
                            showSampleData()
                        }
                        Resource.Idle -> {}
                        Resource.Loading -> showShimmer()
                        is Resource.Success -> showSuccessState(response)
                    }
                }
            }
        }
    }

    private fun showSuccessState(response: Resource.Success<List<VideoModel>>) {
        hideShimmer()

        if (response.data?.isEmpty() == true){
            showEmptyState()
        }else{
            controller.videoData = response.data!!
        }
    }

    private fun showSampleData(){
        val roomList = listOf(
            VideoModel(
                category = "Music",
                filename = "song1.mp3",
                thumbnail = "thumbnail1.jpg",
                title = "Relaxing Beats",
                username = "dj_smooth",
                userid = "user123"
            ),
            VideoModel(
                category = "Art",
                filename = "art1.png",
                thumbnail = "thumbnail2.jpg",
                title = "Abstract Design",
                username = "artist_xyz",
                userid = "user456"
            ),
            VideoModel(
                category = "Photography",
                filename = "photo1.jpg",
                thumbnail = "thumbnail3.jpg",
                title = "Sunset Bliss",
                username = "photog_guru",
                userid = "user789"
            ),
            VideoModel(
                category = "Gaming",
                filename = "game1.exe",
                thumbnail = "thumbnail4.jpg",
                title = "Epic Battle",
                username = "gamer_pro",
                userid = "user101"
            ),
            VideoModel(
                category = "Education",
                filename = "lecture1.mp4",
                thumbnail = "thumbnail5.jpg",
                title = "Math 101",
                username = "prof_knowit",
                userid = "user112"
            ),
            VideoModel(
                category = "Technology",
                filename = "tech_podcast1.mp3",
                thumbnail = "thumbnail6.jpg",
                title = "AI Revolution",
                username = "techie_talks",
                userid = "user124"
            ),
            VideoModel(
                category = "Fitness",
                filename = "workout1.mp4",
                thumbnail = "thumbnail7.jpg",
                title = "Morning Yoga",
                username = "fit_fanatic",
                userid = "user133"
            ),
            VideoModel(
                category = "Cooking",
                filename = "recipe1.pdf",
                thumbnail = "thumbnail8.jpg",
                title = "Healthy Meals",
                username = "chef_master",
                userid = "user145"
            ),
            VideoModel(
                category = "Travel",
                filename = "travel_vlog1.mp4",
                thumbnail = "thumbnail9.jpg",
                title = "Exploring Europe",
                username = "wanderlust_hero",
                userid = "user154"
            ),
            VideoModel(
                category = "Fashion",
                filename = "style1.png",
                thumbnail = "thumbnail10.jpg",
                title = "Summer Collection",
                username = "fashionista",
                userid = "user166"
            )
        )
        controller.videoData = roomList
    }

    private fun hideShimmer() {
        binding.profileVideoShimmer.visibility = View.GONE
        binding.rvProfileVideo.visibility = View.VISIBLE
    }

    private fun showShimmer() {
        hideEmptyState()
        binding.profileVideoShimmer.visibility = View.VISIBLE
        binding.rvProfileVideo.visibility = View.GONE
    }

    private fun showEmptyState(){
        binding.profileVideoLabel.visibility = View.VISIBLE
        binding.profileVideoText.visibility = View.VISIBLE
    }

    private fun hideEmptyState(){
        binding.profileVideoLabel.visibility = View.GONE
        binding.profileVideoText.visibility = View.GONE
    }

    private fun errorState(response: Resource.Error) {
        if (response.message?.contains(NetworkStatus.NoInternet.toString(),true) == true){
            //show no internet
            SnackBarMessage.makeSnackbar(requireView(),"Check you internet connection")
        }else{
            showEmptyState()
            Log.e(TAG,response.message.toString())
            SnackBarMessage.makeSnackbar(requireView(),response.message.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}