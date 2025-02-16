package com.searchai.common

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.searchai.common.viewmodel.BaseViewModel
import com.searchai.explore.data.repository.ExploreRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), ToFlowNavigable,
    NavController.OnDestinationChangedListener {

    companion object {
        private const val TAG = "HomeActivity"

        private const val VIDEO_CHANNEL_ID = "0"
        private const val PROFILE_CHANNEL_ID = "1"
    }

    val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment).navController
    }

    private var _binding: ActivityHomeBinding? = null
    val binding get() = _binding!!

    val baseViewModel: BaseViewModel by viewModels()
    val commentsViewModel: CommentsViewModel by viewModels()

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var exploreRepository: ExploreRepository

    @Inject
    lateinit var amazonSNSImpl: AmazonSNSImpl

    var profileDrawable: Drawable? = null

    lateinit var mGoogleSignInClient: GoogleSignInClient

    private var hideBottomNavInPrevFragment = false

    //val clientID = "909136434512-cca9i6u7cjo19l8583l6ho5q6c5h7ccf.apps.googleusercontent.com"
    val clientID = "909136434512-ua5aj7mrto8vv544vao08khihm9m0a2v.apps.googleusercontent.com"
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(clientID)
        .requestEmail()
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        lifecycleScope.launch{
           // baseViewModel.getSubCategoryCode()
        }

        // Navigate to on-boarding screen if applicable
        lifecycleScope.launch {
            baseViewModel.isOnBoardingFinished
                .filterNotNull()
                .distinctUntilChanged()
                .collectLatest { isOnBoardingFinished ->
                    if (!isOnBoardingFinished) {
                        navController.navigate(MainNavGraphDirections.actionGlobalSuggestionFlow())
                        //navController.navigate(MainNavGraphDirections.actionGlobalNoInternetFlow())
                    }
                }
        }


        observeTheme()
        applyWindowInsets()
        observeNetworkState()
        navController.addOnDestinationChangedListener(this)

        navigator.navController = navController
        navigator.activity = this

        binding.bottomNavigation.itemIconTintList = null
        binding.bottomNavigation.itemTextColor = null
        binding.bottomNavigation.setupWithNavController(navController)
        val menuViewb = binding.bottomNavigation.getChildAt(0) as BottomNavigationMenuView
        val itemViewb = menuViewb.getChildAt(3) as BottomNavigationItemView

        val notificationBadge = layoutInflater.inflate(
            R.layout.view_notification_badge,
            menuViewb,
            false
        )

        itemViewb.addView(notificationBadge)
        lifecycleScope.launch {
            baseViewModel.isBadgeVisible.collectLatest { showBadge ->
                notificationBadge.isVisible = showBadge
            }
        }

        val createItem = menuViewb.getChildAt(2) as BottomNavigationItemView
        val createIconView = createItem[0]
        createIconView.layoutParams = createIconView.layoutParams.apply {
            val displayMetrics: DisplayMetrics = resources.displayMetrics
            height = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 40f, displayMetrics
            ).toInt()
            width = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 40f, displayMetrics
            ).toInt()
        }

        /* Setting custom listener on this icon,
         since on clicking it, the original navGraph is reset */
        createIconView.setOnClickListener {
            navigateToFlow(NavigationFlow.CameraFlow)
        }

        /*Getting Device ID.*/
        val deviceId =
            Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID)
                .toString()
        baseViewModel.setDeviceId(deviceId)
        Log.d(TAG, "Device Id: $deviceId")

        baseViewModel.id.observe(this) {
            amazonSNSImpl.userId = it
            amazonSNSImpl.getEndpoint(it)
            Log.d(TAG, "user id: $it")
        }

        Log.d(TAG, "user id: ${baseViewModel.id}")

        //checking if user is signed in or not
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        baseViewModel.checkUserSignedIn()

        baseViewModel.authFlag.observe(this) {
            if (it) {
                Log.d(TAG, "Auth Flag is now :$it")
                baseViewModel.loadUserInfo()
            } else {
                Log.d(TAG, "Auth Flag is now :$it")
            }
        }

//        setUpFirstNotifications()

        setUpNavIcon()


    }


    override fun onResume() {
        super.onResume()

        val uri = intent.data
        if (uri != null){

            Toast.makeText(this, "$uri", Toast.LENGTH_SHORT).show()
            (this as ToFlowNavigable).navigateToFlow(
                NavigationFlow.GlobalProfileFlow
            )
        }

    }

    private fun setUpFirstNotifications() {
        val notification = ArrayList<MyWorldNotification>()
        val notif = LocalNotifBuilder(this, PROFILE_CHANNEL_ID)
        notif.createNotificationChannel()

        baseViewModel.authFlag.observe(this) { auth ->
            if (auth) {
                lifecycleScope.launch {
                    try {
                        val res = exploreRepository.getNotifications().data.myWorldNotifications
                        res.forEach {
                            notification.add(it)
                            notif.buildNotification(
                                it.username,
                                it.message,
                                Integer.parseInt(it.id)
                            )
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "unable to fetch notifications", e)
                    }
                }
            } else {
                lifecycleScope.launch {
                    try {
                        val res = exploreRepository.getNotificationPlease()
                            .body()?.data?.myWorldNotifications
                        res?.forEach {
                            notification.add(it)
                            notif.buildNotification(
                                it.username,
                                it.message,
                                Integer.parseInt(it.id)
                            )
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "unable to fetch notifications", e)
                    }
                }
            }
        }
    }

    private fun observeTheme() {
        lifecycleScope.launch {
            baseViewModel.isDarkThemeEnabled.collectLatest { isDarkThemeEnabled ->
                if (isDarkThemeEnabled) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                updateStatusBarAppearance()
            }
        }
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.noInternetView) { view, windowInsets ->
            val systemBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBarInsets.top)
            windowInsets
        }

        // Prevent bottom navigation bar from consuming insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigation) { _, windowInsets ->
            windowInsets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(bottom = insets.bottom)
            windowInsets
        }
    }

    private fun setUpNavIcon() {
        lifecycleScope.launch {
            baseViewModel.profile.filterNotNull().collectLatest {
                setUpNavProfileIcon(it.profilePicture, null, false)
            }
        }
    }

    fun setUpNavProfileIcon(profileImage: String?, bitmap: Drawable?, profileFragment: Boolean) {
        if (!baseViewModel.isLoggedIn)
            return

        val menu = binding.bottomNavigation.menu
        val menuItem = menu.findItem(R.id.profile_flow)
            Log.d(TAG,"Inside setupnavicon")
        if (bitmap == null) {
            Glide.with(this@HomeActivity)
                .asBitmap()
                .load(profileImage)
                .apply(
                    RequestOptions
                        .circleCropTransform()
                        .placeholder(R.drawable.navigation_profile_selector)
                )
                .into(object : SimpleTarget<Bitmap>() {
                    @SuppressLint("RestrictedApi")
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?,
                    ) {
                        if (profileFragment) {
                            menuItem?.icon = BitmapDrawable(
                                resources,
                                baseViewModel.addBorderToCircularBitmap(
                                    resource,
                                    10,
                                    getColor(R.color.cpTextDark)
                                )

                            )
                        } else {
                            profileDrawable = BitmapDrawable(
                                resources,
                                baseViewModel.addBorderToCircularBitmap(
                                    resource,
                                    10,
                                    Color.TRANSPARENT
                                )
                            )
                            menuItem?.icon = profileDrawable
                        }
                    }

                })
        } else {
            menuItem?.icon = bitmap
        }
    }


    override fun onNavigateUp() = navController.navigateUp()

    override fun navigateToFlow(flow: NavigationFlow) {
        navigator.navigateToFlow(flow)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.PERMISSION_CODE &&
            (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)
        ) {
            baseViewModel.setIsPermission(true)
        }
    }

    fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                profileDrawable = null
                baseViewModel.nukeUserAtSignOut()
                binding.bottomNavigation.menu.findItem(R.id.profile_flow).icon = resources.getDrawable(R.drawable.navigation_profile_selector)
//                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()
            }

    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        // These updates rely on current nav destination
        updateBottomNavBarVisibility()
        updateStatusBarAppearance()

        Log.d(TAG, "Destination change: ${destination.label}")
    }

    /**
     * Sets status bar appearance to either dark or light.
     * A light status bar has dark foreground color (icons) and a dark status bar has a light foreground.
     *
     * @param lightStatusBar whether to use a light status bar.
     */
    private fun setLightStatusBar(lightStatusBar: Boolean) {
        val insetsController = WindowCompat.getInsetsController(
            window,
            findViewById(android.R.id.content)
        )
        insetsController.isAppearanceLightStatusBars = lightStatusBar
       // insetsController?.isAppearanceLightNavigationBars = lightStatusBar

        /*
        Remain True irrespective
         */
        insetsController.isAppearanceLightNavigationBars = true
    }

    /**
     * Updates status bar appearance based on current nav destination or dark mode configuration.
     *
     * A destination can request a specific status bar color by setting one of the
     * [R.string.nav_arg_force_light_status_bar] or [R.string.nav_arg_force_dark_status_bar]
     * arguments to true
     *
     * If current destination does not require a specific status bar color, status bar color is decided
     * based on whether the app is in dark theme.
     */
    private fun updateStatusBarAppearance() {
        val navArgs = navController.currentBackStackEntry?.arguments

        val forceLightStatusBar = navArgs?.getBoolean(
            getString(R.string.nav_arg_force_light_status_bar)
        ) == true

        val forceDarkStatusBar = navArgs?.getBoolean(
            getString(R.string.nav_arg_force_dark_status_bar)
        ) == true

        when {
            forceLightStatusBar -> setLightStatusBar(true)
            forceDarkStatusBar -> setLightStatusBar(false)
            // Set according to theme if destination does not require a specific status bar color
            else -> setLightStatusBar(!baseViewModel.isDarkThemeEnabled.value)
        }
    }

//    fun showRoomSlideDownNotification(){
//        val dialogFragment = SlideDownDialogFragment2()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.frameLayout_top_slide_down,
//            dialogFragment, "Home_Fragment")
//            .commit()
//
//    }

    /**
     * Sets visibility of bottom navigation bar based on the value of [R.string.nav_arg_hide_bottom_nav]
     * argument in the current destination
     *
     * Defaults to showing the bottom navigation bar if the argument is not set.
     * This argument must be set to true for the destinations where bottom navigation bar
     * is not intended to be shown.
     */
    private fun updateBottomNavBarVisibility() {
        val navArgs = navController.currentBackStackEntry?.arguments
        val argKeyHideBottomNav = getString(R.string.nav_arg_hide_bottom_nav)

        var hideBottomNav = navArgs?.getBoolean(argKeyHideBottomNav, false) == true

        // check if the fragment before SignInFragment has hidden BottomNav
        if (hideBottomNavInPrevFragment && navController.currentDestination?.label == "SignInFragment") {
            // Then hide the BottomNav for SignInFragment too
            hideBottomNav = true
        }

        binding.bottomNavigation.isGone = hideBottomNav
        hideBottomNavInPrevFragment = hideBottomNav // store BottomNav hidden status of prev fragment
    }

    private fun observeNetworkState() {
        lifecycleScope.launch {
            baseViewModel.isInternetAvailable.collectLatest { isInternetAvailable ->
                binding.fragmentContainerView.isVisible = isInternetAvailable
                binding.noInternetView.isVisible = !isInternetAvailable
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}