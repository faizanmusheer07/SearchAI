package com.searchai.onboarding.getStarted.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.searchai.onboarding.R
import com.searchai.onboarding.databinding.FragmentGetStartedBinding


class GetStartedFragment : Fragment() {
    companion object{
        private const val TAG="GetStartedFragment"
    }
    private var _binding: FragmentGetStartedBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

            Log.d(TAG, "Reached at the end of GetStarted Fragment")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGetStartedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createHubBtn.setOnClickListener { onClickNext() }
    }

    private fun onClickNext() {
        findNavController().navigate(R.id.action_getStartedFragment_to_interestsFragment)
    }
}