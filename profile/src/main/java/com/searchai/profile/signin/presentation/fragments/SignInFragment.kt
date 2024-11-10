package com.searchai.profile.signin.presentation.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.searchai.auth.presentation.AuthViewModel
import com.searchai.profile.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.searchai.api.utils.Resource
import com.searchai.profile.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignInFragment(listener:OnLoggedInListener) : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "SignInBottomSheet"
        private const val EMAIL = "email"
    }

    private val authViewModel by viewModels<AuthViewModel>()
    private var listenerSheet:OnLoggedInListener? = null

    init {
        this.listenerSheet = listener
    }

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val isUserLoggedIn: Boolean
        get() = authViewModel.isUserLoggedIn()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            DialogFragment.STYLE_NO_FRAME,
            com.searchai.common.R.style.AppBottomSheetDialogTheme
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listenerSheet = context as OnLoggedInListener
        }catch (e:ClassCastException){
            Log.e(TAG,"No listener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeAuthState()
        setupTermsAndConditions()
    }

    private fun setupClickListeners() {
        binding.roomNoticeDropdownIcon.setOnClickListener { dismiss() }

        binding.signInButton.setOnClickListener {
            startGoogleSignIn()
        }

        binding.facebookSignIn.setOnClickListener {
            startFacebookSignIn()
        }

        binding.textView8.setOnClickListener { dismiss() }
    }

    private fun startGoogleSignIn() {
        binding.signInButton.visibility = View.INVISIBLE
        binding.btnProgress.isVisible = true
        authViewModel.googleSignIn(requireContext())
    }

    private fun startFacebookSignIn() {
        binding.facebookSignIn.visibility = View.INVISIBLE
        binding.btnProgressFacebook.isVisible = true
        // Implement Facebook sign-in logic here
        // For example: authViewModel.facebookSignIn(requireContext())
    }

    private fun observeAuthState() {
        authViewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {
                    Log.d(TAG, "signIn success: ${state.data}")
                    resetButtonStates()
                    Toast.makeText(requireContext(), "Sign-in successful", Toast.LENGTH_SHORT)
                        .show()
                    viewLifecycleOwner.lifecycleScope.launch {
                        val profile = state.data?.data?.profile
                        Log.d(TAG, "Profile: $profile")
                        if (profile != null) {
                            Log.d(TAG, "Profile is not null")
                            authViewModel.saveProfile(profile)
                            Log.d(TAG, authViewModel.getCurrentUserProfile().toString())
                            val token = state.data?.data?.token
                            if (token != null) {
                                Log.d(TAG, "Token before storing: $token")
                                authViewModel.storeAccessToken(token.accessToken)
                                Log.d(TAG, token.accessToken)
                                listenerSheet?.userHasLoggedIn()
                                dismiss()
                            }
                        } else {
                            Log.d(TAG, "Profile is null")
                            dismiss()
                        }
                    }

                }

                is Resource.Error -> handleAuthError(state.message)
                else -> { /* Do nothing */
                }
            }
        }
    }

    private fun handleAuthError(message: String?) {
        Log.d(TAG, "signIn error: $message")
        resetButtonStates()
        Toast.makeText(requireContext(), message ?: "Sign-in failed", Toast.LENGTH_SHORT).show()
    }

    private fun resetButtonStates() {
        binding.signInButton.isEnabled = true
        binding.signInButton.isVisible = true
        binding.btnProgress.isVisible = false
        binding.facebookSignIn.isEnabled = true
        binding.btnProgressFacebook.isVisible = false
    }

    private fun setupTermsAndConditions() {
        binding.termsAndCondition.movementMethod = LinkMovementMethod.getInstance()
        binding.termsAndCondition.setLinkTextColor(
            ContextCompat.getColor(requireContext(), com.searchai.common.R.color.cpDark)
        )
    }

    override fun onStart() {
        super.onStart()
        if (isUserLoggedIn) {
            findNavController().navigateUp()
        }
    }

    override fun onStop() {
        super.onStop()
        if (findNavController().currentDestination?.label == "MyProfileFragment") {
            findNavController().navigateUp()
        }
    }

    interface OnLoggedInListener{
        fun userHasLoggedIn()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}