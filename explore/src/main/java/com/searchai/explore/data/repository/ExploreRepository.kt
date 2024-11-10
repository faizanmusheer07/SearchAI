package com.searchai.explore.data.repository

import android.util.Log
import com.searchai.api.services.explore.ExploreInfoService
import com.searchai.api.services.explore.ExploreRecordService
import com.searchai.api.services.explore.ExploreService
import java.io.IOException

class ExploreRepository @Inject constructor(
    private val exploreService: ExploreService,
    private val userIdentifierPreferences: UserIdentifierPreferences,
    private val exploreInfoService: ExploreInfoService,
    private val exploreRecordService: ExploreRecordService,
) {
    companion object{
        private val TAG = "ExploreRepo"
    }



//    suspend fun getExploreVideos():ExploreVideosResponse{
//        return exploreService.getExploreVideos()
//    }

//    suspend fun getDetails(id:String): Response<Component> {
//        return exploreService.getDetails(userIdentifierPreferences.id)
//    }


    @ExperimentalPagingApi
    fun getLiveRoomsRegisterUser(): Flow<PagingData<LiveRoom>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                ExplorePagination(exploreService, true, userIdentifierPreferences)
            }
        ).flow
    }

    @ExperimentalPagingApi
    fun getLiveRoomsSignedUser(): Flow<PagingData<LiveRoom>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                ExplorePagination(
                    exploreService,
                    false,
                    userIdentifierPreferences
                )
            }
        ).flow
    }

    suspend fun explorePaging(page:Int): ApiExploreRoom? {
        return try {
            exploreService.explore_room(page).body()

        }  catch (e:Exception){
            e.printStackTrace()
            return null
        }
    }

    suspend fun exploreProfilePaging(page:Int): ApiExploreProfile? {
        return try {
            if(!userIdentifierPreferences.loggedIn)
            exploreService.explore_top_profile(page).body()
            else
                exploreService.explore_top_profile_auth(page,userIdentifierPreferences.id).body()


        }  catch (e:Exception){
            e.printStackTrace()
            return null
        }
    }


    suspend fun exploreUser(deviceId: String): Flow<Resource<Explore>> {

        return flow {
            try {

                Log.d(TAG,"Exploruser -------")
                val response = if (userIdentifierPreferences.loggedIn) {
                    //If user is Signed In (get data for signed in user)
                    exploreService.explore(id = deviceId).toModel()

                }
                else {

                    //If user is not Signed In (get data for not signed in user)
                    exploreService.exploreUnAuth(id = deviceId).toModel()
                }

                Log.d(TAG,"Explore = ${response}")
                emit(Resource.Success(data = response))
            } catch (e: IOException) {
                e.printStackTrace()
                //emit custom error message here according to exception or error that may be displayed to the user or data ( dummy or empty data if required)
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }
            catch (e:Exception){
                Log.d("ExploreRespo","Error in expkore")
                e.printStackTrace()
            }
        }

    }


    suspend fun getsuggestedrooms(page:Int): Flow<Resource<suggestionroomresponse>> {
        Log.d("Hemlo", "get suggested rooms")
        Log.d("Hemlo", "${userIdentifierPreferences.id} ")

        return flow {

            try {

                val response = if (!userIdentifierPreferences.loggedIn) {
                    exploreService.getSuggestedRooms(
                        id = userIdentifierPreferences.uuid,
                        page= page,
                        limit = 5)
                } else {
                    exploreService.getSuggestedRooms(
                        userIdentifierPreferences.id,
                        page= page,
                        limit = 5)
                }

                emit(Resource.Success(data = response))
            } catch (e: IOException) {
                e.printStackTrace()
                //emit custom error message here according to exception or error that may be displayed to the user or data ( dummy or empty data if required)
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }

        }

    }

    suspend fun getNotifications(): ApiNotificationWhenLoggedIn {
//        return exploreService.getNotification(TokenBody(token)).getOrThrow().data.map { it.toModel() }
        return exploreService.getNotification()
    }

    suspend fun getNotificationPlease(): Response<ApiNotification> {
        return exploreService.getNotificationPlease()
    }

    suspend fun getRecordedRooms(): Flow<Resource<ComponentRecorded>>{
        return flow {
            try {

                val response = exploreService.getRecordedRooms()

                Log.d("RecordedRoom","RecordedRoom= $response")
                emit(Resource.Success(data = response))
            }
            catch (e: IOException) {
                e.printStackTrace()
                //emit custom error message here according to exception or error that may be displayed to the user or data ( dummy or empty data if required)
                emit(Resource.Error("Couldn't load  Rdcorded data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load Recorded  data"))
            }
        }
    }

    suspend fun componentSignedUser(): Flow<Resource<Component>> {
        Log.e("UserId", userIdentifierPreferences.id)
        return flow {
            try {
                val response = exploreService.getComponentAuth(userIdentifierPreferences.id)
                emit(Resource.Success(data = response))
            } catch (e: IOException) {
                e.printStackTrace()
                //emit custom error message here according to exception or error that may be displayed to the user or data ( dummy or empty data if required)
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }
        }
    }

    suspend fun componentRegisterUser(): Flow<Resource<Component>> {

        return flow {


            try {
                val response = exploreService.getComponentUnAuth(userIdentifierPreferences.uuid)
                emit(Resource.Success(data = response))
            } catch (e: IOException) {
                e.printStackTrace()
                //emit custom error message here according to exception or error that may be displayed to the user or data ( dummy or empty data if required)
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }

        }
    }

    suspend fun getInfo(): Flow<Resource<Info>> {
        return flow {

            try {
                val response = exploreInfoService.getInfo()
                emit(Resource.Success(data = response))
            } catch (e: IOException) {
                e.printStackTrace()
                //emit custom error message here according to exception or error that may be displayed to the user or data ( dummy or empty data if required)
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }

        }

    }

    suspend fun getRecordedVideos(): Flow<Resource<RecordedVideo>> {

        return flow {

            try {
                val response = exploreRecordService.getRecordedVideos(userIdentifierPreferences.id)
                emit(Resource.Success(data = response))
            } catch (e: IOException) {
                e.printStackTrace()
                //emit custom error message here according to exception or error that may be displayed to the user or data ( dummy or empty data if required)
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }

        }

    }

    suspend fun getRecordedVideosProfile(id: String): Flow<Resource<RecordedVideo>> {

        return flow {


            try {
                val response = exploreRecordService.getRecordedVideos(id)
                emit(Resource.Success(data = response))
            } catch (e: IOException) {
                e.printStackTrace()
                //emit custom error message here according to exception or error that may be displayed to the user or data ( dummy or empty data if required)
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }

        }

    }

    suspend fun getCommonRecordedVideos(): Flow<Resource<RecordedVideo>> {
        return flow {

            try {
                val response = exploreRecordService.getCommonRecordedVideos()
                emit(Resource.Success(data = response))
            } catch (e: IOException) {
                e.printStackTrace()
                //emit custom error message here according to exception or error that may be displayed to the user or data ( dummy or empty data if required)
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }

        }
    }


    suspend fun getTrendingRooms(): Flow<Resource<TrendingRooms>> {

        return flow {
            try {
                val response = exploreService.getTrendingRooms()
                emit(Resource.Success(data = response))
            } catch (e: IOException) {
                e.printStackTrace()
                //emit custom error message here according to exception or error that may be displayed to the user or data ( dummy or empty data if required)
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }

        }
    }

    suspend fun getTrendingRoomsPaged(page: Int): Flow<Resource<TrendingRooms>> {

        return flow {
            try {
                val response = exploreService.getTrendingRoomsPaged(
                    page = page,
                    limit = 5,
                )
                emit(Resource.Success(data = response))
            } catch (e: IOException) {
                e.printStackTrace()
                //emit custom error message here according to exception or error that may be displayed to the user or data ( dummy or empty data if required)
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }

        }
    }



}
