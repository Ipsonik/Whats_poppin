package com.example.whatspopin.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.whatspopin.FunkoApplication
import com.example.whatspopin.R
import com.example.whatspopin.adapters.FunkoListAdapter
import com.example.whatspopin.adapters.PopListener
import com.example.whatspopin.databinding.FragmentHomeBinding
import com.example.whatspopin.viewmodels.FunkoViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: FunkoViewModel by activityViewModels {
        FunkoViewModel.FunkoViewModelFactory(
            (activity?.application as FunkoApplication).repository
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val adapter = FunkoListAdapter(PopListener { pop ->
            viewModel.onPopClicked(pop)
            findNavController()
                .navigate(R.id.action_homeFragment_to_popDetailFragment)
        })

        binding.apply {
            funkoViewModel = viewModel
            lifecycleOwner = this.lifecycleOwner
            recyclerView.adapter = adapter
            progressBar.visibility = View.VISIBLE
            searchEtLayout.visibility = View.INVISIBLE
        }

        loadData(adapter)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun loadData(adapter: FunkoListAdapter) {
        if (isOnline(requireContext())) {
            binding.swipeLayout.setOnRefreshListener {
                binding.swipeLayout.isRefreshing = false
            }
            showComponents()
            viewModel.fetchData().observe(viewLifecycleOwner) { _ ->
                binding.apply {
                    progressBar.visibility = View.GONE
                    searchEtLayout.visibility=View.VISIBLE
                }
                viewModel.isListFiltered.observe(viewLifecycleOwner) {
                    if (it) {
                        adapter.submitList(viewModel.getFilteredList(viewModel.filterText.value.toString()))
                    } else
                        adapter.submitList(viewModel.pops.value)
                }
            }
        } else {
            binding.apply {
                hideComponents()
                swipeLayout.setOnRefreshListener {
                    loadData(adapter)
                    swipeLayout.isRefreshing = false
                }
            }
        }
    }

    private fun showComponents() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
            searchEtLayout.visibility = View.VISIBLE
            netTv.visibility = View.GONE
        }
    }

    private fun hideComponents() {
        binding.apply {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.GONE
            searchEtLayout.visibility = View.GONE
            netTv.visibility = View.VISIBLE
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }


}