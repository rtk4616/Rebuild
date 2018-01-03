package rejasupotaro.rebuild.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import rejasupotaro.rebuild.R
import rejasupotaro.rebuild.listener.NotificationEpisodesCheckBoxChangeListener
import rejasupotaro.rebuild.utils.PreferenceUtils

class SettingsActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
        setupActionBar()
    }

    private fun setupActionBar() {
        supportActionBar?.also {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)
            setUpNotificationEpisodeCheckBox()
        }

        private fun setUpNotificationEpisodeCheckBox() {
            val key = getString(R.string.pref_key_notification_episodes)
            val checkBox = findPreference(key) as CheckBoxPreference
            val checkStatus = PreferenceUtils.getBoolean(activity, key)
            checkBox.isChecked = checkStatus
            checkBox.onPreferenceChangeListener = NotificationEpisodesCheckBoxChangeListener(activity)
        }
    }
}