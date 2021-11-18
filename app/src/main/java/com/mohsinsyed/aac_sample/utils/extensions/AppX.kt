package com.mohsinsyed.aac_sample.utils.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
    message: String,
    length: Int = Snackbar.LENGTH_LONG
) {
    if (message.isNotEmpty()) {
        Snackbar.make(this, message, length).show()
    }
}

fun Fragment.setToolBarTitle(title: String, showBackIcon: Boolean = false) {
    val actionBar = (activity as? AppCompatActivity)?.supportActionBar
    actionBar?.title = title
    actionBar?.setDisplayHomeAsUpEnabled(showBackIcon)
}

fun Fragment.hideKeyboard() {
    requireActivity().currentFocus?.hideKeyboard()
}

fun View.hideKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun RecyclerView.addDivider() {
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
}