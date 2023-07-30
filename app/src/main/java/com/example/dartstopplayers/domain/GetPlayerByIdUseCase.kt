package com.example.dartstopplayers.domain

class GetPlayerByIdUseCase(private val playerRepository: PlayerRepository) {

    fun getPlayerById(playerId: Int) : Player {
        return playerRepository.getPlayerById(playerId)
    }
}