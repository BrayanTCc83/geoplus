package com.example.geoplus

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import com.example.geoplus.databinding.ActivityIdeaBinding
import com.example.geoplus.fragments.RenderizeIdea
import com.example.geoplus.models.Idea
import com.example.geoplus.utils.ReadJSONFromAssets
import com.google.gson.Gson
import kotlin.random.Random

class IdeaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIdeaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIdeaBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_idea)

        val indexFile = Random.nextInt(0, 32)
        val jsonString = ReadJSONFromAssets(baseContext, "idea/idea_$indexFile.json")
        val content: Idea = Gson().fromJson(jsonString, Idea::class.java)

        val textTitle = findViewById<TextView>(R.id.text_title)
        textTitle.text = content.country

        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        val composeView: ComposeView = findViewById<ComposeView>(R.id.description_view)
        composeView.setContent {
            RenderizeIdea(content.places, baseContext)
        }
    }
}