package com.example.giphy.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giphy.databinding.FragmentHomeBinding
import com.example.giphy.model.Gif
import com.example.giphy.ui.adapters.GifAdapter
import com.example.giphy.utils.Const.SEARCH_GIF_TIME_DELAY
import com.example.giphy.utils.States
import com.example.giphy.viewmodels.GifViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val gifViewModel: GifViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
        initListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi() {
        initAdapters()
    }

    private fun initAdapters() {
        initGifAdapter()
    }

    private fun initListener() {
        initSearchRegularGifListener()
    }

    private fun initGifAdapter() {
        binding.fragmentHomeGifVerticalRv.apply {
            layoutManager = LinearLayoutManager(context)

            val onGifHolderClick: (Gif) -> Unit = {
                findNavController().navigate(
                    HomeFragmentDirections.navgraphActionFragmentHomeToFragmentSingleGif(it)
                )
            }
            val onFavoritesClick: (Gif) -> Unit = {
                gifViewModel.onFavoriteClick(it)
            }

            adapter = GifAdapter(onGifHolderClick, onFavoritesClick)
        }
    }

    private fun initObservers() {
        initStatesObserver()
        initPagingAdapterStatesObserver()
        initGifRegularListObserver()
    }

    private fun initGifRegularListObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            gifViewModel.gifPagingRegularListEvent.collectLatest { pagingData ->
                (binding.fragmentHomeGifVerticalRv.adapter as GifAdapter).submitData(pagingData)
            }
        }
    }

    private fun initStatesObserver() {
        gifViewModel.state.observe(viewLifecycleOwner) { state ->
            binding.apply {
                when(state) {
                    States.Idle -> fragmentHomePb.visibility = View.GONE
                    States.Deleted -> { }
                    States.Loading -> fragmentHomePb.visibility = View.VISIBLE
                    else -> fragmentHomePb.visibility = View.GONE
                }
            }
        }
    }

    private fun initSearchRegularGifListener() {
        var job: Job? = null
        binding.fragmentHomeSearchEt.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_GIF_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        gifViewModel.updateQueryPaging(editable.toString())
//                        gifViewModel.getGifsBySearch(editable.toString())
                    } else {
                        gifViewModel.updateQueryPaging("null")
//                        gifViewModel.getGifsBySearch()
                    }
                }
            }
        }
    }

    private fun initPagingAdapterStatesObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            (binding.fragmentHomeGifVerticalRv.adapter as GifAdapter).loadStateFlow.collectLatest { loadStates ->
                binding.fragmentHomePb.visibility =
                    if(loadStates.refresh is LoadState.Loading) View.VISIBLE
                    else View.GONE
            }
        }
    }

    companion object {
        private const val TAG = "HomeFragmentTAG"
    }
}