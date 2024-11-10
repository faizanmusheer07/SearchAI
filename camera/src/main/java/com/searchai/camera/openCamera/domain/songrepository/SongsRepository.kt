package com.searchai.camera.openCamera.domain.songrepository



import com.searchai.api.extensions.getOrThrow
import com.searchai.api.services.explore.ExploreService
import com.searchai.common.models.cameraModels.Audio
import com.searchai.common.mappers.toAudio
import javax.inject.Inject

class SongsRepository @Inject constructor(
    private val exploreApi: ExploreService,
) {
    suspend fun getSongs(): ArrayList<Audio> {
        return exploreApi.getSongs().getOrThrow().message.filter { it.value.isNotEmpty() }.toAudio()
    }
}
