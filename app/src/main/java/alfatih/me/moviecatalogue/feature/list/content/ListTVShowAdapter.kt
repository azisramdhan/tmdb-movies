package alfatih.me.moviecatalogue.feature.list.content

import alfatih.me.moviecatalogue.R
import alfatih.me.moviecatalogue.data.model.tv.TVItem
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.movie_list_item.*
import timber.log.Timber


class ListTVShowAdapter: RecyclerView.Adapter<ListTVShowAdapter.MovieHolder>() {

    private val tvItem: MutableList<TVItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_list_item, parent, false)
        return MovieHolder(view)
    }

    override fun getItemCount(): Int = tvItem.count()

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            holder.bind(tvItem[position])
    }

    fun onReplace(tv: List<TVItem>){
            tvItem.clear()
            tvItem.addAll(tv)
            notifyDataSetChanged()
    }

    class MovieHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(tvItem: TVItem) {
            tv_title.text = tvItem.name
            tv_overview.text = tvItem.overview
            Glide.with(containerView.context)
                .load("${AppConstant.BASE_IMAGE_URL}${tvItem.poster_path}")
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(10)))
                .into(iv_poster)
            containerView.setOnClickListener {
                Timber.d("Item $adapterPosition Click ")
                val intent = Intent(containerView.context, DetailTVShowActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("tv", tvItem)
                intent.putExtra("bundle", bundle)
                startActivity(containerView.context, intent, null)
            }
        }
    }
}