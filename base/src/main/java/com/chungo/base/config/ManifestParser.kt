package com.chungo.base.config

import android.content.Context
import android.content.pm.PackageManager
import java.util.*

/**
 * 用于解析 AndroidManifest 中的 Meta 属性
 * 配合 [ConfigModule] 使用
 *
 *
 */
class ManifestParser(private val context: Context) {

    fun parse(): ArrayList<ConfigModule> {
        val modules = ArrayList<ConfigModule>()
        try {
            val appInfo = context.packageManager.getApplicationInfo(
                    context.packageName, PackageManager.GET_META_DATA)
            if (appInfo.metaData != null) {
                for (key in appInfo.metaData.keySet()) {
                    if (MODULE_VALUE == appInfo.metaData.get(key)) {
                        modules.add(parseModule(key))
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Unable to find metadata to parse ConfigModule", e)
        }

        return modules
    }

    companion object {
        private val MODULE_VALUE = "ConfigModule"

        private fun parseModule(className: String): ConfigModule {
            val clazz: Class<*>
            try {
                clazz = Class.forName(className)
            } catch (e: ClassNotFoundException) {
                throw IllegalArgumentException("Unable to find ConfigModule implementation", e)
            }

            val module: Any
            try {
                module = clazz.newInstance()
            } catch (e: InstantiationException) {
                throw RuntimeException("Unable to instantiate ConfigModule implementation for $clazz", e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("Unable to instantiate ConfigModule implementation for $clazz", e)
            }

            if (module !is ConfigModule) {
                throw RuntimeException("Expected instanceof ConfigModule, but found: $module")
            }
            return module
        }
    }
}