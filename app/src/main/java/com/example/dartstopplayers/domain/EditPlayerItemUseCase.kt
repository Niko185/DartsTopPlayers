package com.example.dartstopplayers.domain

class EditPlayerItemUseCase(private val playerRepository: PlayerRepository) {

    fun editPlayerItem(player: Player) {
        playerRepository.editPlayerItem(player)
    }
}