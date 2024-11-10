package com.searchai.profile.manage.presentation.tablayouts.schedule.presentation.fragment

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
import com.searchai.profile.commonModel.Room
import com.searchai.profile.databinding.FragmentManageScheduledTabBinding
import com.searchai.profile.manage.presentation.tablayouts.live.presentation.epoxy.LiveRoomController
import com.searchai.profile.manage.presentation.tablayouts.live.presentation.viewmodel.LiveRoomViewModel
import com.searchai.profile.manage.presentation.tablayouts.schedule.presentation.epoxy.ScheduleRoomController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Schedule : Fragment() {

    companion object {
        private val TAG = "ManageScheduleRoom"
    }

    private var _binding: FragmentManageScheduledTabBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LiveRoomViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var controller: ScheduleRoomController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManageScheduledTabBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            val profile = authViewModel.getCurrentUserProfile()
            Log.d(TAG, "Profile data: $profile")
            if (profile != null) {
                viewModel.fetchScheduleRoom(profile.userId)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = ScheduleRoomController(onClickCallback = { onClick ->
            SnackBarMessage.makeSnackbar(requireView(), "Click")
        })
        binding.manageRoomsRecyclerView.setController(controller)
        binding.manageRoomsRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )
        binding.manageRoomsRecyclerView.addItemDecoration(
            DividerItemDecoration(requireActivity(), RecyclerView.VERTICAL)
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.scheduleRoom.collect { response ->
                    when (response) {
                        is Resource.Error -> {
                            hideShimmer()
                            errorState(response)
                        }

                        Resource.Idle -> {}
                        Resource.Loading -> showShimmer()
                        is Resource.Success -> showSuccessState(response)
                    }
                }
            }
        }
    }

    private fun showSuccessState(response: Resource.Success<List<Room>>) {
        hideShimmer()

        if (response.data?.isEmpty() == true) {
            showEmptyState()
        } else {
            controller.room = response.data!!
        }
    }

    private fun hideShimmer() {
        binding.scheduleRoomShimmer.visibility = View.GONE
        binding.manageRoomsRecyclerView.visibility = View.VISIBLE
    }

    private fun showShimmer() {
        hideEmptyState()
        binding.scheduleRoomShimmer.visibility = View.VISIBLE
        binding.manageRoomsRecyclerView.visibility = View.GONE
    }

    private fun showEmptyState() {
        binding.noRoomsLabel.visibility = View.VISIBLE
        binding.manageRoomText.visibility = View.VISIBLE
    }

    private fun hideEmptyState() {
        binding.noRoomsLabel.visibility = View.GONE
        binding.manageRoomText.visibility = View.GONE
    }

    private fun errorState(response: Resource.Error) {
        if (response.message?.contains(NetworkStatus.NoInternet.toString(), true) == true) {
            //show no internet
            SnackBarMessage.makeSnackbar(requireView(), "Check you internet connection")
        } else {
            showEmptyState()
            Log.e(TAG, response.message.toString())
            SnackBarMessage.makeSnackbar(requireView(), response.message.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}