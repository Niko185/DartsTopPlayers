package com.example.dartstopplayers.domain

 data class Player(
    val nickname: String,
    val status: Boolean,
    val countGame: String,
    var playerId: Int = -1
)
