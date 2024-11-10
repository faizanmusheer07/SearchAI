package com.searchai.profile.myprofile.presentation.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.searchai.api.models.auth.response.Profile
import com.searchai.api.utils.Resource
import com.searchai.auth.presentation.AuthViewModel
import com.searchai.profile.R
import com.searchai.profile.databinding.FragmentMyProfileBinding
import com.searchai.profile.followandfollower.presentation.fragment.FollowerAndFollowing
import com.searchai.profile.manage.presentation.fragment.ManageRoom
import com.searchai.profile.setting.presentation.fragment.SettingFragment
import com.searchai.profile.signin.presentation.fragments.SignInFragment
import com.searchai.profile.tablayouts.adapter.ViewPagerAdapter
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.jamshid.library.progress_bar.CircleProgressBar
import kotlin.math.sign

@AndroidEntryPoint
class MyProfileFragment : Fragment(), MotionLayout.TransitionListener,SignInFragment.OnLoggedInListener {
    companion object {
        private const val TAG = "MyProfileFragment"
    }
    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    private var signInFragment: SignInFragment? = null
    private var settingFragment: SettingFragment? = null
    private var manageRoom: ManageRoom? = null
    private var followFollowerFragment:FollowerAndFollowing? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)

       // binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRefreshLayout()
        setupAuthStateObserver()
        setupMotionLayout()
        checkUserLoginStatus()
        setupTabLayout()
        userEvents()
    }


    private fun setupRefreshLayout() {
        binding.swipeRefreshLayout.apply {
            val cp = CircleProgressBar(requireContext())
            cp.setBorderWidth(2)
            setCustomBar(cp)

            setRefreshListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    try {
                        showShimmer()
                        setRefreshing(false)
                    } catch (e: NullPointerException) {
                        Log.e(TAG, "Refresh layout error: ", e)
                    }
                }, 3000)
            }
        }
    }

    private fun setupAuthStateObserver() {
        authViewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {
                    signInFragment?.dismiss()
                    loadUserProfile()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Sign-in failed: ${state.message}", Toast.LENGTH_SHORT).show()
                }
                else -> { /* Do nothing */ }
            }
        }
    }

    private fun checkUserLoginStatus() {
        if (!authViewModel.isUserLoggedIn()) {
            Log.e(TAG,authViewModel.isUserLoggedIn().toString())
            showBottomSheetForLogin()
        } else {
            loadUserProfile()
        }
    }

    private fun showBottomSheetForLogin() {
        signInFragment = SignInFragment(this)
        signInFragment?.show(childFragmentManager, "SignInBottomSheet")
    }

    private fun loadUserProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            val profile = authViewModel.getCurrentUserProfile()
            Log.d(TAG, "Profile data: $profile")
            if (profile != null) {
              //  binding.profile = profile
                handleExistingUser(profile)
                signInFragment?.dismiss()
            } else {
                handleFirstTimeUser()
            }
        }
    }

    private fun handleFirstTimeUser() {
        binding.profileName.text = "Profile Name"
        binding.profileFollowerCount.text = "0"
        binding.profileFollowingCount.text = "0"
        binding.profileChannelName.text = "Channel Name"
        binding.profileBio.text = "Bio text"
    }

    private fun handleExistingUser(profile: Profile) {
        binding.profileName.text = profile.name
        binding.profileFollowerCount.text = profile.followerCount.toString()
        binding.profileFollowingCount.text = profile.followingCount.toString()
        binding.profileChannelName.text = profile.channelName
        Picasso.get().load(profile.profilePicture).error(com.searchai.common.R.drawable.ic_user_defolt_avator).into(binding.profilePicture)
        Picasso.get().load(profile.profilePicture).error(com.searchai.common.R.drawable.ic_user_defolt_avator).into(binding.profilePictureSmall)
        binding.profileBio.text = profile.bio
    }

    private fun showShimmer() {
        // Implement shimmer loading if necessary
    }

    private fun setupMotionLayout() {
        binding.motionLayout.setTransitionListener(this)
    }

    private fun userEvents(){
        binding.settingIcon.setOnClickListener{
            settingFragment = SettingFragment()
            settingFragment?.show(childFragmentManager, "SettingBottomSheet")
        }

        binding.followersText.setOnClickListener {
            openFollowFollowerSheet()
        }

        binding.followingText.setOnClickListener {
            openFollowFollowerSheet()
        }

        binding.btnManage.setOnClickListener {
            manageRoom = ManageRoom()
            manageRoom?.show(childFragmentManager,"ManageRoomSheet")
        }
    }

    private fun openFollowFollowerSheet() {
        followFollowerFragment = FollowerAndFollowing()
        followFollowerFragment?.show(childFragmentManager,"FollowFollowerSheet")
    }

    private fun setupTabLayout() {
        val pagerAdapter = ViewPagerAdapter(requireActivity())
        binding.viewPager2.adapter = pagerAdapter

        TabLayoutMediator(binding.profileTab, binding.viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "Room"
                1 -> "Video"
                2 -> "More"
                else -> null
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        signInFragment = null
        settingFragment = null
        manageRoom = null
        followFollowerFragment = null
    }

    // Implement MotionLayout.TransitionListener methods
    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}
    override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {}
    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {}
    override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}

    override fun userHasLoggedIn() {
        loadUserProfile()
    }
}