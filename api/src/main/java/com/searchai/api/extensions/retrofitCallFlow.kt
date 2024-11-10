package com.searchai.api.extensions

import android.util.Log
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.searchai.api.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

inline fun <T> retrofitCallFlow(
    crossinline body: suspend () -> Response<T>,
    tag: String = "RetrofitCall"
): Flow<Resource<T>> = flow {
    emit(Resource.Loading)
    try {
        val response = body().getOrThrow()
        Log.d(tag, "Response: $response")
        emit(Resource.Success(response))
    } catch (e: HttpException) {
        Log.e(tag, "HttpException: ${e.message()}", e)
        emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
    } catch (e: IOException) {
        Log.e(tag, "IOException: ${e.message}", e)
        emit(Resource.Error("Couldn't reach server. Check your internet connection."))
    } catch (e: Exception) {
        Log.e(tag, "Exception: ${e.message}", e)
        emit(Resource.Error(e.localizedMessage ?: "An unknown error occurred"))
    } catch (_: GetCredentialCancellationException){
        Log.e(tag, "GetCredentialCancellationException")
        emit(Resource.Idle)
    }catch (e: NoCredentialException){
        Log.e(tag, "NoCredentialException")
        emit(Resource.Idle)
    }
}