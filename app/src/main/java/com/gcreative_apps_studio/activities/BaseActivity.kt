package com.gcreative_apps_studio.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.toast.*
import java.util.*

abstract class BaseActivity : Activity() {
    private var aToast: Toast? = null
    private var isDialogActive: Boolean = false

    fun displayToast(message: String) {
        aToast?.cancel()

        val layout = layoutInflater.inflate(R.layout.toast, toast_container)

        layout.findViewById<TextView>(R.id.toast_text).text = message

        aToast = Toast(this)
        aToast?.duration = Toast.LENGTH_SHORT
        aToast?.view = layout
        aToast?.setGravity(Gravity.BOTTOM, 0, resources.getDimension(R.dimen.toast_offset).toInt())

        aToast?.show()
    }

    fun displayDialog(title: String, message: String) {
        if (!isDialogActive) {
            isDialogActive = true

            val builder = AlertDialog.Builder(this, R.style.AppTheme_AlertDialog)

            builder.setTitle(title)
            builder.setMessage(message)

            builder.setNegativeButton(R.string.dialog_general_ok) { dialogInterface, _ ->
                dialogInterface.cancel()
                isDialogActive = !isDialogActive
            }

            builder.setIcon(R.drawable.ic_dialog_general)

            builder.show()
        }
    }

    fun displayError(ex: Exception) {
        if (!isDialogActive) {

            isDialogActive = true

            val builder = AlertDialog.Builder(this, R.style.AppTheme_AlertDialog)

            builder.setTitle(R.string.dialog_error_title)
            builder.setMessage(R.string.dialog_error_text)

            builder.setPositiveButton(R.string.dialog_error_ok) { _, _ ->
                sendReport(ex)
                isDialogActive = !isDialogActive
            }

            builder.setNegativeButton(R.string.dialog_error_cancel) { dialogInterface, _ ->
                dialogInterface.cancel()
                isDialogActive = !isDialogActive
            }

            builder.setIcon(R.drawable.ic_dialog_mark)

            builder.show()
        }
    }


    private fun sendReport(ex: Exception) {
        val i = Intent(Intent.ACTION_SEND)

        i.type = getString(R.string.app_send_report_type)
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.app_send_report_email)))
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_send_report_subject))
        i.putExtra(Intent.EXTRA_TEXT, ex.toString() + Arrays.toString(ex.stackTrace))

        val apps = packageManager.queryIntentActivities(i, 0)

        if (apps.size != 0) {
            startActivity(Intent.createChooser(i, getString(R.string.dialog_error_send_report_title)))
        } else {
            displayToast(getString(R.string.error_no_app))
        }
    }
}