package com.searchai.myroom.createHub.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.searchai.myroom.R
import com.searchai.myroom.databinding.FragmentCreateHubBinding


class CreateHubFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentCreateHubBinding? = null
    private val binding get() = _binding!!
    companion object{
        private const val TAG="CreateHubFragment"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            binding.addTopic.setOnClickListener{
                findNavController().navigate(R.id.action_createHubFragment_to_selectRoomCategoryFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentCreateHubBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}