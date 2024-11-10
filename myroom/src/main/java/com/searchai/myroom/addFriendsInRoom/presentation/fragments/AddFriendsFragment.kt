//package com.searchai.myroom.addFriendsInRoom.presentation.fragments
//
//import android.app.Dialog
//import android.content.DialogInterface
//import android.content.Intent
//import android.content.pm.ActivityInfo
//import android.content.pm.PackageManager
//import android.content.pm.ResolveInfo
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.lifecycleScope
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.google.android.material.bottomsheet.BottomSheetBehavior
//import com.google.android.material.bottomsheet.BottomSheetDialog
//import com.searchai.api.utils.Resource
//import com.searchai.common.kotlinExtentions.base.bottomsheet.BaseBottomSheet
//import com.searchai.common.kotlinExtentions.base.bottomsheet.BottomSheetLevelInterface
//import com.searchai.myroom.R
//import com.searchai.myroom.addFriendsInRoom.domain.models.PreviousProfile
//import com.searchai.myroom.addFriendsInRoom.domain.models.User
//import com.searchai.myroom.addFriendsInRoom.presentation.epoxy.FriendsEpoxyController
//import com.searchai.myroom.addFriendsInRoom.presentation.viewmodel.FollowFollowingViewModel
//import com.searchai.myroom.createRoom.presentation.fragments.CreateRoomFragment
//import com.searchai.myroom.databinding.FragmentAddFriendsBinding
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.async
//import kotlinx.coroutines.awaitAll
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.launch
//
//@AndroidEntryPoint
//class AddFriendsFragment(
//    private val levelInterface: BottomSheetLevelInterface
//) : BaseBottomSheet<FragmentAddFriendsBinding>(
//    R.layout.fragment_select_room_category,
//    FragmentAddFriendsBinding::inflate
//) {
//    var count = 0
//    private var settingBottomSheetHeight: Int = 80
//
//    companion object {
//        private const val TAG = "AddFriendsFragment"
//    }
//    private val followingViewModel : FollowFollowingViewModel by viewModels()
//
//    private lateinit var epoxyControllerPrevious: FriendsEpoxyController
//    private lateinit var epoxyControllerFollower: FriendsEpoxyController
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setupRecyclerView()
////        fetchPreviousUsers()
//        fetchFollowers()
//
//        binding.connectButton.setOnClickListener {
//            val link =
//                "https://www.myworld.com/liveRoom?code=${CreateRoomFragment.current_room_code}"  //add room code
//            val pm: PackageManager = requireActivity().packageManager
//            var finalLaunchables: ResolveInfo? = null
////            val recAdapter= RecAdapter(pm,finalLaunchables,requireContext(),shareBody)
//            val main = Intent(Intent.ACTION_MAIN, null)
//            main.addCategory(Intent.CATEGORY_LAUNCHER)
//            val launchables: List<ResolveInfo> = pm.queryIntentActivities(main, 0)
//            for (resolveInfo in launchables) {
//                val packageName = resolveInfo.activityInfo.packageName
//                if (packageName.contains("com.whatsapp")) {
//                    finalLaunchables = resolveInfo
//                }
//            }
//            if (finalLaunchables != null) {
//                val launchable = finalLaunchables as ResolveInfo
//                val activity: ActivityInfo = launchable.activityInfo
//                val i = Intent(Intent.ACTION_SEND)
//                i.type = "text/plain"
//                val packageName = activity.packageName
//                i.putExtra(Intent.EXTRA_TEXT, link)
//                i.setPackage(packageName)
//                context?.startActivity(i)
//            } else {
//                Toast.makeText(
//                    context,
//                    "Whatsapp is not installed in your device",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//        }
//
////        binding.editTextSearch.setOnClickListener {
////            val searchFragment = SearchUsersFragment(this@ShareFragment)
////            searchFragment.show(parentFragmentManager, searchFragment.tag)
////        }
//    }
//
//    /**this function fetch the users follower list and call a function called handleFollowers()
//     * which take list of followers and then iterate ech followers
//     * and get the follower profile using profileRepository.getProfile(follower.id) method*/
//    private suspend fun fetchFollowers() {
//        Log.e(TAG, "Inside fetchFollowers()")
//
//        // Trigger the ViewModel to fetch followers
//        followingViewModel.getFollower(userIdentifierPreferences.id) //use datastore userid
//
//        // Observe the ViewModel's followerState
//        followingViewModel.followerState.collectLatest { resource ->
//            when (resource) {
//                is Resource.Success -> {
//                    val followers  = resource.data //?: emptyList()
//                    handleFollowers(followers!!) // Handle followers data
//                }
//                is Resource.Error -> {
//                    // Display the error message
////                    toast(resource.message.orEmpty())
//                    Log.e(TAG, "Error fetching followers: ${resource.message}")
//                }
//                else -> {
//                    // Show loading indicator if necessary
//                    Log.d(TAG, "Loading followers...")
//                }
//            }
//        }
//    }
//
//    private fun handleFollowers(followers: List<User>) {
//        if (followers.isNotEmpty()) {
//            val data = ArrayList<PreviousProfile>()
//
//            viewLifecycleOwner.lifecycleScope.launch {
//                // Fetch profiles in parallel using async calls
//                followers.map { follower ->
//                    async {
//                        runCatching {
//                            val profile = profileRepository.getProfile(follower.id)
//                            PreviousProfile(
//                                follower.id,
//                                follower.name,
//                                follower.channelName,
//                                follower.profilePicture,
//                                profile.followerCount,
//                                profile.followingCount,
//                                profile.expertise
//                            )
//                        }.getOrNull()
//                    }
//                }.awaitAll().filterNotNull().let { profileList ->
//                    data.addAll(profileList)
//                }
//
//                Log.e(TAG, "Fetched Followers Data: $data")
//
//                // Update the UI
//                if (data.isNotEmpty()) {
//                    binding.apply {
//                        rlFollowers.visibility = View.VISIBLE
//                    }
//                    epoxyControllerFollower.setData(data)
//                } else {
//                    binding.apply {
//                        // Show no followers UI or handle empty state
//                    }
//                }
//            }
//        } else {
//            // Handle case where there are no followers
//            binding.apply {
//                // Show no followers UI or handle empty state
//            }
//        }
//    }
//
//    /** this function shows that the user previously create room or note */
//    private fun fetchPreviousUsers(){
//        Log.e(TAG, "Inside previousUsers()")
//        val previousUsers = viewLifecycleOwner.lifecycleScope.async {
//            Log.e(
//                TAG,
//                "fetchRooms: ${roomRepository.previousRoomUsers()}"
//            )
//            runCatching { roomRepository.previousRoomUsers() }
//                .onFailure {
//                    toast(it.message.orEmpty())
//                }
//                .getOrDefault(emptyList())
//        }
//        viewLifecycleOwner.lifecycleScope.launch {
//            val users = previousUsers.await()
//            Log.e(TAG, "fetchRooms: $users")
//            if (users.isNotEmpty()) {
//                binding.apply {
//                    rlFriends.visibility=View.VISIBLE
//                }
//                epoxyControllerPrevious.setData(users)
//                //buildRoomModels(users)
//            } else {
//                binding.apply {
//                    rlFriends.visibility=View.GONE
//                }
//            }
//        }
//    }
//
//
//    /** When the datastore set then pass the userIdentifierPreferences.id (currentUserId) */
//    private fun setupRecyclerView() {
//        binding.friends.apply {
//            layoutManager = LinearLayoutManager(context)
//        }
////        epoxyControllerPrevious = FriendsEpoxyController(userIdentifierPreferences.id,requireContext())
//
//        binding.friends.setController(epoxyControllerPrevious)
//        binding.followers.apply {
//            layoutManager = LinearLayoutManager(context)
//        }
////        epoxyControllerFollower = FriendsEpoxyController(userIdentifierPreferences.id,requireContext())
//
//        binding.followers.setController(epoxyControllerFollower)
//
//    }
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//
//        dialog.setOnShowListener {
//            val bottomSheetDialog = it as BottomSheetDialog
//            setUpFullScreen(bottomSheetDialog, settingBottomSheetHeight + 6)
//        }
//
//        (dialog as BottomSheetDialog).behavior.addBottomSheetCallback(object :
//            BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                if (newState == BottomSheetBehavior.STATE_SETTLING) {
//                    count++
//                    if (count % 2 == 0) {
//                        dismiss()
//                    }
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//            }
//        })
//        return dialog
//    }
//
//    override fun onStart() {
//        super.onStart()
//        val behavior = BottomSheetBehavior.from(requireView().parent as View)
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED
//    }
//
//    override fun onDismiss(dialog: DialogInterface) {
//        super.onDismiss(dialog)
//        try {
//            levelInterface?.onSheet1Dismissed()
//        } catch (e: Exception) {
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//    }
//
//    fun onSheet2Dismissed() {
//        setLevel(0)
//        levelInterface?.onSheet2Dismissed()
//    }
//
//    fun onSheet2Created() {
//        setLevel(-1)
//        levelInterface?.onSheet2Created()
//    }
//
//    fun onSheet1Dismissed() = Unit
//
//    fun getHeightOfBottomSheet(height: Int) {
//        binding.shareConstraint.layoutParams.height = height + 10.toPx().toInt()
//    }
//}