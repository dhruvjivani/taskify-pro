package com.conestoga.taskifypro

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {

    private lateinit var switchTheme: Switch
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = getSharedPreferences("settings", MODE_PRIVATE)
        val isDark = preferences.getBoolean("dark_theme", false)
        setThemeMode(isDark)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        switchTheme = findViewById(R.id.switch_theme)
        switchTheme.isChecked = isDark

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean("dark_theme", isChecked).apply()
            setThemeMode(isChecked)
        }
    }

    private fun setThemeMode(darkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
