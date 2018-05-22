package com.gcreative_apps_studio.activities.fragments.register

import android.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.gcreative_apps_studio.activities.R
import com.gcreative_apps_studio.activities.main.MainActivity
import com.gcreative_apps_studio.activities.main.MainContract
import com.gcreative_apps_studio.application.AppClass
import com.gcreative_apps_studio.custom.CustomEditText
import kotlinx.android.synthetic.main.fragment_login_register.*


class RegisterFragment : Fragment(), View.OnClickListener, TextWatcher, TextView.OnEditorActionListener, RegisterContract.View {
    // Slot enumeration to store the current state of the fragment.
    private enum class RegisterSlot {
        RSlot1,
        RSlot2,
        RSlot3,
        RSlot4,
        RSlot5,
        RSlot6
    }

    // VAR
    lateinit var mainActivity: MainContract.View
    private lateinit var presenter: RegisterContract.Presenter
    private var passMatch: Boolean = false

    // FUNC
    override fun afterTextChanged(s: Editable?) {
        try {
            controlUserField()
            controlPassAgainField()
            controlEmailField()
        } catch (ex: Exception) {
            mainActivity.displayError(ex)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun controlUserField() {
        if (register_username_text.text.toString().isNotEmpty()) {
            if (presenter.checkUsername(register_username_text.text.toString())) {
                register_username_status.setImageResource(R.drawable.ic_action_correct)
            } else {
                register_username_status.setImageResource(R.drawable.ic_action_wrong)
            }
        } else {
            register_username_status.setImageResource(R.drawable.ic_action_wrong)
        }
    }

    override fun controlPassAgainField() {
        passMatch = if (register_password_text.text.toString().isNotEmpty()) {
            if (register_password_text.text.toString() == register_password_again_text.text.toString()) {
                register_password_again_status.setImageResource(R.drawable.ic_action_correct)
                true
            } else {
                register_password_again_status.setImageResource(R.drawable.ic_action_wrong)
                false
            }
        } else {
            register_password_again_status.setImageResource(R.drawable.ic_action_wrong)
            false
        }
    }

    override fun controlEmailField() {
        if (register_email_text.text.toString().isNotEmpty()) {
            if (presenter.checkEmail(register_email_text.text.toString())) {
                register_email_status.setImageResource(R.drawable.ic_action_correct)
            } else {
                register_email_status.setImageResource(R.drawable.ic_action_wrong)
            }
        } else {
            register_email_status.setImageResource(R.drawable.ic_action_wrong)
        }
    }

    override fun createPresenter() {
        presenter = RegisterPresenter(this, (activity.application as AppClass).boxStore, activity, activity.getString(R.string.app_name))
    }

    override fun initializeUI(view: android.view.View?) {
        // Get views.
        mainActivity = activity as MainActivity
        val register = view?.findViewById<TextView>(R.id.register_button)
        val userField = view?.findViewById<CustomEditText>(R.id.register_username_text)
        val pField = view?.findViewById<CustomEditText>(R.id.register_password_text)
        val paField = view?.findViewById<CustomEditText>(R.id.register_password_again_text)
        val emailField = view?.findViewById<CustomEditText>(R.id.register_email_text)
        val secField = view?.findViewById<CustomEditText>(R.id.register_sec_text)
        val sqField = view?.findViewById<CustomEditText>(R.id.register_sec_q_text)

        // Load fragment.
        (mainActivity as MainActivity).current = com.gcreative_apps_studio.activities.main.Fragment.Register

        // Set properties.
        userField?.activity = this
        pField?.activity = this
        paField?.activity = this
        emailField?.activity = this
        secField?.activity = this
        sqField?.activity = this

        // Register listeners.
        register?.setOnClickListener(this)
        userField?.addTextChangedListener(this)
        pField?.addTextChangedListener(this)
        paField?.addTextChangedListener(this)
        emailField?.addTextChangedListener(this)
        sqField?.setOnEditorActionListener(this)

        // Create presenter.
        createPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_login_register, container, false)

        // Init.
        initializeUI(view)

        return view
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        when (actionId) {
            EditorInfo.IME_ACTION_DONE -> {
                mainActivity.hideKeyboard()
                showLogo()
                register_button.performClick()
                return true
            }
        }

        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.register_button -> {
                if (register_username_text.text.toString().isEmpty() ||
                        register_password_text.text.toString().isEmpty() ||
                        register_email_text.text.toString().isEmpty() ||
                        register_sec_text.text.toString().isEmpty() ||
                        register_sec_q_text.text.toString().isEmpty()) {
                    mainActivity.displayToast(getString(R.string.error_user_fields_empty))

                } else {
                    if (!passMatch) {
                        mainActivity.displayToast(getString(R.string.error_user_pass_match))
                        mainActivity.showKeyboard()
                        showLogo()
                        return
                    }

                    try {
                        if (presenter.registerUser(presenter.createUser(
                                        register_username_text.text.toString(),
                                        register_password_text.text.toString(),
                                        register_email_text.text.toString(),
                                        register_sec_text.text.toString(),
                                        register_sec_q_text.text.toString()))) {
                            mainActivity.displayToast(getString(R.string.error_user_already_exists))
                        } else {
                            mainActivity.displayToast(getString(R.string.general_user_created))
                            mainActivity.hideKeyboard()

                            // Close this fragment.
                            activity.fragmentManager.popBackStack()
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

    override fun hideLogo() {
        register_logo?.visibility = GONE
        if (register_logo != null) {
            register_body_layout.gravity = Gravity.NO_GRAVITY
        }
    }

    override fun restoreState() {
        register_username_text.setText(presenter.loadState(RegisterSlot.RSlot1.toString()))
        register_password_text.setText(presenter.loadState(RegisterSlot.RSlot2.toString()))
        register_password_again_text.setText(presenter.loadState(RegisterSlot.RSlot3.toString()))
        register_email_text.setText(presenter.loadState(RegisterSlot.RSlot4.toString()))
        register_sec_text.setText(presenter.loadState(RegisterSlot.RSlot5.toString()))
        register_sec_q_text.setText(presenter.loadState(RegisterSlot.RSlot6.toString()))
    }

    override fun showLogo() {
        register_logo?.visibility = VISIBLE

        if (register_logo != null) {
            register_body_layout.gravity = Gravity.CENTER
        }

        (mainActivity as MainActivity).keyboardState = GONE
    }

    override fun saveState() {
        presenter.saveState(RegisterSlot.RSlot1.toString(), register_username_text.text.toString())
        presenter.saveState(RegisterSlot.RSlot2.toString(), register_password_text.text.toString())
        presenter.saveState(RegisterSlot.RSlot3.toString(), register_password_again_text.text.toString())
        presenter.saveState(RegisterSlot.RSlot4.toString(), register_email_text.text.toString())
        presenter.saveState(RegisterSlot.RSlot5.toString(), register_sec_text.text.toString())
        presenter.saveState(RegisterSlot.RSlot6.toString(), register_sec_q_text.text.toString())
    }
}