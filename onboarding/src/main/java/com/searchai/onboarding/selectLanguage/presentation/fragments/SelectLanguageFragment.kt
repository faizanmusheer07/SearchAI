package com.searchai.onboarding.selectLanguage.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
//import com.searchai.myworld.R
import com.searchai.onboarding.databinding.FragmentSelectLanguageBinding

class SelectLanguageFragment : Fragment() {

    private var _binding: FragmentSelectLanguageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentSelectLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.selectBtn.setOnClickListener { onClickNext() }
    }

    private fun onClickNext() {
        findNavController().navigate(com.searchai.profile.R.id.profile_nav_graph)
    }
}