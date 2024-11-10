package com.searchai.myroom.dependencyInjection

import com.searchai.api.services.followfollowing.FollowFollowingService
import com.searchai.api.services.myroom.MyRoomService
import com.searchai.myroom.addFriendsInRoom.data.repository.FollowFollowingRepositoryImpl
import com.searchai.myroom.addFriendsInRoom.domain.repository.FollowFollowingRepository
import com.searchai.myroom.createHub.data.repository.CreateRoomRepoImpl
import com.searchai.myroom.createHub.domain.repository.CreateRoomRepo
import com.searchai.myroom.selectCategories.data.repository.MyRoomRepositoryImpl
import com.searchai.myroom.selectCategories.domain.repository.MyRoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyRoomModule {
    @Provides
    @Singleton
    fun provideMyRoomRepository(
        myRoomService: MyRoomService
    ) :MyRoomRepository{
        return MyRoomRepositoryImpl(myRoomService)
    }
    @Provides
    @Singleton
    fun providesCreateRoomRepo(
        myRoomService: MyRoomService
    ): CreateRoomRepo = CreateRoomRepoImpl(
        myRoomService
    )

    @Provides
    @Singleton
    fun providesFollowFollowingRepository(
        followingService: FollowFollowingService
    ): FollowFollowingRepository = FollowFollowingRepositoryImpl(
        followingService = followingService
    )
}