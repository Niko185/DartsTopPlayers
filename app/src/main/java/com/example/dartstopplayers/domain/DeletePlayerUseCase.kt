package com.example.dartstopplayers.domain

class DeletePlayerUseCase(private val playerRepository: PlayerRepository) {

    fun deletePlayer(player: Player){
        playerRepository.deletePlayer(player)
    }

}