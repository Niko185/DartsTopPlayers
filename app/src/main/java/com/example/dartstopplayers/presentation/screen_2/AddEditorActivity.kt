package com.example.dartstopplayers.presentation.screen_2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dartstopplayers.R
import java.lang.RuntimeException

class AddEditorActivity: AppCompatActivity(), AddEditorFragment.CloseFragmentToActionListener {
    private var screenMode = ""
    private var playerId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_editor)
        parseAndGetIntent()
        if(savedInstanceState == null) { // Значит активити не пересоздавалось
            launchFinalVariantMode()
        }
    }

    private fun launchFinalVariantMode() {
        val fragment = when(screenMode) {
            MODE_ADD -> AddEditorFragment.newInstanceAddFragmentMode()
            MODE_EDIT -> AddEditorFragment.newInstanceEditFragmentMode(playerId)
            else -> throw RuntimeException("Screen name key not found")
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.add_editor_container, fragment)
            .commit()
    }

    private fun parseAndGetIntent() {
        if (!intent.hasExtra(KEY_MODE)) { // Если KEY_MODE не содержит параметров
            throw RuntimeException("Param KEY_MODE not found")
        }
        val mode = intent.getStringExtra(KEY_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Value: $mode is not found")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(KEY_PLAYER_ID)) {
                throw RuntimeException("Param playerId is not found")
            }
            playerId = intent.getIntExtra(KEY_PLAYER_ID, -1)
        }
    }

    companion object {
        private const val KEY_MODE = "mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val KEY_PLAYER_ID = "playerId"

        fun newIntentStartScreenAddMode(context: Context): Intent {
            val intent = Intent(context, AddEditorActivity::class.java)
            intent.putExtra(KEY_MODE, MODE_ADD)
            return intent
        }

        fun newIntentStartScreenEditMode(context: Context, playerId: Int): Intent {
            val intent = Intent(context, AddEditorActivity::class.java)
            intent.putExtra(KEY_MODE, MODE_EDIT)
            intent.putExtra(KEY_PLAYER_ID, playerId)
            return intent
        }
    }

    override fun finishFragmentToAction() {
        finish()
    }
}
