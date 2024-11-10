package com.searchai.explore

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.*
import android.view.ViewGroup
import com.searchai.explore.databinding.FragmentExploreBinding
import android.view.View.VISIBLE

class ExploreFragment : Fragment() {
    private var _binding : FragmentExploreBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupRefreshLayout(){
        binding.swipeRefreshLayout.apply {
            setRefreshListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    try {
                        showShimmer()
                        setRefreshing(false)
                    }catch (e:Exception){
                        Log.e(TAG,"Refresh layout error: ",e)
                    }
                },3000)
            }
        }
    }

    private fun showShimmer(){

//        binding.exploreShimmer.visibility = View.VISIBLE // Show the shimmer layout
//        binding.exploreShimmer.startShimmer() // Start shimmer animation
        
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}