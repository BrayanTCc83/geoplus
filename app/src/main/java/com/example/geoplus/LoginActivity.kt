package com.example.geoplus

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.example.geoplus.databinding.ActivityLoginBinding
import com.example.geoplus.global.Database
import com.example.geoplus.models.User

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttonLogin = findViewById<Button>(R.id.btn_login);
        buttonLogin.setOnClickListener {
            val user = User(
                nick = binding.inputUsername.text.toString(),
                password = binding.inputAnswer.text.toString(),
                email = "",
                lastname1 = "",
                lastname2 = "",
                name = "",
                age = "0"
            )
            if(Database.getInstance().loginUser(this, user)) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }

        val buttonRegister = findViewById<Button>(R.id.btn_register);
        buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val buttonExit = findViewById<Button>(R.id.btn_exit);
        buttonExit.setOnClickListener {
            this.finish();
            System.exit(0);
        }
    }
}