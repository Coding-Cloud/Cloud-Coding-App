package com.cloudcoding.features.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloudcoding.R
import com.cloudcoding.features.follow.FollowAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {
    private val model: SearchModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewpager.adapter = SearchAdapter(this@SearchFragment)
        search_bar.doOnTextChanged{
            text,_,_,_ ->
            model.select(text.toString())
        }
        TabLayoutMediator(tabLayout, viewpager) { tab: TabLayout.Tab, i: Int ->
            when (i) {
                0 -> tab.text = view.context.getText(R.string.users)
                else -> tab.text = view.context.getText(R.string.projects)
            }
        }.attach()
    }
}

class SearchModel : ViewModel() {
    private val selected = MutableLiveData<String>()

    fun select(search: String) {
        selected.value = search
    }
    fun getSelected(): MutableLiveData<String> {
        return selected
    }
}