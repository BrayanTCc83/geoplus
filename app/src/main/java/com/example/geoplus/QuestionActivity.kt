package com.example.geoplus

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.platform.ComposeView
import com.example.geoplus.databinding.ActivityQuestionBinding
import com.example.geoplus.fragments.FindQuestionComponent
import com.example.geoplus.fragments.PaintQuestionComponent
import com.example.geoplus.fragments.QuizQuestionComponent
import com.example.geoplus.fragments.RouteQuestionComponent
import com.example.geoplus.global.GlobalState
import com.example.geoplus.models.ComplexRutaQuestion
import com.example.geoplus.models.FindByDescriptionQuestion
import com.example.geoplus.models.FindByImageQuestion
import com.example.geoplus.models.FindQuestion
import com.example.geoplus.models.FindYourselfModel
import com.example.geoplus.models.GameModel
import com.example.geoplus.models.PaintContryQuestion
import com.example.geoplus.models.PaintObjectQuestion
import com.example.geoplus.models.PaintOnceQuestion
import com.example.geoplus.models.PintaEstadosModel
import com.example.geoplus.models.QuizCheckboxQuestionModel
import com.example.geoplus.models.QuizModel
import com.example.geoplus.models.QuizOpenQuestionModel
import com.example.geoplus.models.QuizQuestionModel
import com.example.geoplus.models.QuizRadioQuestionModel
import com.example.geoplus.models.RutaQuestion
import com.example.geoplus.models.RutasModel
import com.example.geoplus.models.SimpleRutaQuestion
import com.example.geoplus.utils.ReadJSONFromAssets
import com.example.geoplus.utils.RuntimeTypeAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.Exception

class QuestionActivity : AppCompatActivity() {
    final val TYPE_UBICATE = "Ubícate"
    final val TYPE_QUIZ = "Quiz"
    final val TYPE_PINTA_ESTADOS = "Pinta estados"
    final val TYPE_RUTAS = "Rutas"

    final val FIELD_TYPE = "type"

    final val TYPE_CHECKBOX = "checkbox"
    final val TYPE_RADIO = "radio"
    final val TYPE_OPEN = "open"

    final val TYPE_SIMPLE = "simple"
    final val TYPE_COMPLEX = "complex"

    final val TYPE_ONCE = "paint_once"
    final val TYPE_OBJ = "paint_obj"

    final val TYPE_IMAGE = "find_image"
    final val TYPE_TEXT = "find_text"

    private lateinit var binding: ActivityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var fileName = intent.getStringExtra("file")
        var type = intent.getStringExtra("type")
        var questionId = intent.getIntExtra("question_id", 0)
        val globalState: GlobalState = GlobalState.getInstance()

        if (fileName == null)
            fileName = globalState.fileName
        else
            globalState.fileName = fileName

        if (type == null)
            type = globalState.type
        else
            globalState.type = type

        if (questionId == 0)
            questionId = globalState.questionId
        else
            globalState.questionId = questionId

        globalState.ctx = this

        try {
            var game: GameModel? = null
            if(globalState.questionary != null)
                game = globalState.questionary
            else {
                val jsonString = ReadJSONFromAssets(baseContext, fileName?:"")
                val adapterQuiz = RuntimeTypeAdapterFactory
                    .of(QuizQuestionModel::class.java, FIELD_TYPE)
                    .registerSubtype(QuizOpenQuestionModel::class.java, TYPE_OPEN)
                    .registerSubtype(QuizCheckboxQuestionModel::class.java, TYPE_CHECKBOX)
                    .registerSubtype(QuizRadioQuestionModel::class.java, TYPE_RADIO)
                val adapterRoutes = RuntimeTypeAdapterFactory
                    .of(RutaQuestion::class.java, FIELD_TYPE)
                    .registerSubtype(SimpleRutaQuestion::class.java, TYPE_SIMPLE)
                    .registerSubtype(ComplexRutaQuestion::class.java, TYPE_COMPLEX)
                val adapterPaint = RuntimeTypeAdapterFactory
                    .of(PaintContryQuestion::class.java, FIELD_TYPE)
                    .registerSubtype(PaintOnceQuestion::class.java, TYPE_ONCE)
                    .registerSubtype(PaintObjectQuestion::class.java, TYPE_OBJ)
                val adapterFind = RuntimeTypeAdapterFactory
                    .of(FindQuestion::class.java, FIELD_TYPE)
                    .registerSubtype(FindByImageQuestion::class.java, TYPE_IMAGE)
                    .registerSubtype(FindByDescriptionQuestion::class.java, TYPE_TEXT)
                val gson: Gson = GsonBuilder().setPrettyPrinting()
                    .registerTypeAdapterFactory(adapterQuiz)
                    .registerTypeAdapterFactory(adapterRoutes)
                    .registerTypeAdapterFactory(adapterPaint)
                    .registerTypeAdapterFactory(adapterFind)
                    .create()
                Log.d("LOG_GEOPLUS", jsonString)
                game = when(type) {
                    TYPE_UBICATE -> gson.fromJson(jsonString, FindYourselfModel::class.java)
                    TYPE_QUIZ -> gson.fromJson(jsonString, QuizModel::class.java)
                    TYPE_PINTA_ESTADOS -> gson.fromJson(jsonString, PintaEstadosModel::class.java)
                    TYPE_RUTAS -> gson.fromJson(jsonString, RutasModel::class.java)
                    else -> null
                }
                globalState.questionary = game
            }

            val composeView = findViewById<ComposeView>(R.id.compose_view)
            composeView.setContent {
                when(type) {
                    TYPE_UBICATE -> FindQuestionComponent(questions = game as FindYourselfModel, questionId = questionId)
                    TYPE_QUIZ -> QuizQuestionComponent(questions = game as QuizModel, questionId = questionId)
                    TYPE_PINTA_ESTADOS -> PaintQuestionComponent(questions = game as PintaEstadosModel, questionId = questionId)
                    TYPE_RUTAS -> RouteQuestionComponent(questions = game as RutasModel, questionId = questionId)
                    else -> Column {}
                }
            }
        } catch (e: Exception) {
            Log.d("LOG_GEOPLUS", e.message?:"")
        }
    }
}