package com.example.ui.nav

import android.net.Uri

internal sealed class Screen(val route: String) {
    data object DashboardScreen : Screen(DASHBOARD_ROUTE)

    data object CarListScreen : Screen(CAR_LIST_ROUTE)

    data object CarSelectionScreen : Screen(CAR_SELECTION_ROUTE)
}


internal fun createRouteWithPathArguments(route: String, vararg arguments: String): String {
    val builder = Uri.parse(route).buildUpon()
    arguments.forEach {
        builder.appendEncodedPath("{$it}")
    }
    return builder.build().toString()
}
