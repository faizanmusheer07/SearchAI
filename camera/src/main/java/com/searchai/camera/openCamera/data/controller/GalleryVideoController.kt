package com.searchai.camera.openCamera.data.controller

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.airbnb.epoxy.AsyncEpoxyController
import com.searchai.camera.R
import com.searchai.camera.openCamera.presentation.epoxy.GalleryVideoRecyclerRow
import com.searchai.common.HomeActivity
import com.searchai.common.models.cameraModels.VideoModel
import com.searchai.common.models.homemodels.commonModels.StoryModel
import com.searchai.common.viewmodel.GalleryViewModel
import java.io.File

class GalleryVideoController(var context: Activity?, var viewModel: GalleryViewModel?) :
    AsyncEpoxyController() {

    override fun buildModels() {
        allVideo.forEachIndexed { index, video ->
            GalleryVideoRecyclerRow(
                videoPath = video,
                onVideoClickListener = {
                    onVideoClick(video)
                }
            ).id(index).addTo(this)
        }
    }

    /**When a Video is selected from gallery to upload.*/
    private fun onVideoClick(path: String) {

        //fetching a file
        val videoFile = File(path)
        Log.d("GalleryVM", videoFile.name)
        //creating a thumbnail
        val bitmap = viewModel!!.setUpThumbnail(videoFile)


        if (viewModel!!.uploadType.value == 1) {
            Log.d("VideoGalleryFragment", "Path of Video: $path")

            viewModel!!.setVideo(VideoModel(bitmap, "", "", videoFile.path, ""))

            (context as HomeActivity).navController
                .navigate(R.id.action_videoGalleryFragment_to_videoUploadFragment)

        } else if (viewModel!!.uploadType.value == 0) {

            Log.d("VideoGalleryFragment", "Path of Viewer: $path")

            viewModel!!.setStory(StoryModel("", bitmap, 36, videoFile.path))

            (context as HomeActivity).navController
                .navigate(R.id.action_videoGalleryFragment_to_videoUploadFragment)

        } else {
            Toast.makeText(context, "Room not enabled", Toast.LENGTH_SHORT).show()
        }


    }

    /**Setting up the VideosList.*/
    var allVideo: ArrayList<String> =
        arrayListOf()
        set(value) {
            field = value
            requestModelBuild()
        }

}