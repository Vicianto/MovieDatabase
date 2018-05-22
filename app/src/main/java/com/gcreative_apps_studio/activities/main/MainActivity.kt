package com.gcreative_apps_studio.activities.main

import android.app.Activity
import android.app.AlertDialog
import android.app.FragmentTransaction
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.gcreative_apps_studio.activities.BaseActivity
import com.gcreative_apps_studio.activities.R
import com.gcreative_apps_studio.activities.fragments.login.LoginFragment
import com.gcreative_apps_studio.activities.fragments.register.RegisterFragment
import com.gcreative_apps_studio.activities.fragments.remind.RemindFragment
import kotlinx.android.synthetic.main.toast.*
import java.util.*


// Enum used for restore the backstack
enum class Fragment {
    Login,
    Register,
    Remind
}

class MainActivity : BaseActivity(), MainContract.View {
    // VAR
    var keyboardState: Int = View.GONE
    var isConfigurationChanged: Boolean = false
    var current: Fragment = Fragment.Login
    private val handler: Handler = Handler()


    // FUNC
    override fun createPresenter() {

    }

    override fun displayLogin() {
        fragmentManager.beginTransaction().add(R.id.fragment_container, LoginFragment()).commit()
    }

    override fun hideKeyboard() {
        val view: View? = currentFocus

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)

        keyboardState = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Display login.
        try {
            displayLogin()
        } catch (ex: Exception) {
            displayError(ex)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        // Sign configuration change event.
        isConfigurationChanged = true

        try {
            // Reload the actual fragment.
            when (current) {
                Fragment.Login -> {
                    reloadFragment(LoginFragment())
                }

                Fragment.Register -> {
                    reloadFragment(RegisterFragment())
                }

                Fragment.Remind -> {
                    reloadFragment(RemindFragment())
                }
            }
        } catch (ex: Exception) {
            displayError(ex)
        }
    }

    override fun reloadFragment(fragment: android.app.Fragment) {
        fragmentManager.popBackStack()
        val transaction = fragmentManager.beginTransaction()

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun restoreState() {
    }

    override fun saveState() {
    }

    override fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (keyboardState != View.VISIBLE) {
            keyboardState = View.VISIBLE
            handler.postDelayed({ imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0) }, resources.getInteger(R.integer.general_soft_keyboard_delay).toLong())
        }
    }
}
