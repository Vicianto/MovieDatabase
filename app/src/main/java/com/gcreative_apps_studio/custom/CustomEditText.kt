package com.gcreative_apps_studio.custom


import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.EditText
import com.gcreative_apps_studio.activities.fragments.register.RegisterContract
import com.gcreative_apps_studio.activities.fragments.register.RegisterFragment

class CustomEditText : EditText {
    var activity: RegisterContract.View? = null

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?) : super(context)

    init {
        showSoftInputOnFocus = false
    }


    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            activity?.showLogo()
        }

        return super.onKeyPreIme(keyCode, event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            activity?.hideLogo()
            (activity as RegisterFragment?)?.mainActivity?.showKeyboard()
        }

        return super.onTouchEvent(event)
    }
}