package ru.set.moviebase.ui.main.view

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import ru.set.moviebase.R
import ru.set.moviebase.databinding.MainActivityBinding
import ru.set.moviebase.ui.main.model.MovieEntity
import ru.set.moviebase.ui.main.viewmodel.ViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: ViewModel by lazy { ViewModelProvider(this).get(ViewModel::class.java) }
    private val binding: MainActivityBinding by lazy { MainActivityBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getChosenMovie().observe(this) { changeFragment(it) }
        setElements(savedInstanceState)
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
}