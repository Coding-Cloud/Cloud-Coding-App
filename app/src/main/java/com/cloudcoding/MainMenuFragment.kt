package com.cloudcoding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.main_menu_nav_host.*

class MainMenuFragment : Fragment() {
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_menu_nav_host, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mDrawerToggle = ActionBarDrawerToggle(
            this.activity,
            drawer_layout,
            toolbar as Toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        val x = nav.getHeaderView(0).findViewById<ImageView>(R.id.profile_picture)
        Glide.with(x)
            .load("https://interactive-examples.mdn.mozilla.net/media/cc0-images/grapefruit-slice-332-332.jpg")
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.drawable.ic_user)
            .into(x)
        drawer_layout.addDrawerListener(mDrawerToggle)
        val navHost =
            childFragmentManager.findFragmentById(R.id.main_menu_nav_host) as NavHostFragment
        nav.setupWithNavController(navHost.navController)
        NavigationUI.setupWithNavController(main_menu_bottom_nav, navHost.navController)
        mDrawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (mDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}