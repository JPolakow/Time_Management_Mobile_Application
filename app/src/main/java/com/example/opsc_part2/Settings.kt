package com.example.opsc_part2

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.viewpager.widget.ViewPager
import com.example.opsc_part2.AchievementsFragment
import com.example.opsc_part2.GeneralFragment
import com.example.opsc_part2.ProfileFragment
import com.google.android.material.tabs.TabLayout

class Settings : AppCompatActivity() {

    //tabs
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager


    //============================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        val adapter = SettingsPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        val btnBack = findViewById<ImageButton>(R.id.imageBtnBackArrow)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        val customTabSelectedListener = CustomTabSelectedListener(tabLayout)
        tabLayout.setSelectedTabIndicator(R.drawable.tab_indicator_anim)
        tabLayout.addOnTabSelectedListener(customTabSelectedListener)


        btnBack.setOnClickListener{
            val intent = Intent(this, Dashboard::class.java )
            startActivity(intent)
        }
    }

    //============================================================================
    // This function is used to add items in arraylist and assign
    // the adapter to view pager
    private inner class SettingsPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return 3
        }

        //populate the tabs
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> GeneralFragment()
                1 -> ProfileFragment()
                2 -> AchievementsFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }

        //populate the tab headings
        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "General"
                1 -> "Profile"
                2 -> "Achievements"
                else -> null
            }

        }
    }
}

//============================================================================
//============================================================================

class CustomTabSelectedListener(private val tabLayout: TabLayout) : TabLayout.OnTabSelectedListener {
    private val animationDuration = 300L // Animation duration in milliseconds

    //============================================================================
    override fun onTabSelected(tab: TabLayout.Tab) {
        val selectedColor = ContextCompat.getColor(tabLayout.context, R.color.black)
        val textView =
            tab.customView?.findViewById<TextView>(R.id.tabLayout) // Replace R.id.tab_title with the ID of your tab title TextView

        textView?.setTextColor(selectedColor)

        val view = tab.view
        view?.let {
            val scaleXAnimator = ObjectAnimator.ofFloat(it, View.SCALE_X, 1f)
            scaleXAnimator.duration = animationDuration
            scaleXAnimator.interpolator = FastOutSlowInInterpolator()
            scaleXAnimator.start()

            val scaleYAnimator = ObjectAnimator.ofFloat(it, View.SCALE_Y, 1f)
            scaleYAnimator.duration = animationDuration
            scaleYAnimator.interpolator = FastOutSlowInInterpolator()
            scaleYAnimator.start()
        }
    }


    //============================================================================
    override fun onTabUnselected(tab: TabLayout.Tab) {
        val defaultColor = ContextCompat.getColor(tabLayout.context, R.color.black)
        val textView = tab.customView?.findViewById<TextView>(R.id.tabLayout) // Replace R.id.tab_title with the ID of your tab title TextView

        textView?.setTextColor(defaultColor)

        val view = tab.view
        view?.let {
            val scaleXAnimator = ObjectAnimator.ofFloat(it, View.SCALE_X, 0.8f)
            scaleXAnimator.duration = animationDuration
            scaleXAnimator.interpolator = FastOutSlowInInterpolator()
            scaleXAnimator.start()

            val scaleYAnimator = ObjectAnimator.ofFloat(it, View.SCALE_Y, 0.8f)
            scaleYAnimator.duration = animationDuration
            scaleYAnimator.interpolator = FastOutSlowInInterpolator()
            scaleYAnimator.start()
        }
    }

    //============================================================================
    override fun onTabReselected(tab: TabLayout.Tab) {
        val viewPager = tabLayout.rootView.findViewById<ViewPager>(R.id.viewPager)
        viewPager?.setCurrentItem(tab.position, true)
    }
}



// Create the object of Toolbar, ViewPager and
// TabLayout and use “findViewById()” method*/
//    var tab_toolbar = findViewById<Toolbar>(R.id.toolbar)
/* var tab_viewpager = findViewById<ViewPager>(R.id.tab_viewpager)
 var tab_tablayout = findViewById<TabLayout>(R.id.tab_tablayout)*/

// As we set NoActionBar as theme to this activity
// so when we run this project then this activity doesn't
// show title. And for this reason, we need to run
// setSupportActionBar method
//  setSupportActionBar(tab_toolbar)
//setupViewPager(tab_viewpager)

// If we dont use setupWithViewPager() method then
// tabs are not used or shown when activity opened
// tab_tablayout.setupWithViewPager(tab_viewpager)

/*  private fun setupViewPager(viewpager: ViewPager) {
var adapter: ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

// LoginFragment is the name of Fragment and the Login
// is a title of tab
adapter.addFragment(GeneralFragment(), "General")
adapter.addFragment(ProfileFragment(), "Profile")
adapter.addFragment(AchievementsFragment(), "Achievements")


// setting adapter to view pager.
viewpager.setAdapter(adapter)
}*/

// This "ViewPagerAdapter" class overrides functions which are
// necessary to get information about which item is selected
// by user, what is title for selected item and so on.*/
// class ViewPagerAdapter : FragmentPagerAdapter {

// objects of arraylist. One is of Fragment type and
// another one is of String type.*/
//private final var fragmentList1: ArrayList<Fragment> = ArrayList()
//private final var fragmentTitleList1: ArrayList<String> = ArrayList()

// this is a secondary constructor of ViewPagerAdapter class.
//public constructor(supportFragmentManager: FragmentManager)
//        : super(supportFragmentManager)

/*  // returns which item is selected from arraylist of fragments.
override fun getItem(position: Int): Fragment {
return fragmentList1.get(position)
}

// returns which item is selected from arraylist of titles.
override fun getPageTitle(position: Int): CharSequence {
return fragmentTitleList1.get(position)
}

// returns the number of items present in arraylist.
override fun getCount(): Int {
return fragmentList1.size
}

// this function adds the fragment and title in 2 separate arraylist.
fun addFragment(fragment: Fragment, title: String) {
fragmentList1.add(fragment)
fragmentTitleList1.add(title)
}*/
