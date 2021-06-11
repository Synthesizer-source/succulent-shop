package school.cactus.succulentshop.infra.navigation

import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController

class NavigationObserver {
    fun observe(
        navigation: Navigation,
        navController: NavController,
        lifecycleOwner: LifecycleOwner
    ) {
        navigation.navigateTo.observe(lifecycleOwner) { directions ->
            try {
                directions?.let {
                    navController.navigate(it)
                    navigation.onNavigationComplete()
                }
            } catch (exception: Exception) {
                navigation.onNavigationComplete()
            }
        }
    }
}