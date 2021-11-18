package com.mohsinsyed.aac_sample.utils.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

fun Fragment.navigateTo(directions: NavDirections) {
    findNavController().navigate(directions)
}

fun Fragment.navigateTo(destinationResId: Int) {
    findNavController().navigate(destinationResId)
}

fun Fragment.navigateTo(destinationResId: Int, args: Bundle) {
    findNavController().navigate(destinationResId, args)
}