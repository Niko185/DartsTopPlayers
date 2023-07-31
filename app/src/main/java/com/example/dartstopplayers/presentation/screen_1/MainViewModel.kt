package com.example.dartstopplayers.presentation.screen_1



import androidx.lifecycle.ViewModel
import com.example.dartstopplayers.data.PlayerRepositoryImpl
import com.example.dartstopplayers.domain.DeletePlayerUseCase
import com.example.dartstopplayers.domain.EditPlayerItemUseCase
import com.example.dartstopplayers.domain.GetListPlayersUseCase

import com.example.dartstopplayers.domain.Player

class MainViewModel: ViewModel() {

    private val repository = PlayerRepositoryImpl


    private val deletePlayerUseCase = DeletePlayerUseCase(repository)
    private val editPlayerItemUseCase = EditPlayerItemUseCase(repository)
    private val getListPlayersUseCase = GetListPlayersUseCase(repository)


    val mainListPlayers = getListPlayersUseCase.getListPlayers()

     fun deletePlayer(player: Player){
        deletePlayerUseCase.deletePlayer(player)
    }
     fun changeStatusPlayer(player: Player){
        val newItem = player.copy(status = !player.status)
        editPlayerItemUseCase.editPlayerItem(newItem)
    }

}