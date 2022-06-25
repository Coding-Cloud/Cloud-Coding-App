package com.cloudcoding

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.api.request.GetFollowersRequest
import kotlinx.android.synthetic.main.main_menu_nav_host.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
        val spanString = SpannableString(nav.menu.findItem(R.id.nav_item_disconnect).title)
        spanString.setSpan(
            ForegroundColorSpan(Color.RED),
            0,
            spanString.length,
            0
        )
        nav.menu.findItem(R.id.nav_item_disconnect).title = spanString

        nav.menu.findItem(R.id.nav_item_disconnect).setOnMenuItemClickListener {
            val sharedPref = context?.getSharedPreferences(
                getString(R.string.token),
                Context.MODE_PRIVATE
            )!!
            val sharedPrefMe = context?.getSharedPreferences(
                getString(R.string.me),
                Context.MODE_PRIVATE
            )!!
            with(sharedPrefMe.edit()) {
                clear()
                commit()
            }
            with(sharedPref.edit()) {
                clear()
                commit()
            }
            findNavController()
                .navigate(R.id.action_to_loginFragment)
            true
        }
        mDrawerToggle = ActionBarDrawerToggle(
            this.activity,
            drawer_layout,
            toolbar as Toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        GlobalScope.launch(Dispatchers.Default) {
            val user = CloudCodingNetworkManager.getMe()
            val followerRequest = GetFollowersRequest(user.id, null, null)
            val followers = CloudCodingNetworkManager.getFollowers(followerRequest)
            val followings = CloudCodingNetworkManager.getFollowings(followerRequest)
            withContext(Dispatchers.Main) {
                nav.getHeaderView(0).findViewById<TextView>(R.id.followers_count).text =
                    followers.totalResults.toString()
                nav.getHeaderView(0).findViewById<TextView>(R.id.followings_count).text =
                    followings.totalResults.toString()
                nav.getHeaderView(0).findViewById<TextView>(R.id.name).text =
                    getString(R.string.user_name, user.firstname, user.lastname)
                nav.getHeaderView(0).findViewById<TextView>(R.id.username).text =
                    getString(R.string.username, user.username)
            }
        }
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