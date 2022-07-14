package com.cloudcoding.features.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloudcoding.R
import com.cloudcoding.features.search.SearchModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.follow_fragment.*

class FollowFragment : Fragment() {
    private val model: FollowModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.follow_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        model.select(requireArguments().getString("userId")!!)
        viewpager.adapter = FollowAdapter(this@FollowFragment)
        TabLayoutMediator(tabLayout, viewpager) { tab: TabLayout.Tab, i: Int ->
            when (i) {
                0 -> tab.text = view.context.getText(R.string.followers)
                else -> tab.text = view.context.getText(R.string.followings)
            }
        }.attach()
    }
}
class FollowModel : ViewModel() {
    private val selected = MutableLiveData<String>()

    fun select(search: String) {
        selected.value = search
    }
    fun getSelected(): MutableLiveData<String> {
        return selected
    }
}