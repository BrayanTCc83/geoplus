package com.example.geoplus.models

import kotlinx.serialization.Serializable

interface GameModel {
    val title: String
    val game: String
    val id: Int
};

interface QuizQuestionModel {
    val question: String
    val type: String
    val points: Int
    val image: String
};

@Serializable
data class QuizCheckboxQuestionModel(
    override val question: String,
    override val type: String,
    override val points: Int,
    override val image: String,
    val answer: Array<Int>,
    val options: Array<String>
): QuizQuestionModel

@Serializable
data class QuizRadioQuestionModel(
    override val question: String,
    override val type: String,
    override val points: Int,
    override val image: String,
    val answer: Int,
    val options: Array<String>,
): QuizQuestionModel

@Serializable
data class QuizOpenQuestionModel(
    override val question: String,
    override val type: String,
    override val points: Int,
    override val image: String,
    val answer: String,
    val tries: Int
): QuizQuestionModel

@Serializable
data class QuizModel(
    override val title: String,
    override val game: String,
    override val id: Int,
    val questions: Array<QuizQuestionModel>
): GameModel

interface PaintContryQuestion {
    val question: String
    val type: String
    val points: Int
}

@Serializable
data class PaintOnceQuestion(
    override val type: String,
    override val question: String,
    override val points: Int,
    val answer: Array<Int>,
    val color: Int
): PaintContryQuestion

@Serializable
data class PaintObjectQuestion(
    override val type: String,
    override val question: String,
    override val points: Int,
    val answer: Map<String, Array<Int>>
): PaintContryQuestion

@Serializable
data class PintaEstadosModel(
    override val title: String,
    override val game: String,
    override val id: Int,
    val questions: Array<PaintContryQuestion>
): GameModel

interface RutaQuestion {
    val question: String
    val type: String
    val start: Int
    val end: Int
    val points: Int
}

@Serializable
data class ComplexRutaQuestion(
    override val type: String,
    override val question: String,
    override val start: Int,
    override val end: Int,
    override val points: Int,
    val nodes: Array<Int>,
    val nodesSize: Int
): RutaQuestion

@Serializable
data class SimpleRutaQuestion(
    override val type: String,
    override val question: String,
    override val start: Int,
    override val end: Int,
    override val points: Int,
): RutaQuestion

@Serializable
data class RutasModel(
    override val title: String,
    override val game: String,
    override val id: Int,
    val questions: Array<RutaQuestion>
): GameModel

interface FindQuestion {
    val question: String
    val type: String
    val points: Int
    val answer: Int
}

@Serializable
data class FindByImageQuestion(
    override val type: String,
    override val question: String,
    override val points: Int,
    override val answer: Int,
    val image: String,
    val options: Array<String>
): FindQuestion

@Serializable
data class FindByDescriptionQuestion(
    override val type: String,
    override val question: String,
    override val points: Int,
    override val answer: Int,
    val options: Array<Int>
): FindQuestion

@Serializable
data class FindYourselfModel(
    override val title: String,
    override val game: String,
    override val id: Int,
    val questions: Array<FindQuestion>
): GameModel


@Serializable
data class MemoramaModel (
    override val title: String,
    override val game: String,
    override val id: Int,
    val type: String,
    val cards: Array<CardMemo>
): GameModel