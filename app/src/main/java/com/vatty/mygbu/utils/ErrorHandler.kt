package com.vatty.mygbu.utils

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.vatty.mygbu.R

/**
 * Utility class for handling errors and showing user feedback consistently across the app
 */
class ErrorHandler(private val activity: Activity) {
    companion object {
        private const val TAG = "ErrorHandler"
    }

    private val rootView: ViewGroup = activity.findViewById(android.R.id.content)
    private var loadingView: View? = null
    private var errorView: View? = null

    /**
     * Show a short toast message
     */
    fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Show a snackbar with an action
     */
    fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        action: (() -> Unit)? = null,
        duration: Int = Snackbar.LENGTH_LONG
    ) {
        val rootView = activity.findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, duration)
        if (actionLabel != null && action != null) {
            snackbar.setAction(actionLabel) { action.invoke() }
        }
        snackbar.show()
    }

    /**
     * Show loading state in a view
     */
    fun showLoadingState() {
        hideErrorState()
        if (loadingView == null) {
            loadingView = activity.layoutInflater.inflate(R.layout.layout_loading_state, rootView, false)
        }
        if (loadingView?.parent == null) {
            rootView.addView(loadingView)
        }
    }

    /**
     * Show content and hide loading/error states
     */
    fun showContent() {
        hideLoadingState()
        hideErrorState()
    }

    /**
     * Handle common API errors
     */
    fun handleApiError(
        error: Throwable,
        view: View? = null,
        retryAction: (() -> Unit)? = null
    ) {
        val message = when (error) {
            is java.net.UnknownHostException -> activity.getString(R.string.error_no_internet)
            is java.net.SocketTimeoutException -> activity.getString(R.string.error_timeout)
            is retrofit2.HttpException -> {
                when (error.code()) {
                    401 -> activity.getString(R.string.error_unauthorized)
                    403 -> activity.getString(R.string.error_forbidden)
                    404 -> activity.getString(R.string.error_not_found)
                    500 -> activity.getString(R.string.error_server)
                    else -> activity.getString(R.string.error_unknown)
                }
            }
            else -> activity.getString(R.string.error_unknown)
        }

        if (view != null) {
            showSnackbar(message, activity.getString(R.string.action_retry), retryAction)
        } else {
            showToast(message)
        }
    }

    fun showError(error: Throwable) {
        hideLoadingState()
        if (errorView == null) {
            errorView = activity.layoutInflater.inflate(R.layout.layout_error_state, rootView, false)
        }

        val errorMessage = when (error) {
            is java.net.UnknownHostException,
            is java.net.ConnectException,
            is java.net.SocketTimeoutException -> activity.getString(R.string.error_network)
            is java.lang.SecurityException -> activity.getString(R.string.error_authentication)
            is IllegalArgumentException -> activity.getString(R.string.error_validation)
            else -> error.message ?: activity.getString(R.string.error_unknown)
        }

        errorView?.findViewById<TextView>(R.id.tvErrorMessage)?.text = errorMessage

        if (errorView?.parent == null) {
            rootView.addView(errorView)
        }
    }

    fun hideLoadingState() {
        loadingView?.let {
            rootView.removeView(it)
        }
    }

    fun hideErrorState() {
        errorView?.let {
            rootView.removeView(it)
        }
    }
}

sealed class AppError : Throwable()
class NetworkError : AppError()
class AuthenticationError : AppError()
class ValidationError(override val message: String?) : AppError() 