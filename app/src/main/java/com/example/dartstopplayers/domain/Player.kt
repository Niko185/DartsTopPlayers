package com.example.dartstopplayers.domain

 data class Player(
    val nickname: String,
    val status: Boolean,
    var playerId: Int = -1
)
