package com.searchai.messaging.peertopeerChat.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.searchai.auth.presentation.AuthViewModel
import com.searchai.messaging.R
import com.searchai.messaging.databinding.FragmentPeerToPeerChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeerToPeerChatFragment : Fragment() {

    private val authViewModel by viewModels<AuthViewModel>()

    private var _binding: FragmentPeerToPeerChatBinding ? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verifyLogin()
    }

    private fun verifyLogin() {
        if (!authViewModel.isUserLoggedIn()){

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeerToPeerChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}