package com.example.dartstopplayers.presentation.screen_2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dartstopplayers.data.PlayerRepositoryImpl
import com.example.dartstopplayers.domain.AddPlayerUseCase
import com.example.dartstopplayers.domain.EditPlayerItemUseCase
import com.example.dartstopplayers.domain.GetPlayerByIdUseCase
import com.example.dartstopplayers.domain.Player
import java.lang.Exception

class AddEditorViewModel: ViewModel() {

    private val repository = PlayerRepositoryImpl

    private val getPlayerByIdUseCase = GetPlayerByIdUseCase(repository)
    private val editPlayerItemUseCase = EditPlayerItemUseCase(repository)
    private val addPlayerUseCase = AddPlayerUseCase(repository)

    private val _errorInputNickname = MutableLiveData<Boolean>() // Для ВьюМодели чтобы была возможность класть значения
    val errorInputNickname: LiveData<Boolean> // Для подписки с Активности чтобы небыло возможности класть значения
        get() = _errorInputNickname

    private val _errorInputGameCount = MutableLiveData<Boolean>() // Для ВьюМодели
    val errorInputGameCount: LiveData<Boolean>
        get() = _errorInputGameCount

    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player>
        get() = _player

    private val _closeStateScreen = MutableLiveData<Boolean>()
    val closeStateScreen: LiveData<Boolean>
        get() = _closeStateScreen

    fun getPlayerById(playerId: Int) {
        val concreticItem = getPlayerByIdUseCase.getPlayerById(playerId)
        _player.value = concreticItem
    }







    fun editPlayer(inputNickname: String?, inputGameCount: String?) {
        val nickname = setupInputNickname(inputNickname)
        val gameCount = setupInputGameCount(inputGameCount)
        val resultValidation: Boolean = validateAllInputData(nickname, gameCount)
        if(resultValidation ==  true){
            _player.value?.let{
                val concreticItem = it.copy(nickname, true, gameCount.toString())
                editPlayerItemUseCase.editPlayerItem(concreticItem)
                finishWorkCloseScreen()
            }

        }
    }

    fun addPlayer(inputNickname: String?, inputGameCount: String?) {
        val nickname = setupInputNickname(inputNickname)
        val gameCount = setupInputGameCount(inputGameCount)
        val resultValidation: Boolean = validateAllInputData(nickname, gameCount)
        if(resultValidation ==  true){
            val player =Player(nickname, true, gameCount.toString())
            addPlayerUseCase.addPlayer(player)
            finishWorkCloseScreen()
        }
    }

    private fun setupInputNickname(inputNickname: String?): String {
        return inputNickname?.trim() ?: ""
    }

    private fun setupInputGameCount(inputGameCount: String?): Int {
        return try {
            inputGameCount?.trim()?.toInt() ?: 0
        } catch(error: Exception) {
            0
        }
    }

    private fun validateAllInputData(nickname: String, gameCount: Int): Boolean {
        var resultValidation: Boolean = true

        if(nickname.isBlank()) {
            _errorInputNickname.value = true
            resultValidation = false
        }

        if(gameCount <= 0){
            _errorInputGameCount.value = true
           resultValidation = false
        }

        return resultValidation
    }

    fun resetErrorInputNickname() {
        _errorInputNickname.value = false
    }

    fun resetErrorInputGameCount() {
        _errorInputGameCount.value = false
    }

   private fun finishWorkCloseScreen() {
        _closeStateScreen.value = true
    }

}