package info.androidhive.glide.activity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import info.androidhive.glide.R
import info.androidhive.glide.databinding.FragmentImageSliderBinding
import info.androidhive.glide.databinding.ImageFullscreenPreviewBinding
import info.androidhive.glide.model.Image

class SlideshowDialogFragment : DialogFragment() {
    private val TAG = SlideshowDialogFragment::class.java.simpleName
    private var images: ArrayList<Image>? = null
    //private var viewPager: ViewPager? = null
    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    //private var lblCount: TextView? = null
    //private var lblTitle: TextView? = null
    //private var lblDate: TextView? = null
    private var selectedPosition = 0
    private var _binding:FragmentImageSliderBinding?=null
    private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentImageSliderBinding.inflate(inflater,container,false)
        //val v: View = inflater.inflate(R.layout.fragment_image_slider, container, false)
        val view=binding.root
        //viewPager = v.findViewById<View>(R.id.viewpager) as ViewPager
        //lblCount = v.findViewById<View>(R.id.lbl_count) as TextView
        //lblTitle = v.findViewById<View>(R.id.title) as TextView
        //lblDate = v.findViewById<View>(R.id.date) as TextView
        images = requireArguments().getSerializable("images") as ArrayList<Image>
        selectedPosition = requireArguments().getInt("position")
        Log.e(TAG, "position: $selectedPosition")
        Log.e(TAG, "images size: " + images!!.size)
        myViewPagerAdapter = MyViewPagerAdapter()
        binding.viewpager!!.adapter = myViewPagerAdapter
        binding.viewpager!!.addOnPageChangeListener(viewPagerPageChangeListener)
        setCurrentItem(selectedPosition)
        return view
    }

    private fun setCurrentItem(position: Int) {
        binding.viewpager!!.setCurrentItem(position, false)
        displayMetaInfo(selectedPosition)
    }

    //	page change listener
    var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            displayMetaInfo(position)
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    private fun displayMetaInfo(position: Int) {
        binding.lblCount!!.text = (position + 1).toString() + " of " + images!!.size
        val image = images!![position]
        binding.title!!.text = image.name
        binding.date!!.text = image.timestamp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }

    //	adapter
    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getLayoutInflater()
            val view: View =
                layoutInflater !!.inflate(R.layout.image_fullscreen_preview, container, false)
            val imageViewPreview = view.findViewById<View>(R.id.image_preview) as ImageView
            val image = images!![position]
            Glide.with(getActivity()).load(image.large)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewPreview)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return images!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj as View
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    companion object {
        fun newInstance(): SlideshowDialogFragment {
            return SlideshowDialogFragment()
        }
    }
}