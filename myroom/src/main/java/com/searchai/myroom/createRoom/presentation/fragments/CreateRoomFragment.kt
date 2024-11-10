package com.searchai.myroom.createRoom.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.searchai.myroom.R
import com.searchai.myroom.databinding.FragmentCreateRoomBinding


class CreateRoomFragment : Fragment() {
    private var _binding: FragmentCreateRoomBinding? = null
    private val binding get() = _binding!!
    companion object{
        private const val TAG="CreateRoomFragment"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            binding.selectStoryIcon.setOnClickListener {
//                findNavController().navigate(R.id.action_createRoomFragment_to_cameraFragment)
            }
            binding.createHubIcon.setOnClickListener{
                findNavController().navigate(R.id.action_createRoomFragment_to_createHubFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}