package com.example.dartstopplayers.domain

import androidx.lifecycle.LiveData

class GetListPlayersUseCase(private val playerRepository: PlayerRepository) {

    fun getListPlayers() : LiveData<List<Player>> {
        return playerRepository.getListPlayers()
    }
}
