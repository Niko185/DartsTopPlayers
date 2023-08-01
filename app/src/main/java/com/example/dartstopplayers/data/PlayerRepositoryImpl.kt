package com.example.dartstopplayers.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dartstopplayers.domain.Player
import com.example.dartstopplayers.domain.PlayerRepository
import kotlin.random.Random

object PlayerRepositoryImpl : PlayerRepository {

    private val playerListLD = MutableLiveData<List<Player>>()
    private val playerList = sortedSetOf<Player>({ o1, o2 -> o1.playerId.compareTo(o2.playerId) })
    private var autoIncrementId = 0
    init {
        for (index in 0 until 25) {
            val item = Player(nickname = "nickname: $index", status = Random.nextBoolean(), countGame = "${Random.nextInt(100,1000)}")
            addPlayer(item)
        }
    }


    override fun addPlayer(player: Player) {
        if (player.playerId == -1) {
            player.playerId = autoIncrementId++
        }
        playerList.add(player)
        updateList()
    }

    override fun deletePlayer(player: Player) {
        playerList.remove(player)
    }

    override fun editPlayerItem(player: Player) {
        val oldPlayer = getPlayerById(player.playerId)
        playerList.remove(oldPlayer)
        addPlayer(player)
    }

    override fun getListPlayers(): LiveData<List<Player>> {
        return playerListLD
    }

    override fun getPlayerById(playerId: Int): Player {
        return playerList.find {
            it.playerId == playerId
        } ?: throw RuntimeException("Player id not found")
    }

    private fun updateList() {
        playerListLD.value = playerList.toList()
    }

}