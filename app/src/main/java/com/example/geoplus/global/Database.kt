package com.example.geoplus.global

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.geoplus.models.PlayerProgressModel
import com.example.geoplus.models.User
import com.google.gson.Gson
import java.io.File

class Database private constructor() {
    final var folder: File? = null
    final var sesionFile: File? = null
    final var progressFile: File? = null
    final var usersFile: File? = null
    var session: User? = null

    fun registerUser(ctx: Context, user: User) : Boolean {
        var uString = usersFile?.readText()?:"[]"
        if(uString == "") uString = "[]"
        Log.d("LOG_GEOPLUS", uString)
        val users: MutableList<User> = Gson().fromJson(uString, Array<User>::class.java).toMutableList()
        users.forEach {
            u -> Log.d("LOG_GEOPLUS", "Login ${u.nick}")
        }
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
        Log.d("LOG_GEOPLUS", uString)
        val users: List<User> = Gson().fromJson(uString, Array<User>::class.java).toList()
        users.forEach {
            u -> Log.d("LOG_GEOPLUS", "Login ${u.nick}")
        }
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
        Log.d("LOG_GEOPLUS", "Login $content")
        return true
    }

    fun logout(): Boolean {
        sesionFile?.createNewFile()
        sesionFile?.writeText("")
        Log.d("LOG_GEOPLUS", "Logout")
        return true
    }

    fun isActiveSession() : Boolean {
        var uString = sesionFile?.readText()?:"{}"
        if(uString == "") uString = "{}"
        if(uString == "{}")
            return false
        Log.d("LOG_GEOPLUS", uString)
        val user: User? = Gson().fromJson(uString, User::class.java)
        if(user == null) {
            Log.d("LOG_GEOPLUS", "Sin sesion")
            return false
        }

        session = user
        Log.d("LOG_GEOPLUS", "Sesion activa")
        return true
    }

    fun progressExists(game: String) : Boolean {
        Log.d("LOG_GEOPLUS", "Progress file ${session?.nick?:""}_progress_$game.json")
        progressFile = File(folder, "${session?.nick?:""}_progress_$game.json")
        return progressFile?.exists()?:false
    }

    fun getProgress(game: String) : PlayerProgressModel {
        if(!isActiveSession())
            return PlayerProgressModel(0, floatArrayOf(0f))
        progressFile = File(folder, "${session?.nick}_progress_$game.json")
        if(!progressFile?.exists()!!)
            progressFile?.createNewFile()

        Log.d("LOG_GEOPLUS", "Progress $progressFile: ${progressFile?.readText()?:"Vacio"}")
        var uString = progressFile?.readText()?:"{}"
        if(uString == "") uString = "{}"
        val progress: PlayerProgressModel = Gson().fromJson(uString, PlayerProgressModel::class.java)
        return progress
    }

    fun saveProgress(game: String, progress: PlayerProgressModel) : Boolean {
        Log.d("LOG_GEOPLUS", "${session?.nick}_progress_$game.json <- $progress")
        if(!isActiveSession())
            return false
        progressFile = File(folder, "${session?.nick}_progress_$game.json")
        if(!progressFile?.exists()!!)
            progressFile?.createNewFile()

        Log.d("LOG_GEOPLUS", "Progress $progressFile: ${progressFile?.readText()?:"Vacio"}")
        val content = Gson().toJson(progress)
        progressFile?.createNewFile()
        progressFile?.writeText(content)
        Log.d("LOG_GEOPLUS", content)
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

            Log.d("LOG_GEOPLUS", "Folder $path exists: ${folder?.exists()}")
            val sesionFile = File(folder, "sesion.json")
            Log.d("LOG_GEOPLUS", "Sesion $sesionFile: ${sesionFile.readText()}")
            val usersFile = File(folder, "users.json")
            Log.d("LOG_GEOPLUS", "Sesion $usersFile: ${usersFile.readText()}")
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
