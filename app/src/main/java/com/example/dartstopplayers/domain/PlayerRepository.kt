package com.example.dartstopplayers.domain

import androidx.lifecycle.LiveData


interface PlayerRepository {

    fun addPlayer(player: Player)

    fun deletePlayer(player: Player)

    fun editPlayerItem(player: Player)

    fun getListPlayers(): LiveData<List<Player>>

    fun getPlayerById(playerId: Int): Player



}