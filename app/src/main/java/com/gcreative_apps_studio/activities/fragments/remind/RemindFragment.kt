package com.gcreative_apps_studio.activities.fragments.remind

import android.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.gcreative_apps_studio.activities.R
import com.gcreative_apps_studio.activities.main.MainActivity
import com.gcreative_apps_studio.activities.main.MainContract
import com.gcreative_apps_studio.application.AppClass
import kotlinx.android.synthetic.main.fragment_login_remind.*

class RemindFragment : Fragment(), View.OnClickListener, TextWatcher, RemindContract.View {
    // Slot enumeration to store the current state of the fragment.
    private enum class RemindSlot {
        ReSlot1,
        ReSlot2,
        ReSlot3
    }

    // VAR
    lateinit var mainActivity: MainContract.View
    private lateinit var presenter: RemindContract.Presenter

    // FUNC
    override fun afterTextChanged(s: Editable?) {
        if (remind_username_text.text.toString().isNotEmpty()) {
            try {
                remind_sec_text.setText(presenter.getSecurity(remind_username_text.text.toString()))
            } catch (ex: Exception) {
                mainActivity.displayError(ex)
            }

            if (remind_sec_text.text.toString().isNotEmpty()) {
                remind_sec_status.setImageResource(R.drawable.ic_action_correct)
            } else {
                remind_sec_status.setImageResource(R.drawable.ic_action_wrong)
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun createPresenter() {
        presenter = RemindPresenter(this, (activity.application as AppClass).boxStore, activity, activity.getString(R.string.app_name))
    }

    override fun initializeUI(view: View?) {
        // Get views.
        mainActivity = activity as MainActivity
        val user = view?.findViewById<EditText>(R.id.remind_username_text)
        val remind = view?.findViewById<TextView>(R.id.remind_button)

        // Load fragment.
        (mainActivity as MainActivity).current = com.gcreative_apps_studio.activities.main.Fragment.Remind

        // Register listeners.
        user?.addTextChangedListener(this)
        remind?.setOnClickListener(this)

        // Create presenter.
        createPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_login_remind, container, false)

        // Init.
        initializeUI(view)

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.remind_button -> {
                if (remind_username_text.text.toString().isEmpty() ||
                        remind_sec_text.text.isEmpty() ||
                        remind_sec_q_text.text.isEmpty()) {
                    mainActivity.displayToast(getString(R.string.error_user_fields_empty))
                } else {
                    try {
                        if (presenter.resetPass(remind_username_text.text.toString(), remind_sec_text.text.toString(),
                                        remind_sec_q_text.text.toString(), getString(R.string.app_reset_pass))) {
                            mainActivity.displayDialog(getString(R.string.dialog_remind_title), String.format(getString(R.string.dialog_remind_success), getString(R.string.app_reset_pass)))
                        } else {
                            mainActivity.displayDialog(getString(R.string.dialog_remind_title), getString(R.string.dialog_remind_failure))
                        }
                    } catch (ex: Exception) {
                        mainActivity.displayError(ex)
                    }
                }
            }
        }
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

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun restoreState() {
        remind_username_text.setText(presenter.loadState(RemindSlot.ReSlot1.toString()))
        remind_sec_text.setText(presenter.loadState(RemindSlot.ReSlot2.toString()))
        remind_sec_q_text.setText(presenter.loadState(RemindSlot.ReSlot3.toString()))
    }

    override fun saveState() {
        presenter.saveState(RemindSlot.ReSlot1.toString(), remind_username_text.text.toString())
        presenter.saveState(RemindSlot.ReSlot2.toString(), remind_sec_text.text.toString())
        presenter.saveState(RemindSlot.ReSlot3.toString(), remind_sec_q_text.text.toString())
    }
}