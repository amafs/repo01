package info.androidhive.glide.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import info.androidhive.glide.R
import info.androidhive.glide.databinding.ActivityMainBinding
import info.androidhive.glide.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private val fragmentGrid: ImageFragment = ImageFragment("Grid")
    private val fragmentList: ImageFragment = ImageFragment("List")
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    var now = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupUI()

    }

    private fun setupUI() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.fragment_test, fragmentGrid, "Grid")
        fragmentTransaction.add(R.id.fragment_test, fragmentList, "List")
        fragmentTransaction.hide(fragmentList)
        fragmentTransaction.commit()
        binding.tabLayoutMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // 寫一個方法，tab.getPosition是按下哪個按鈕，將之傳入fragmentChange方法內:
                mainViewModel.changePage(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.position.observe(this) { position ->
            fragmentChange(position)
        }
    }

    //切換顯示方法撰寫:
    private fun fragmentChange(position: Int) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        when (now) {
            0 -> fragmentTransaction.hide(fragmentGrid)
            1 -> fragmentTransaction.hide(fragmentList)
        }
        when (position) {
            0 -> fragmentTransaction.show(fragmentGrid)
            1 -> fragmentTransaction.show(fragmentList)
        }
        fragmentTransaction.commit()
        // Step06-更新目前所在的Fragment:
        now = position
    }

}