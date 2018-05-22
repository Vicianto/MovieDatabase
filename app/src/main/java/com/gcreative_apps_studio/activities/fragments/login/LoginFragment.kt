package com.gcreative_apps_studio.activities.fragments.login

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gcreative_apps_studio.activities.R
import com.gcreative_apps_studio.activities.fragments.register.RegisterFragment
import com.gcreative_apps_studio.activities.fragments.remind.RemindFragment
import com.gcreative_apps_studio.activities.main.MainActivity
import com.gcreative_apps_studio.activities.main.MainContract
import com.gcreative_apps_studio.application.AppClass
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login_register.*
import android.content.Intent
import com.gcreative_apps_studio.activities.search.SearchActivity


class LoginFragment : Fragment(), View.OnClickListener, LoginContract.View {
    // Slot enumeration to store the current state of the fragment.
    private enum class LoginSlot {
        LSlot1,
        LSlot2
    }

    // VAR
    lateinit var mainActivity: MainContract.View
    private lateinit var presenter: LoginContract.Presenter


    // FUN
    override fun createPresenter() {
        presenter = LoginPresenter(this, (activity.application as AppClass).boxStore, activity, activity.getString(R.string.app_name))
    }

    override fun initializeUI(view: android.view.View?) {
        // Get views.
        mainActivity = activity as MainActivity
        val login = view?.findViewById<TextView>(R.id.login_button)
        val register = view?.findViewById<TextView>(R.id.register_text)
        val remind = view?.findViewById<TextView>(R.id.remind_text)

        // Load fragment.
        (activity as MainActivity).current = com.gcreative_apps_studio.activities.main.Fragment.Login

        // Register listeners.
        login?.setOnClickListener(this)
        register?.setOnClickListener(this)
        remind?.setOnClickListener(this)

        // Create presenter.
        createPresenter()
    }

    override fun loadFragment(fragment: Fragment) {
        // Create transaction.
        var transaction = fragmentManager.beginTransaction()

        // Set animation.
        transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out, R.animator.fade_in, R.animator.fade_out)

        // Replace.
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)

        // Commit.
        transaction.commit()

        // Reset configuration change mark.
        (mainActivity as MainActivity).isConfigurationChanged = false
    }

    override fun loadSearchActivity(username: String) {
        val intent = Intent(activity, SearchActivity::class.java)
        intent.putExtra(getString(R.string.app_pass_more_key), presenter.getUserID(username))
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_button -> {
                try {
                    if (user_text.text.toString().isEmpty() || password_text.text.toString().isEmpty()) {
                        mainActivity.displayToast(getString(R.string.error_user_fields_empty))
                    } else {
                        if (presenter.loginUser(user_text.text.toString(), password_text.text.toString())) {
                            loadSearchActivity(user_text.text.toString())
                        } else {
                            mainActivity.displayToast(getString(R.string.error_user_login_failed))
                        }
                    }
                } catch (ex: Exception) {
                    mainActivity.displayError(ex)
                }
            }

            R.id.register_text -> {
                loadFragment(RegisterFragment())
            }

            R.id.remind_text -> {
                loadFragment(RemindFragment())
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater?.inflate(R.layout.fragment_login, container, false)

        // UI.
        initializeUI(view)

        return view
    }

    override fun onResume() {
        super.onResume()

        try {
            if ((mainActivity as MainActivity).isConfigurationChanged) {
                // Restore
                restoreState()
            }
        } catch (ex: Exception) {
            mainActivity.displayError(ex)
        }
    }

    override fun onPause() {
        super.onPause()

        try {
            if ((mainActivity as MainActivity).isConfigurationChanged) {
                saveState()
            }

            mainActivity.hideKeyboard()
        } catch (ex: Exception) {
            mainActivity.displayError(ex)
        }
    }

    override fun restoreState() {
        user_text.setText(presenter.loadState(LoginSlot.LSlot1.toString()))
        password_text.setText(presenter.loadState(LoginSlot.LSlot2.toString()))
    }

    override fun saveState() {
        presenter.saveState(LoginSlot.LSlot1.toString(), user_text.text.toString())
        presenter.saveState(LoginSlot.LSlot2.toString(), password_text.text.toString())
    }
}