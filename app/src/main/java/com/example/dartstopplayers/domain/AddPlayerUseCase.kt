package com.example.dartstopplayers.domain

class AddPlayerUseCase(private val playerRepository: PlayerRepository) {

    fun addPlayer(player: Player) {
        playerRepository.addPlayer(player)
    }

}