package ru.set.moviebase

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import ru.set.moviebase.databinding.MainActivityBinding
import ru.set.moviebase.ui.main.view.MoviesFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setElements(savedInstanceState)
    }

    private fun setElements(savedInstanceState: Bundle?){
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar);
        setFragments(savedInstanceState)
    }

    private fun setFragments(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            setNowPlayingFragment()
            setUpcomingFragment()
        }
    }

    private fun setNowPlayingFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.nowPlayingContainer.id, MoviesFragment.newInstance())
            .commitNow()
    }

    private fun setUpcomingFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.upcomingContainer.id, MoviesFragment.newInstance())
            .commitNow()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        (menu?.findItem(R.id.menu_search)?.actionView as SearchView).setOnQueryTextListener(OnQueryTextListener())
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
}