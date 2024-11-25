package com.example.geoplus.global

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.geoplus.models.Configuration
import com.example.geoplus.models.DefaultConfiguration
import com.example.geoplus.models.PlayerProgressModel
import com.example.geoplus.models.User
import com.google.gson.Gson
import java.io.File

class Database private constructor() {
    final var folder: File? = null
    final var sesionFile: File? = null
    final lateinit var progressFile: File
    final var usersFile: File? = null
    final lateinit var configurationFile: File
    var session: User? = null

    fun registerUser(ctx: Context, user: User) : Boolean {
        var uString = usersFile?.readText()?:"[]"
        if(uString == "") uString = "[]"
        val users: MutableList<User> = Gson().fromJson(uString, Array<User>::class.java).toMutableList()
        if(users.find { u: User -> u.nick == user.nick || u.email == user.email } != null) {
            Toast.makeText(
                ctx,
                "El usuario ya se encuentra registrado, prueba con otro correo o nombre de usuario.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        users.add(user)
        val content = Gson().toJson(users)
        usersFile?.createNewFile()
        usersFile?.writeText(content)
        Log.d("LOG_GEOPLUS", content)
        return true
    }

    fun loginUser(ctx: Context, user: User) : Boolean {
        var uString = usersFile?.readText()?:"[]"
        if(uString == "") uString = "[]"
        val users: List<User> = Gson().fromJson(uString, Array<User>::class.java).toList()
        val user = users.find { u: User -> u.nick == user.nick && u.password == user.password }
        if(user == null) {
            Toast.makeText(
                ctx,
                "La informacion es incorrecta, intenta de nuevo o crea una cuenta.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        session = user
        val content = Gson().toJson(user)
        sesionFile?.createNewFile()
        sesionFile?.writeText(content)
        return true
    }

    fun logout(): Boolean {
        sesionFile?.createNewFile()
        sesionFile?.writeText("")
        return true
    }

        fun isActiveSession() : Boolean {
        var uString = sesionFile?.readText()?:"{}"
        if(uString == "") uString = "{}"
        if(uString == "{}")
            return false
        val user: User = Gson().fromJson(uString, User::class.java) ?: return false

            session = user
        return true
    }

    fun progressExists(game: String) : Boolean {
        progressFile = File(folder, "${session?.nick?:""}_progress_$game.json")
        return progressFile.exists()?:false
    }

    fun getProgress(game: String) : PlayerProgressModel {
        if(!isActiveSession())
            return PlayerProgressModel(0, floatArrayOf(0f))
        progressFile = File(folder, "${session?.nick}_progress_$game.json")
        if(!progressFile.exists()) {
            progressFile.createNewFile()
        }

        var uString = progressFile?.readText()?:"{}"
        if(uString == "") uString = "{}"
        val progress: PlayerProgressModel = Gson().fromJson(uString, PlayerProgressModel::class.java)
        return progress
    }

    fun saveProgress(game: String, progress: PlayerProgressModel) : Boolean {
        if(!isActiveSession())
            return false
        progressFile = File(folder, "${session?.nick}_progress_$game.json")
        if(!progressFile.exists()) {
            progressFile.createNewFile()
        }

        val content = Gson().toJson(progress)
        progressFile.createNewFile()
        progressFile.writeText(content)
        return true
    }

    fun getConfiguration(): Configuration {
        configurationFile = File(folder, "${session?.nick}_configuration.json")
        if(!configurationFile.exists()) {
            return DefaultConfiguration
        }
        val content: String? = configurationFile.readText()
        if(content.isNullOrEmpty()) {
            return DefaultConfiguration
        }
        val configuration: Configuration = Gson().fromJson(content, Configuration::class.java)
        return configuration
    }

    fun saveConfiguration(configuration: Configuration): Boolean {
        if(!isActiveSession())
            return false
        configurationFile = File(folder, "${session?.nick}_configuration.json")
        configurationFile.createNewFile()

        val content = Gson().toJson(configuration)
        configurationFile.writeText(content)
        return true
    }

    fun tryInit(ctx: Context) {
        try {
            // Cargando progreso del usuario
            val path = ctx.getExternalFilesDir(null)
            folder = File(path, "geoplus_data")
            folder?.mkdirs()

            sesionFile = File(folder, "sesion.json")
            usersFile = File(folder, "users.json")

            if(!sesionFile?.exists()!!)
                sesionFile?.createNewFile()
            if(!usersFile?.exists()!!)
                usersFile?.createNewFile()
        } catch(e: Exception) {
            Log.d("LOG_GEOPLUS", e.message?:"Error inesperado")
        }
    }

    companion object {
        @Volatile private var instance: Database? = null
        fun getInstance() =
                instance ?: synchronized(this) {
            instance ?: Database().also { instance = it }
        }
    }
}
