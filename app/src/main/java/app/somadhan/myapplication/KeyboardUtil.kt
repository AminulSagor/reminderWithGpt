package app.somadhan.myapplication

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window

class KeyboardUtil(activity: Activity, private val contentView: View) {
    private val activityRoot: View = activity.findViewById(Window.ID_ANDROID_CONTENT)
    private var previousHeight: Int = 0

    init {
        activityRoot.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            activityRoot.getWindowVisibleDisplayFrame(r)
            val screenHeight = activityRoot.rootView.height
            val keypadHeight = screenHeight - r.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is visible
                if (previousHeight != keypadHeight) {
                    onKeyboardVisibilityChanged(true)
                }
            } else {
                // Keyboard is hidden
                if (previousHeight != keypadHeight) {
                    onKeyboardVisibilityChanged(false)
                }
            }
            previousHeight = keypadHeight
        }
    }

    var onKeyboardVisibilityChanged: (Boolean) -> Unit = {}
}
