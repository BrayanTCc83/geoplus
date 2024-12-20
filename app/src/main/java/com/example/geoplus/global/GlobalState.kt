package com.example.geoplus.global

import android.content.Context
import androidx.compose.ui.util.fastFilter
import com.example.geoplus.models.GameModel
import com.example.geoplus.models.ResultGameCard
import com.example.geoplus.models.ResultModel

class GlobalState private constructor() {
    var questionary: GameModel? = null
    var ctx: Context? = null
    var fileName: String = ""
    var type: String = ""
    var questionId: Int = 0

    private var _results: ArrayList<ResultModel> = arrayListOf()
    fun addResult(res: ResultModel) {
        if (_results.fastFilter { r -> r.id == res.id }.isEmpty())
            _results.add(res)
    }

    fun clearResult() {
        _results = arrayListOf()
    }

    fun getResults(): ArrayList<ResultModel> {
        return _results
    }

    private var _resultCards: ResultGameCard? = null
    fun setCardResult(res: ResultGameCard) {
        _resultCards = res
    }

    fun clearCardResult() {
        _resultCards = null
    }

    fun getCardResult(): ResultGameCard? {
        return _resultCards
    }

    fun reset() {
        type = ""
        fileName = ""
        questionary = null
        questionId = 0
        ctx = null
        clearResult()
        clearCardResult()

    }

    companion object {
        @Volatile private var instance: GlobalState? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: GlobalState().also { instance = it }
            }
    }
}