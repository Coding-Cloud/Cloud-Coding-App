package com.cloudcoding.features.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.api.response.ProjectsResponse
import com.cloudcoding.features.projects.ProjectAdapter
import com.cloudcoding.utils.PaginationScrollListener
import kotlinx.android.synthetic.main.projects_fragment.*
import kotlinx.coroutines.*

class SearchProjectsFragment : Fragment() {
    private val model: SearchModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.projects_fragment, parent, false)
    }

    private var jobs: MutableList<Job> = mutableListOf()

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach { job ->
            job.cancel()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var loading = true
        val projects = ProjectsResponse(mutableListOf(), 0)
        project_list.run {
            layoutManager = LinearLayoutManager(this@SearchProjectsFragment.context)
            adapter = ProjectAdapter(projects.projects, R.id.action_nav_item_search_to_projectDetailsFragment)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        model.getSelected().observe(viewLifecycleOwner) { search ->
            projects.projects.clear()
            projects.totalResults = 0
            project_list.adapter?.notifyDataSetChanged()
            project_list.addOnScrollListener(object :
                PaginationScrollListener(project_list.layoutManager as LinearLayoutManager) {
                override fun isLastPage(): Boolean {
                    return projects.totalResults == projects.projects.size
                }

                override fun isLoading(): Boolean {
                    return loading
                }

                override fun loadMoreItems() {
                    loading = true
                    jobs.add(GlobalScope.launch(Dispatchers.Default) {
                        val size = projects.projects.size
                        val projectsResponse = CloudCodingNetworkManager.getProjects(
                            search,
                            25,
                            0

                        )
                        projects.projects.addAll(projectsResponse.projects)
                        projects.totalResults = projectsResponse.totalResults
                        withContext(Dispatchers.Main) {
                            loading = false
                            project_list.adapter?.notifyItemRangeInserted(
                                size,
                                projectsResponse.projects.size
                            )
                        }
                    })
                }

                override fun isReversed(): Boolean {
                    return false
                }

                override fun getTotalPageCount(): Int {
                    return 0
                }
            })
            jobs.add(GlobalScope.launch(Dispatchers.Default) {
                val projectsResponse =
                    CloudCodingNetworkManager.getProjects(search, 25, 0)
                projects.projects.addAll(projectsResponse.projects)
                projects.totalResults = projectsResponse.totalResults
                withContext(Dispatchers.Main) {
                    project_list.adapter?.notifyItemRangeInserted(0, projects.projects.size)
                }
            })
        }

    }
}