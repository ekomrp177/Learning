package com.example.submission5.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.submission5.R
import com.example.submission5.notifications.DailyReminder
import com.example.submission5.sharedpreferences.SharedPreference
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    companion object {
        private const val REFSAVE_DAILY = "switch_daily"
    }
    private lateinit var dailyReminder : DailyReminder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        var sharedPreference: SharedPreference = SharedPreference(this)
        switchDaily.setChecked(sharedPreference.getValueBool(REFSAVE_DAILY, false))
        dailyReminder = DailyReminder()

        supportActionBar?.setTitle("Settings")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        switchDaily.setOnCheckedChangeListener { _, b ->
            if (b) {
                Toast.makeText(this, "On", Toast.LENGTH_SHORT).show()
                sharedPreference.setValueBool(REFSAVE_DAILY, b)
                dailyReminder.setReminderDaily(this)
            } else {
                Toast.makeText(this, "Off", Toast.LENGTH_SHORT).show()
                sharedPreference.setValueBool(REFSAVE_DAILY, b)
                dailyReminder.cancelReminderDaily(this)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if(supportFragmentManager.getBackStackEntryCount() == 0) {
            super.onBackPressed()
        }
        else supportFragmentManager.popBackStack()
    }
}
