package ru.set.moviebase.ui.main.view

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationBarView
import ru.set.moviebase.R
import ru.set.moviebase.databinding.MainActivityBinding
import ru.set.moviebase.ui.main.model.MovieEntity
import ru.set.moviebase.ui.main.viewmodel.MainBroadcastReceiver
import ru.set.moviebase.ui.main.viewmodel.ViewModel
import java.util.*

class MainActivity : AppCompatActivity() {
    private val viewModel: ViewModel by lazy { ViewModelProvider(this).get(ViewModel::class.java) }
    private val binding: MainActivityBinding by lazy { MainActivityBinding.inflate(layoutInflater) }
    private val receiver = MainBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getChosenMovie().observe(this) { changeFragment(it) }
        viewModel.getErrorMessage().observe(this) { binding.root.showTextById(it) }
        setElements(savedInstanceState)
        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    private fun changeFragment(movie: MovieEntity?) {
        when {
            movie != null -> setChoosenMovieFragment()
            else -> setMoviesFragment(null)
        }
    }

    private fun setElements(savedInstanceState: Bundle?) {
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar);
        binding.navigation.setOnItemSelectedListener(onItemSelectedListener)
        setMoviesFragment(savedInstanceState)
    }

    private fun setMoviesFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.mainContainer.id, MoviesFragment.newInstance())
                .commit()
        }
    }

    private fun setChoosenMovieFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.mainContainer.id, ChoosenMovieFragment.newInstance())
            .addToBackStack("")
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        (menu?.findItem(R.id.menu_search)?.actionView as SearchView).setOnQueryTextListener(
            OnQueryTextListener()
        )
        return true
    }

    private class OnQueryTextListener : SearchView.OnQueryTextListener {
        // реагирует на конец ввода поиска
        override fun onQueryTextSubmit(query: String): Boolean {
            //TODO
            return true
        }

        // реагирует на нажатие каждой клавиши
        override fun onQueryTextChange(newText: String): Boolean {
            //TODO
            return true
        }
    }

    private val onItemSelectedListener =
        NavigationBarView.OnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_home -> binding.root.showTextById(R.string.home_menu)
                R.id.menu_favorites -> binding.root.showTextById(R.string.favorites_menu)
                R.id.menu_ratings -> binding.root.showTextById(R.string.ratings_menu)
                else -> return@OnItemSelectedListener false
            }
            return@OnItemSelectedListener true
        }
}