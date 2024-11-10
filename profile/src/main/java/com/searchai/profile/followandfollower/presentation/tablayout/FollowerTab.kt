package com.searchai.profile.followandfollower.presentation.tablayout

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
import androidx.recyclerview.widget.RecyclerView
import com.searchai.api.utils.Resource
import com.searchai.auth.presentation.AuthViewModel
import com.searchai.common.kotlinExtentions.constants.NetworkStatus
import com.searchai.common.kotlinExtentions.helper.SnackBarMessage
import com.searchai.profile.databinding.FragmentFollowerTabBinding
import com.searchai.profile.followandfollower.domain.model.FollowItemModels
import com.searchai.profile.followandfollower.presentation.epoxy.FollowerEpoxyController
import com.searchai.profile.followandfollower.presentation.viewmodel.FollowViewModel
import com.searchai.profile.manage.presentation.tablayouts.live.presentation.fragment.Live
import com.searchai.profile.manage.presentation.tablayouts.live.presentation.fragment.Live.Companion
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowerTab : Fragment() {

    companion object {
        const val TAG = "followerTab"
    }

    private var _binding: FragmentFollowerTabBinding? = null
    private val binding get() = _binding!!

    private val followViewModel: FollowViewModel by viewModels()
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var controller: FollowerEpoxyController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowerTabBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            val profile = authViewModel.getCurrentUserProfile()
            Log.d(TAG, "Profile data: $profile")
            if (profile != null) {
                followViewModel.fetchFollowersStream(profile.userId)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEpoxy()
        setupObserver()
    }

    private fun setupEpoxy() {
        controller = FollowerEpoxyController(onClickCallBack = { data ->
            SnackBarMessage.makeSnackbar(requireView(), data.name)
        })
        binding.recyclerFollowerFollowing.setController(controller)
        binding.recyclerFollowerFollowing.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                RecyclerView.VERTICAL
            )
        )
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                followViewModel.followers.collectLatest { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            showShimmer()
                        }

                        is Resource.Error -> {
                            hideShimmer()
                            errorState(resource)
                        }

                        is Resource.Idle -> {}
                        is Resource.Success -> {
                            showSuccessState(resource)
                        }
                    }
                }
            }
        }
    }

    private fun showSuccessState(response: Resource.Success<List<FollowItemModels>>) {
        hideShimmer()

        if (response.data?.isEmpty() == true) {
            showEmptyState()
        } else {
            controller.followFollowing = response.data!!
        }
    }

    private fun hideShimmer() {

        binding.shimmerFollower.visibility = View.GONE
        binding.recyclerFollowerFollowing.visibility = View.VISIBLE
    }

    private fun showShimmer() {
        hideEmptyState()
        binding.shimmerFollower.visibility = View.VISIBLE
        binding.recyclerFollowerFollowing.visibility = View.GONE
    }

    private fun showEmptyState() {
        binding.noRequestsLabel.visibility = View.VISIBLE
        binding.manageRequestsText.visibility = View.VISIBLE
    }

    private fun hideEmptyState() {
        binding.noRequestsLabel.visibility = View.GONE
        binding.manageRequestsText.visibility = View.GONE
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