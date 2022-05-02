package info.androidhive.glide.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import info.androidhive.glide.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private var tablayout: TabLayout? = null
    private val fragmentGrid: GridFragment = GridFragment()
    private val fragmentList: ListFragment = ListFragment()
    var now = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_test, fragmentGrid, "Grid")
        fragmentTransaction.add(R.id.fragment_test, fragmentList, "List")
        fragmentTransaction.hide(fragmentList)
        fragmentTransaction.commit()

        tablayout = findViewById(R.id.tabLayoutMain)
        tablayout?.addOnTabSelectedListener(object : OnTabSelectedListener {
            //按下要做的事
            override fun onTabSelected(tab: TabLayout.Tab) {
                // Step03-寫一個方法，tab.getPosition是按下哪個按鈕，將之傳入fragmentChange方法內:
                fragmentChange(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
    // Step04-切換顯示方法撰寫:
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