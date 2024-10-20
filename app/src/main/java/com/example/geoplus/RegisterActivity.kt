package com.example.geoplus

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.example.geoplus.databinding.ActivityRegisterBinding
import com.example.geoplus.global.Database
import com.example.geoplus.models.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttonRegister = findViewById<Button>(R.id.btn_register);
        buttonRegister.setOnClickListener {
            try {
                val user = User(
                    nick = binding.inputUsername.text.toString(),
                    name = binding.inputName.text.toString(),
                    lastname1 = binding.inputLastnamePat.text.toString(),
                    lastname2 = binding.inputLastnameMat.text.toString(),
                    age =  binding.inputAge.text.toString(),
                    email = binding.inputEmail.text.toString(),
                    password = binding.inputPassword.text.toString()
                )
                if(!Database.getInstance().registerUser(this, user))
                    throw Exception("No se pudo registrar el usuario")

                if(!Database.getInstance().loginUser(this, user))
                    throw Exception("No se pudo registrar el usuario")

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.d("LOG_GEOPLUS", e.message?:"")
                Toast.makeText(this, "Los datos proveidos para el usuario son invalidos, verifique las entradas.", Toast.LENGTH_LONG).show()
            }
        }

        val buttonLogin = findViewById<Button>(R.id.btn_login);
        buttonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val buttonExit = findViewById<Button>(R.id.btn_exit);
        buttonLogin.setOnClickListener {
            this.finish();
            System.exit(0);
        }
    }
}