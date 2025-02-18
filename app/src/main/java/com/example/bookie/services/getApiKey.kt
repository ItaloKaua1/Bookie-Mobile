package com.example.bookie.services

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

fun getApiKey(context: Context): String {
    val ai: ApplicationInfo = context.packageManager
        .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
    val bundle = ai.metaData
    return bundle.getString("GOOGLE_BOOKS_API_KEY") ?: throw IllegalArgumentException("API key not found")
}