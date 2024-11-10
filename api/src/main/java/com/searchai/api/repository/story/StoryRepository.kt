package com.searchai.api.repository.story

import android.media.MediaMetadataRetriever
import com.searchai.common.models.basicModels.ProfileStories
import com.searchai.common.models.basicModels.ViewAndDuration
import com.searchai.api.repository.createMultipartBody
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val storyService: com.searchai.api.services.story.StoryService,
    private val storyGetService: com.searchai.api.services.story.StoryGetService,
) {
    suspend fun like(id: String) {
        storyService.like(com.searchai.api.services.story.model.body.StoryIdBody(id))
    }

    suspend fun getViewAndDuration(id: String): ViewAndDuration {
        return com.searchai.common.mappers.toModel()
    }

    suspend fun getStories(): List<ProfileStories> {
        return storyGetService.getStories().data.map { com.searchai.common.mappers.toModel() }
    }

    suspend fun upload(
        caption: String = "",
        thumbnail: String,
        story: String,
    ) {
        val duration = MediaMetadataRetriever().apply { setDataSource(story) }
            .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!
            .toLong().div(other = 1000)

        storyService.upload(
            title = "story".toRequestBody(MultipartBody.FORM),
            caption = caption.toRequestBody(MultipartBody.FORM),
            duration = duration.toString().toRequestBody(MultipartBody.FORM),
            category = "1".toRequestBody(MultipartBody.FORM),
            thumbnail = File(thumbnail).createMultipartBody(
                formName = "thumbnail",
                contentType = "image/*",
            ),
            upload_file = File(story).createMultipartBody(
                formName = "upload_file",
                contentType = "video/mp4",
            )!!,
        )
    }
}
