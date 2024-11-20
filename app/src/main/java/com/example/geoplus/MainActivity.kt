package com.example.geoplus

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.geoplus.databinding.ActivityMainBinding
import com.example.geoplus.global.Database
import com.example.geoplus.models.DefaultUser
import com.example.geoplus.models.User
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val context = this

        Database.getInstance().tryInit(context)

        try {
            Database.getInstance().isActiveSession()
            val sesion = Database.getInstance().session ?: DefaultUser
            Log.d("LOG_GEOPLUS", "Recuperando sesion ${sesion.nick}")

            val configuration = Database.getInstance().getConfiguration()
            if(configuration.darkmode) {
                Log.d("LOG_GEOPLUS", "Modo oscuro activado")
                setTheme(R.style.GeoPlus_Theme_Dark)
            } else {
                Log.d("LOG_GEOPLUS", "Modo claro activado")
                setTheme(R.style.GeoPlus_Theme)
            }
            recreate()
        } catch(e: Exception) {
            Log.d("LOG_GEOPLUS", e.message?:"Error")
        }

        timer = object : CountDownTimer(5_000, 1_000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                try {
                    if(Database.getInstance().isActiveSession()) {
                        val intent = Intent(context, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)
                    }
                } catch(e: Exception) {
                    Log.d("LOG_GEOPLUS", e.message?:"")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        timer.start()
    }
}