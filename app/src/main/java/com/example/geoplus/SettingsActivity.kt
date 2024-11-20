package com.example.geoplus

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.geoplus.databinding.ActivitySettingsBinding
import com.example.geoplus.global.Database
import com.example.geoplus.models.Configuration
import com.example.geoplus.models.DefaultUser
import com.example.geoplus.models.User

class SettingsActivity : AppCompatActivity() {

    private lateinit var sesion: User
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_settings)

        try {
            sesion = Database.getInstance().session ?: DefaultUser
        } catch(e: Exception) {
            Log.d("LOG_GEOPLUS", e.message?:"Error")
        }

        val textEmail = findViewById<TextView>(R.id.text_email)
        textEmail.text = sesion.email
        val textAge = findViewById<TextView>(R.id.text_age)
        textAge.text = sesion.age
        val textUsername = findViewById<TextView>(R.id.text_username)
        textUsername.text = sesion.nick
        val textName = findViewById<TextView>(R.id.text_name)
        textName.text = sesion.name
        val textLastnamePat = findViewById<TextView>(R.id.text_lastname_pat)
        textLastnamePat.text = sesion.lastname1
        val textLastnameMat = findViewById<TextView>(R.id.text_lastname_mat)
        textLastnameMat.text = sesion.lastname2

        val currentConfiguration: Configuration = Database.getInstance().getConfiguration()

        val switchDarkmode = findViewById<SwitchCompat>(R.id.switch_darkmode)
        switchDarkmode.isChecked = currentConfiguration.darkmode
        switchDarkmode.setOnCheckedChangeListener { _, isChecked ->
            currentConfiguration.darkmode = isChecked
            Database.getInstance().saveConfiguration(currentConfiguration)
        }

        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }
    }
}