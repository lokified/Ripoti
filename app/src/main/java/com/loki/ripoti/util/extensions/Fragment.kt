package com.loki.ripoti.util.extensions

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

fun Fragment.setStatusBarColor(color: Int) {

    requireActivity().window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = color
    }
}

fun Fragment.darkStatusBar() {
    requireActivity().window.apply {
        darkStatusBar()
    }
}

fun Fragment.lightStatusBar() {
    requireActivity().window.apply {
        lightStatusBar()
    }

}

fun Fragment.showToast(content: String) {

    Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
}

fun View.showSnackBar(content: String) {
    Snackbar.make(
        this,
        content,
        Snackbar.LENGTH_LONG
    ).show()
}

fun Fragment.navigateBack() {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack()
        }
    })
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}