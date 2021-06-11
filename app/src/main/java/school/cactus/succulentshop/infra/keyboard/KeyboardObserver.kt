package school.cactus.succulentshop.infra.keyboard

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

class KeyboardObserver {

    fun observe(keyboardFlag: LiveData<Int>, view: View, lifecycleOwner: LifecycleOwner) {
        keyboardFlag.observe(lifecycleOwner, {
            val inputMethodManager =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, it)
        })
    }
}