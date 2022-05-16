package info.androidhive.glide.adapter

import android.content.Context
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import info.androidhive.glide.databinding.GalleryThumbnailGridBinding
import info.androidhive.glide.databinding.GalleryThumbnailListBinding
import info.androidhive.glide.model.Image

class GalleryAdapter(
    private val mContext: Context,
    images: List<Image>,
    private val tabPosition: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val images: List<Image>

    inner class ListViewHolder(binding: GalleryThumbnailListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var thumbnail: ImageView
        var listName: TextView

        init {
            thumbnail = binding.listThumbNail
            listName = binding.listName
        }
    }

    inner class GridViewHolder(binding: GalleryThumbnailGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var thumbnail: ImageView

        init {
            thumbnail = binding.thumbnail
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        when (tabPosition) {
            1 -> {
                val viewList = GalleryThumbnailListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                viewHolder = ListViewHolder(viewList)
            }
            0 -> {
                val viewGrid = GalleryThumbnailGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                viewHolder = GridViewHolder(viewGrid)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val image: Image = images[position]

        when (tabPosition) {
            1 -> {
                val listHolder: ListViewHolder = holder as ListViewHolder
                Glide.with(mContext).load(image.medium)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(listHolder.thumbnail)
                listHolder.listName.setText(image.name)
            }
            0 -> {
                val gridHolder: GridViewHolder = holder as GridViewHolder
                Glide.with(mContext).load(image.medium)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(gridHolder.thumbnail)
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    interface ClickListener {
        fun onClick(view: View?, position: Int)
        fun onLongClick(view: View?, position: Int)
    }

    class RecyclerTouchListener(
        context: Context?,
        recyclerView: RecyclerView,
        private val clickListener: ClickListener?
    ) :
        OnItemTouchListener {
        private val gestureDetector: GestureDetector
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

        init {
            gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(
                            child,
                            recyclerView.getChildAdapterPosition(child)
                        )
                    }
                }
            })
        }
    }

    init {
        this.images = images
    }

}