package com.searchai.common.kotlinExtentions.base.extensions

import androidx.fragment.app.Fragment
import com.searchai.auth.constants.ProfileConstants
import com.searchai.common.navigation.NavigationFlow

fun Fragment.navigateToProfile(id: String, idFromBaseViewModel: String) {
    if (id == idFromBaseViewModel) {
        (requireActivity() as ToFlowNavigable).navigateToFlow(
            NavigationFlow.ProfileFlow
        )
        return
    }
    ProfileConstants.creatorModel = SearchCreatorApiResponse(id = id)

    (requireActivity() as ToFlowNavigable).navigateToFlow(
        NavigationFlow.GlobalProfileFlow
    )
}
