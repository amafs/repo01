package info.androidhive.glide.activity
import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import info.androidhive.glide.R
import info.androidhive.glide.adapter.GalleryAdapter
import info.androidhive.glide.app.AppController
import info.androidhive.glide.model.Image
import org.json.JSONException
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import androidx.fragment.app.FragmentTransaction;

class ListFragment :Fragment(){
    private val TAG = ListFragment::class.java.simpleName
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View{
        return inflater.inflate(R.layout.fragment_list, container, false)
    }
}