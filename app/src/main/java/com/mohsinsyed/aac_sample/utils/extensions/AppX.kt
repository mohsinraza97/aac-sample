package com.mohsinsyed.aac_sample.utils.extensions

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.mohsinsyed.aac_sample.R
import com.mohsinsyed.aac_sample.utils.utilities.LogUtils

fun View.showSnackBar(
    message: String?,
    length: Int = Snackbar.LENGTH_LONG,
) {
    if (message?.isNotEmpty() == true) {
        Snackbar.make(this, message, length).show()
    }
}

fun ImageView.loadImage(resId: Int?, placeholderResId: Int = R.color.placeholder) {
    Glide.with(context)
        .load(resId)
        .placeholder(placeholderResId)
        .into(this)
}

fun ImageView.loadImage(
    path: String?,
    placeholderResId: Int? = R.color.placeholder,
    callback: ((Boolean) -> Unit)? = null,
) {
    val glide = Glide.with(context)
        .load(path)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean,
            ): Boolean {
                LogUtils.debugLog(e?.message ?: e.toString(), path)
                callback?.invoke(false)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean,
            ): Boolean {
                callback?.invoke(true)
                return false
            }

        })

    if (placeholderResId != null) {
        glide.placeholder(placeholderResId)
    }
    glide.diskCacheStrategy(DiskCacheStrategy.ALL).into(this)
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

fun TextInputLayout.toggleError(message: String? = null) {
    this.apply {
        isErrorEnabled = message != null
        error = message
    }
}

fun View.hideKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun Fragment.setToolBarTitle(title: String, showBackIcon: Boolean = false) {
    val actionBar = (activity as? AppCompatActivity)?.supportActionBar
    actionBar?.title = title
    actionBar?.setDisplayHomeAsUpEnabled(showBackIcon)
}

fun Activity.hideKeyboard() {
    currentFocus?.hideKeyboard()
}

@ColorInt
fun Context.getThemeColor(@AttrRes attrRes: Int): Int {
    return TypedValue()
        .apply { theme.resolveAttribute(attrRes, this, true) }
        .data
}