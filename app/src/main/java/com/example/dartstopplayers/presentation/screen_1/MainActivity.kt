package com.example.dartstopplayers.presentation.screen_1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.dartstopplayers.R
import com.example.dartstopplayers.presentation.screen_2.AddEditorActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var rcViewPlayersAdapter: RcViewPlayersAdapter

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRcView()
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.mainListPlayers.observe(this){
            rcViewPlayersAdapter.submitList(it)
        }
        onClickFloatingButton()
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
            val startIntent = AddEditorActivity.newIntentStartScreenEditMode(this, it.playerId)
            startActivity(startIntent)
        }
    }

    private fun onClickFloatingButton() {
        val buttonAddFloating = findViewById<FloatingActionButton>(R.id.floatingButton)
        buttonAddFloating.setOnClickListener {
            val startIntent = AddEditorActivity.newIntentStartScreenAddMode(this)
            startActivity(startIntent)
        }
    }

}