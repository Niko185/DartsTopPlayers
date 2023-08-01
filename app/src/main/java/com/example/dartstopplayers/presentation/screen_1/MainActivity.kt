package com.example.dartstopplayers.presentation.screen_1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.dartstopplayers.R
import com.example.dartstopplayers.presentation.screen_2.AddEditorActivity
import com.example.dartstopplayers.presentation.screen_2.AddEditorFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), AddEditorFragment.CloseFragmentToActionListener {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var rcViewPlayersAdapter: RcViewPlayersAdapter
    private var fragmentHolder: FragmentContainerView? = null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupFragmentHolderForAlbumVersion()// этот контейнер  присутствует только в альбомном располоджении телефона
        initRcView()
        initViewModel()
        observeDataRcView()
        onClickFloatingButton()
    }
    private fun setupFragmentHolderForAlbumVersion(){
        fragmentHolder = findViewById(R.id.fragmentHolder)
    }
    private fun initViewModel(){
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }
    private fun observeDataRcView(){
        mainViewModel.mainListPlayers.observe(this){
            rcViewPlayersAdapter.submitList(it)
        }
    }

    private fun isBookModeOrientationScreen(): Boolean {
        return fragmentHolder == null // Если fragmentHolder null значит ориентация книжная так как fragmentHolder будет отсутствовать в разметке.
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentHolder, fragment).addToBackStack(null).commit()
    }

    private fun initRcView() {
        val rcViewMaket = findViewById<RecyclerView>(R.id.rcViewPlayers)
        with(rcViewMaket) {
            rcViewPlayersAdapter = RcViewPlayersAdapter()
            adapter = rcViewPlayersAdapter
            recycledViewPool.setMaxRecycledViews(
                RcViewPlayersAdapter.VIEW_TYPE_ACTIVE,
                RcViewPlayersAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                RcViewPlayersAdapter.VIEW_TYPE_FINISH,
                RcViewPlayersAdapter.MAX_POOL_SIZE
            )
        }

        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = rcViewPlayersAdapter.currentList[position]
                mainViewModel.deletePlayer(item)

                val newList = ArrayList(rcViewPlayersAdapter.currentList)
                newList.removeAt(position)
                rcViewPlayersAdapter.submitList(newList)
            }

        }
        ItemTouchHelper(callback).attachToRecyclerView(rcViewMaket)
        onClickLongItemRcView()
        onClickItemRcView()

    }

    private fun  onClickLongItemRcView() {
        rcViewPlayersAdapter.onPlayerItemListenerLongClick = {
            mainViewModel.changeStatusPlayer(it)
        }
    }

    private fun onClickItemRcView() {
        rcViewPlayersAdapter.onPlayerItemListenerShortClick = {
            if (isBookModeOrientationScreen()) {
                val startIntent = AddEditorActivity.newIntentStartScreenEditMode(this, it.playerId)
                startActivity(startIntent)
            } else launchFragment(AddEditorFragment.newInstanceEditFragmentMode(it.playerId))
        }
    }

    private fun onClickFloatingButton() {
        val buttonAddFloating = findViewById<FloatingActionButton>(R.id.floatingButton)
        buttonAddFloating.setOnClickListener {
            if (isBookModeOrientationScreen()) {
                val startIntent = AddEditorActivity.newIntentStartScreenAddMode(this)
                startActivity(startIntent)
            } else {
                launchFragment(AddEditorFragment.newInstanceAddFragmentMode())
            }
        }
    }

    override fun finishFragmentToAction() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

}