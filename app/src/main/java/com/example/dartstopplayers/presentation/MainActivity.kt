package com.example.dartstopplayers.presentation

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.dartstopplayers.R

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
        setupLongClickListener()
        setupClickListener()

    }

    private fun  setupLongClickListener() {
        rcViewPlayersAdapter.onPlayerItemListenerLongClick = {
            mainViewModel.changeStatusPlayer(it)
        }
    }

    private fun setupClickListener() {
        rcViewPlayersAdapter.onPlayerItemListenerShortClick = {
            Log.d("MyLog", it.toString())
        }
    }
}