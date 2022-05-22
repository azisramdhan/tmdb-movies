package alfatih.me.moviecatalogue.feature.list.content

import alfatih.me.moviecatalogue.R
import alfatih.me.moviecatalogue.data.model.movie.MovieItem
import alfatih.me.moviecatalogue.feature.detail.movie.DetailMovieActivity
import alfatih.me.moviecatalogue.feature.detail.tv.DetailTVShowActivity
import alfatih.me.moviecatalogue.util.AppConstant
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kotlinx.android.synthetic.main.movie_list_item.*
import timber.log.Timber


class ListMovieAdapter: RecyclerView.Adapter<ListMovieAdapter.MovieHolder>() {

    private val movieItem: MutableList<MovieItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_list_item, parent, false)
        return MovieHolder(view)
    }

    override fun getItemCount(): Int = movieItem.count()

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            holder.bind(movieItem[position])
    }

    fun onReplace(movie: List<MovieItem>){
            movieItem.clear()
            movieItem.addAll(movie)
            notifyDataSetChanged()

    }

    class MovieHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(movieItem: MovieItem) {
            tv_title.text = movieItem.title
            tv_overview.text = movieItem.overview
            Glide.with(containerView.context)
                .load("${AppConstant.BASE_IMAGE_URL}${movieItem.poster_path}")
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(10)))
                .into(iv_poster)
            containerView.setOnClickListener {
                Timber.d("Item $adapterPosition Click ")
                val intent = Intent(containerView.context, DetailMovieActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("movie", movieItem)
                intent.putExtra("bundle", bundle)
                startActivity(containerView.context, intent, null)
            }
        }
    }
}