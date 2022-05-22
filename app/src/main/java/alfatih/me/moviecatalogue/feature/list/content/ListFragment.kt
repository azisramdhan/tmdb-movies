package alfatih.me.moviecatalogue.feature.list.content

import alfatih.me.moviecatalogue.R
import alfatih.me.moviecatalogue.util.AppConstant.CATEGORY_MOVIES
import alfatih.me.moviecatalogue.util.isNetworkAvailable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ybq.android.spinkit.style.Wave
import kotlinx.android.synthetic.main.fragment_list.*

private const val CATEGORY = "category"
private const val FAVORITE = "favorite"

class ListFragment: Fragment() {

    private var category: String? = null
    private var favorite: Boolean = false
    private lateinit var viewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(CATEGORY)
            favorite = it.getBoolean(FAVORITE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupSearchView()
    }

    private fun setupSearchView() {
        if (!favorite){
            search_view.visibility = View.VISIBLE
            search_view.setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    if(newText.length > 3){
                        if(isNetworkAvailable()){
                            spin_kit.visibility = View.VISIBLE
                            if(category.equals(CATEGORY_MOVIES)){
                                viewModel.searchMovie(newText)
                            }else{
                                viewModel.searchTVShow(newText)
                            }
                        }else{
                            showPopup()
                            return false
                        }
                    }
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }
            })
        }else{
            search_view.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun setupView() {
        val wave = Wave()
        spin_kit.setIndeterminateDrawable(wave)
    }

    private fun initData() {
        viewModel = ViewModelProviders.of(this)[ListViewModel::class.java]
        if(!favorite){
            viewModel.movieList.value.let {
                if(it.isNullOrEmpty()){
                    if(isNetworkAvailable()){
                        viewModel.fetchData()
                    }else{
                        showPopup()
                        return
                    }
                }
            }
        }else{
            context?.let {
                viewModel.fetchLocalDataMovie(it)
                viewModel.fetchLocalDataTV(it)
            }
        }
        if (category.equals(CATEGORY_MOVIES)){
            val adapter = ListMovieAdapter()
            spin_kit.visibility = View.VISIBLE
            viewModel.movieList.observe(this, Observer {
                adapter.onReplace(it)
                spin_kit.visibility = View.INVISIBLE
            })
            rv_movies.adapter = adapter
            rv_movies.layoutManager = LinearLayoutManager(context)
        }else{
            val adapter = ListTVShowAdapter()
            spin_kit.visibility = View.VISIBLE
            viewModel.tvShowList.observe(this, Observer {
                adapter.onReplace(it)
                spin_kit.visibility = View.INVISIBLE
            })
            rv_movies.adapter = adapter
            rv_movies.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showPopup() {
        context?.let {
            val builder = AlertDialog.Builder(it)
                .setCancelable(false)
                .setTitle(resources.getString(R.string.popup_title))
                .setMessage(resources.getString(R.string.popup_message))
                .setPositiveButton("OK"){dialog, which ->
                    activity?.finish()
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(category: String, favorite: Boolean) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(CATEGORY, category)
                    putBoolean(FAVORITE, favorite)
                }
            }
    }
}
