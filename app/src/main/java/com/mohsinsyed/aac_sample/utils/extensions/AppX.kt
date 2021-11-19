package com.mohsinsyed.aac_sample.utils.extensions

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
    message: String?,
    length: Int = Snackbar.LENGTH_LONG,
) {
    if (message?.isNotEmpty() == true) {
        Snackbar.make(this, message, length).show()
    }
}

fun Fragment.setToolBarTitle(title: String, showBackIcon: Boolean = false) {
    val actionBar = (activity as? AppCompatActivity)?.supportActionBar
    actionBar?.title = title
    actionBar?.setDisplayHomeAsUpEnabled(showBackIcon)
}

fun Activity.hideKeyboard() {
    currentFocus?.hideKeyboard()
}

fun View.hideKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun RecyclerView.addDivider() {
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
}

fun EditText.onTextChanged(onTextChanged: (String?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(s?.toString())
        }

        override fun afterTextChanged(editable: Editable?) {
        }
    })
}

@ColorInt
fun Context.themeColor(@AttrRes attrRes: Int): Int {
    return TypedValue()
        .apply { theme.resolveAttribute(attrRes, this, true) }
        .data
}