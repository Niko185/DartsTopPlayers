package com.example.dartstopplayers.presentation.screen_2


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.dartstopplayers.R


import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class AddEditorFragment : Fragment() {
   private lateinit var closeFragmentToActionListener: CloseFragmentToActionListener

    private lateinit var addEditorViewModelForFragment: AddEditorViewModelForFragment
    private lateinit var buttonSave: Button
    private lateinit var nicknameLayout: TextInputLayout
    private lateinit var nicknameEditText: EditText
    private lateinit var gameCountLayout: TextInputLayout
    private lateinit var gameCountEditText: EditText
    private var screenMode = ""
    private var playerId: Int = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is CloseFragmentToActionListener){
            closeFragmentToActionListener = context
        } else throw RuntimeException("Activity must empelemnt CloseFragmentToActionListener")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_editor, container, false)
    }

    override fun onViewCreated(viewMaket: View, savedInstanceState: Bundle?) {
        super.onViewCreated(viewMaket, savedInstanceState)
        parsAndGetParams()
        initViewModel()
        initAllViewsOnScreen(viewMaket)
        addTextChange()
        launchFinalVariantMode()
        observedViewModel()
    }

    private fun initAllViewsOnScreen(viewMaket: View) {
        buttonSave = viewMaket.findViewById(R.id.buttonSave)
        nicknameLayout = viewMaket.findViewById(R.id.nicknameLayout)
        nicknameEditText = viewMaket.findViewById(R.id.nicknameEditText)
        gameCountLayout = viewMaket.findViewById(R.id.gameCountLayout)
        gameCountEditText = viewMaket.findViewById(R.id.gameCountEditText)
    }

    private fun initViewModel() {
        addEditorViewModelForFragment = ViewModelProvider(this)[AddEditorViewModelForFragment::class.java]
    }

    private fun launchEditVariantScreen() {
        addEditorViewModelForFragment.getPlayerById(playerId)
        addEditorViewModelForFragment.player.observe(viewLifecycleOwner) {
            nicknameEditText.setText(it.nickname)
            gameCountEditText.setText(it.countGame)
        }
        buttonSave.setOnClickListener {
            addEditorViewModelForFragment.editPlayer(
                nicknameEditText.text?.toString(),
                gameCountEditText.text?.toString()
            )
        }
    }

    private fun launchAddVariantScreen() {
        buttonSave.setOnClickListener {
            addEditorViewModelForFragment.addPlayer(
                nicknameEditText.text?.toString(),
                gameCountEditText.text?.toString()
            )
        }
    }

    private fun observedViewModel() {
        addEditorViewModelForFragment.errorInputNickname.observe(viewLifecycleOwner) {
            val messageError = if (it) {
                getString(R.string.error_input_nickname)
            } else {
                null // nothing error
            }
            nicknameLayout.error = messageError
        }
        addEditorViewModelForFragment.errorInputGameCount.observe(viewLifecycleOwner) {
            val messageError = if (it) {
                getString(R.string.error_input_countGame)
            } else {
                null // nothing error
            }
            gameCountLayout.error = messageError
        }
        addEditorViewModelForFragment.closeStateScreen.observe(viewLifecycleOwner) {
            closeFragmentToActionListener.finishFragmentToAction()
        }
    }

    private fun launchFinalVariantMode() {
        when (screenMode) {
            MODE_ADD -> launchAddVariantScreen()
            MODE_EDIT -> launchEditVariantScreen()
        }
    }

    private fun addTextChange() {
        nicknameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addEditorViewModelForFragment.resetErrorInputNickname()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        gameCountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addEditorViewModelForFragment.resetErrorInputGameCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun parsAndGetParams() {
        val args = requireArguments()
        if (!args.containsKey(KEY_MODE)) { // Если KEY_MODE не содержит параметров
            throw RuntimeException("Param KEY_MODE not found")
        }
        val mode = args.getString(KEY_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Value: $mode is not found")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(KEY_PLAYER_ID)) {
                throw RuntimeException("Param playerId is not found")
            }
            playerId = args.getInt(KEY_PLAYER_ID, -1)
        }
    }

    interface CloseFragmentToActionListener { // Закрыть фрагмент по действию
        fun finishFragmentToAction()
    }

    companion object {
        private const val KEY_MODE = "mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val KEY_PLAYER_ID = "playerId"

        fun newInstanceAddFragmentMode(): AddEditorFragment {
            return AddEditorFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditFragmentMode(playerId: Int): AddEditorFragment {
            return AddEditorFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_MODE, MODE_EDIT)
                    putInt(KEY_PLAYER_ID, playerId)
                }
            }
        }
    }
}
