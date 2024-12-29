package com.example.hw1_daniel_gerbi

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.button.MaterialButton

class SettingsActivity : AppCompatActivity() {

    private lateinit var settings_BTN_switch: SwitchCompat
    private lateinit var settings_BTN_speed: AppCompatSpinner
    private lateinit var settings_BTN_save: MaterialButton
    private lateinit var settings_BTN_cancel: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViews()
        initViews()
    }

    private fun findViews() {
        settings_BTN_switch = findViewById(R.id.settings_BTN_switch)
        settings_BTN_speed = findViewById(R.id.settings_BTN_speed)
        settings_BTN_save = findViewById(R.id.settings_BTN_save)
        settings_BTN_cancel = findViewById(R.id.settings_BTN_cancel)
    }

    private fun initViews() {
        val speeds = arrayOf("Slow", "Normal", "Fast")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, speeds)
        settings_BTN_speed.adapter = adapter

        settings_BTN_save.setOnClickListener {
            saveSettings()
        }
        settings_BTN_cancel.setOnClickListener {
            cancelSettings()
        }
    }

    private fun saveSettings() {
        val isUsingSensors = settings_BTN_switch.isChecked
        val selectedSpeed = settings_BTN_speed.selectedItem.toString()
        val intent = Intent().apply {
            putExtra("IS_USING_SENSORS", isUsingSensors)
            putExtra("SELECTED_SPEED", selectedSpeed)
        }
        setResult(RESULT_OK, intent)
        finish()
    }
    private fun cancelSettings() {
        setResult(RESULT_CANCELED)
        finish()
    }

}
