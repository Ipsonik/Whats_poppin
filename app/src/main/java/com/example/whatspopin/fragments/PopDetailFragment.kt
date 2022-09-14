package com.example.whatspopin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.whatspopin.FunkoApplication
import com.example.whatspopin.R
import com.example.whatspopin.databinding.FragmentHomeBinding
import com.example.whatspopin.databinding.FragmentPopDetailBinding
import com.example.whatspopin.viewmodels.FunkoViewModel
import com.squareup.picasso.Picasso


class PopDetailFragment : Fragment() {

    private lateinit var binding: FragmentPopDetailBinding

    private val viewModel: FunkoViewModel by activityViewModels {
        FunkoViewModel.FunkoViewModelFactory(
            (activity?.application as FunkoApplication).repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pop_detail, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        Picasso.get()
            .load(viewModel._pop.value?.img)
            .resize(640, 640)
            .into(binding.imageView)

        return binding.root
    }


}