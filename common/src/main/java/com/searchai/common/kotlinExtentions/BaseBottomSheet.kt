package com.searchai.common.kotlinExtentions

import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.searchai.common.R
import kotlin.math.floor

abstract class BaseBottomSheet<T : ViewBinding>(
    @LayoutRes private val layoutId: Int,
    private val bind: (View) -> T // A lambda to bind the view
) : BottomSheetDialogFragment() {
    private var _binding: T? = null
    val binding get() = _binding!!

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(layoutId, container, false)
        _binding = bind(view)
        return view
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyInsets()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setLevel(level: Int) {
        val margins = -1 * (level) * 9
        val width = 0.05 * -(1 * (level))
        addMargin(binding.root, margins, (1 - width).toFloat())
    }

    fun setMotionLevel(level: Float) {
        val margins = -1 * (floor(level).toInt()) * 9
        val width = 0.05 * -(1 * (level))
        addMargin(binding.root, margins, (1 - width).toFloat())
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    private fun addMargin(view: View, margins: Int, width: Float) {
        val layoutParams: FrameLayout.LayoutParams =
            view.layoutParams as FrameLayout.LayoutParams
        val margin = margins.toPx().toInt()
        layoutParams.setMargins(margin, 0, margin, 0)

        view.layoutParams = layoutParams
        view.requestLayout()

        val displayRectangle = Rect()
        this.requireDialog().window!!
            .setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.transparent)))
        val window: Window = requireActivity().window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        this.requireDialog().window!!.setLayout(
            if (width == 1F) -1 else (displayRectangle.width() * width).toInt(),
            -1
        )
    }

    fun Number.toPx() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

    @RequiresApi(Build.VERSION_CODES.R)
    fun setUpFullScreen(dialog: BottomSheetDialog, dp: Int) {
        val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val behaviour = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val activityHeight = requireActivity().display?.height!!
        layoutParams.height = activityHeight - dp
        Log.d("BaseBottomSheet", "final height= ${layoutParams.height}")
        bottomSheet.layoutParams = layoutParams
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        val displayMatrix = DisplayMetrics()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = requireActivity().display
            display?.getRealMetrics(displayMatrix)
            displayMatrix.heightPixels
        } else {
            @Suppress("DEPRECATION")
            val display = requireActivity().windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(displayMatrix)
            displayMatrix.heightPixels
        }
    }

    protected var systemBarInsetsEnabled: Boolean = true
        set(value) {
            field = value
            view?.let { ViewCompat.requestApplyInsets(it) }
        }

    private fun applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(requireView()) { view, insets ->
            if (systemBarInsetsEnabled) {
                val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    updateMargins(top = systemBarInsets.top)
                }
            }

            insets
        }
    }
}
