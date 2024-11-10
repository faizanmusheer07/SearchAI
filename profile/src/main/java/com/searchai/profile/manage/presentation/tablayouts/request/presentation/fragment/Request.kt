package com.searchai.profile.manage.presentation.tablayouts.request.presentation.fragment

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
import com.searchai.profile.databinding.FragmentManageRequestsTabBinding
import com.searchai.profile.databinding.FragmentManageRoomTabBinding
import com.searchai.profile.manage.presentation.tablayouts.live.presentation.epoxy.LiveRoomController
import com.searchai.profile.manage.presentation.tablayouts.live.presentation.fragment.Live
import com.searchai.profile.manage.presentation.tablayouts.live.presentation.fragment.Live.Companion
import com.searchai.profile.manage.presentation.tablayouts.live.presentation.viewmodel.LiveRoomViewModel
import com.searchai.profile.manage.presentation.tablayouts.request.domain.model.RequestModel
import com.searchai.profile.manage.presentation.tablayouts.request.presentation.epoxy.RequestController
import com.searchai.profile.manage.presentation.tablayouts.request.presentation.viewmodel.RequestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Request: Fragment(){

    companion object{
        private val TAG = "Requests"
    }

    private var _binding: FragmentManageRequestsTabBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RequestViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var controller: RequestController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManageRequestsTabBinding.inflate(inflater, container, false)
        viewLifecycleOwner.lifecycleScope.launch {
            val profile = authViewModel.getCurrentUserProfile()
            Log.d(TAG, "Profile data: $profile")
            if (profile != null) {
                //Todo call request here need room id
               // viewModel.fetchRequestResponse(profile.)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = RequestController(onClickCallback = {onClick->
            SnackBarMessage.makeSnackbar(requireView(), "Click")
        })
        binding.requestsRecyclerView.setController(controller)
        binding.requestsRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false)
        binding.requestsRecyclerView.addItemDecoration(
            DividerItemDecoration(requireActivity(), RecyclerView.VERTICAL)
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.request.collect { response->
                    when(response){
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

    private fun showSuccessState(response: Resource.Success<List<RequestModel>>) {
        hideShimmer()

        if (response.data?.isEmpty() == true){
            showEmptyState()
        }else{
            controller.request = response.data!!
        }
    }

    private fun hideShimmer() {
        binding.requestRoomShimmer.visibility = View.GONE
        binding.requestsRecyclerView.visibility = View.VISIBLE
    }

    private fun showShimmer() {
        hideEmptyState()
        binding.requestRoomShimmer.visibility = View.VISIBLE
        binding.requestsRecyclerView.visibility = View.GONE
    }

    private fun showEmptyState(){
        binding.noRequestsLabel.visibility = View.VISIBLE
        binding.manageRequestsText.visibility = View.VISIBLE
    }

    private fun hideEmptyState(){
        binding.noRequestsLabel.visibility = View.GONE
        binding.manageRequestsText.visibility = View.GONE
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