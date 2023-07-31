package com.example.dartstopplayers.presentation.screen_2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dartstopplayers.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception
import java.lang.RuntimeException

class AddEditorActivity: AppCompatActivity() {
    private lateinit var buttonSave: Button
    private lateinit var nicknameLayout: TextInputLayout
    private lateinit var nicknameEditText: EditText
    private lateinit var gameCountLayout: TextInputLayout
    private lateinit var gameCountEditText: EditText
    private lateinit var addEditorViewModel: AddEditorViewModel

    private var valueForScreen = "" // Можно сделать константу VALUE_UNKNOWN
    private var playerId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_editor)
        parseIntent()
        initViewModel()
        initAllViewsOnScreen()
        addTextChange()
        /*getIntentValue()
        getIntentValue2()*/
        launchFinalVariantMode()
        observedViewModel()
    }

    private fun initAllViewsOnScreen() {
        buttonSave = findViewById(R.id.buttonSave)
        nicknameLayout = findViewById(R.id.nicknameLayout)
        nicknameEditText = findViewById(R.id.nicknameEditText)
        gameCountLayout = findViewById(R.id.gameCountLayout)
        gameCountEditText = findViewById(R.id.gameCountEditText)
    }

    private fun initViewModel() {
        addEditorViewModel = ViewModelProvider(this)[AddEditorViewModel::class.java]
    }

    private fun launchEditVariantScreen() {
        addEditorViewModel.getPlayerById(playerId)
        addEditorViewModel.player.observe(this){
            nicknameEditText.setText(it.nickname)
            gameCountEditText.setText(it.countGame)
        }
        buttonSave.setOnClickListener {
            addEditorViewModel.editPlayer(nicknameEditText.text?.toString(), gameCountEditText.text?.toString())
        }
    }

    private fun launchAddVariantScreen() {
        buttonSave.setOnClickListener {
            addEditorViewModel.addPlayer(nicknameEditText.text?.toString(), gameCountEditText.text?.toString())
        }
    }

    private fun observedViewModel() {
        addEditorViewModel.errorInputNickname.observe(this){
            val messageError = if(it == true){
                getString(R.string.error_input_nickname)
            } else {
                null // nothing error
            }
            gameCountLayout.error = messageError
        }
        addEditorViewModel.errorInputGameCount.observe(this){
            val messageError = if(it == true){
                getString(R.string.error_input_countGame)
            } else {
                null // nothing error
            }
            gameCountLayout.error = messageError
        }
        addEditorViewModel.closeStateScreen.observe(this){
            finish()
        }
    }

    private fun launchFinalVariantMode(){
        when(valueForScreen){
            VALUE_FOR_NAME_KEY_SCREEN_ADD -> launchAddVariantScreen()
            VALUE_FOR_NAME_KEY_SCREEN_EDIT -> launchEditVariantScreen()
        }
    }

    private fun addTextChange(){
        nicknameEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addEditorViewModel.resetErrorInputNickname()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        gameCountEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addEditorViewModel.resetErrorInputGameCount()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun parseIntent() {
        if(!intent.hasExtra(NAME_KEY_SCREEN)) { // Если интент не содержит параметра NAME_KEY_SCREEN
            throw RuntimeException("Param NAME_KEY_SCREEN not found")
        }
        val value = intent.getStringExtra(NAME_KEY_SCREEN)
        if(value != VALUE_FOR_NAME_KEY_SCREEN_EDIT && value != VALUE_FOR_NAME_KEY_SCREEN_ADD) {
            throw RuntimeException("Value: $value is not found")
        }
        valueForScreen = value
        if(valueForScreen == VALUE_FOR_NAME_KEY_SCREEN_EDIT) {
            if(!intent.hasExtra(NAME_KEY_ID)) {
                throw RuntimeException("Param playerId is not found")
            }
            playerId = intent.getIntExtra(NAME_KEY_ID, -1)
        }
    }

    // Эти методы тоже самое что и код выше
    /*private fun getIntentValue() : String? {
        val intentValue = intent.getStringExtra(NAME_KEY_SCREEN)
        return intentValue
    }

    private fun getIntentValue2() : String? {
        val intentValue = intent.getStringExtra(NAME_KEY_ID)
        return intentValue
    }*/

    companion object {

        private const val NAME_KEY_SCREEN = "screen"
        private const val VALUE_FOR_NAME_KEY_SCREEN_ADD = "mode_screen_for_add"
        private const val VALUE_FOR_NAME_KEY_SCREEN_EDIT = "mode_screen_for_edit"

        private const val NAME_KEY_ID = "id_for_player"



        fun newIntentStartScreenAddMode(context: Context): Intent {
            val intent = Intent(context, AddEditorActivity::class.java)
            intent.putExtra(NAME_KEY_SCREEN, VALUE_FOR_NAME_KEY_SCREEN_ADD)
            return intent
        }

        fun newIntentStartScreenEditMode(context: Context, playerId: Int) : Intent {
            val intent = Intent(context, AddEditorActivity::class.java)
            intent.putExtra(NAME_KEY_SCREEN, VALUE_FOR_NAME_KEY_SCREEN_EDIT)
            intent.putExtra(NAME_KEY_ID, playerId)
            return intent
        }
    }


}