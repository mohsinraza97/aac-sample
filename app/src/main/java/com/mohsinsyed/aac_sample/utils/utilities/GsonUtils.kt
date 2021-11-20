package com.mohsinsyed.aac_sample.utils.utilities

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonUtils {

    fun toJson(data: Any?): String? {
        return data?.let { Gson().toJson(it) }
    }

    fun <T> fromJson(data: String?, toCast: Class<T>): T? {
        return data?.let { Gson().fromJson(it, toCast) }
    }

    fun <T> fromJsonAsList(data: String?, toCast: Class<T>): ArrayList<T>? {
        return data?.let {
            val collectionType = TypeToken.getParameterized(ArrayList::class.java, toCast).type
            Gson().fromJson(it, collectionType)
        }
    }
}