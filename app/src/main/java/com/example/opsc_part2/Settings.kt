package com.example.opsc_part2

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class Settings : AppCompatActivity() {

    //tabs
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    //============================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
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


            btnBack.setOnClickListener {
                val intent = Intent(this, Dashboard::class.java)
                startActivity(intent)
            }
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    // This function is used to add items in arraylist and assign
    // the adapter to view pager
    private inner class SettingsPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            //return 3
            return 2
        }

        // Populate the tabs
        override fun getItem(position: Int): Fragment {
            return when (position) {
                //0 -> GeneralFragment()
                0 -> ProfileFragment()
                1 -> AchievementsFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }

        // Populate the tab headings
        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                //0 -> "General"
                0 -> "Profile"
                1 -> "Achievements"
                else -> null
            }
        }
    }
}

//============================================================================

class CustomTabSelectedListener(private val tabLayout: TabLayout) :
    TabLayout.OnTabSelectedListener {
    // Animation duration in milliseconds
    private val animationDuration = 300L

    //============================================================================
    override fun onTabSelected(tab: TabLayout.Tab) {
        try {
            val selectedColor = ContextCompat.getColor(tabLayout.context, R.color.black)
            val textView =
                tab.customView?.findViewById<TextView>(R.id.tabLayout)

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
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }


    //============================================================================
    // WHen tab is unselected
    override fun onTabUnselected(tab: TabLayout.Tab) {
        try {
            val defaultColor = ContextCompat.getColor(tabLayout.context, R.color.black)
            val textView =
                tab.customView?.findViewById<TextView>(R.id.tabLayout)

            textView?.setTextColor(defaultColor)

            val view = tab.view
            view.let {
                val scaleXAnimator = ObjectAnimator.ofFloat(it, View.SCALE_X, 0.8f)
                scaleXAnimator.duration = animationDuration
                scaleXAnimator.interpolator = FastOutSlowInInterpolator()
                scaleXAnimator.start()

                val scaleYAnimator = ObjectAnimator.ofFloat(it, View.SCALE_Y, 0.8f)
                scaleYAnimator.duration = animationDuration
                scaleYAnimator.interpolator = FastOutSlowInInterpolator()
                scaleYAnimator.start()
            }
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    // When a tab is reselected
    override fun onTabReselected(tab: TabLayout.Tab) {
        try {
            val viewPager = tabLayout.rootView.findViewById<ViewPager>(R.id.viewPager)
            viewPager?.setCurrentItem(tab.position, true)
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }
}