package com.searchai.common.navigation

import com.searchai.common.navigation.NavigationFlow


interface ToFlowNavigable {
    fun navigateToFlow(flow: NavigationFlow)
}