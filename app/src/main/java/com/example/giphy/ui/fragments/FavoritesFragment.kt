package com.example.giphy.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giphy.databinding.FragmentFavoritesBinding
import com.example.giphy.model.Gif
import com.example.giphy.ui.adapters.GifFavoritesAdapter
import com.example.giphy.utils.Const
import com.example.giphy.utils.States
import com.example.giphy.viewmodels.GifViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val gifViewModel: GifViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
        initListeners()
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

    private fun initListeners() {
        initSearchFavoritesGifListener()
    }

    private fun initGifAdapter() {
        binding.fragmentFavoritesGifVerticalRv.apply {
            layoutManager = LinearLayoutManager(context)

            val onGifHolderClick: (Gif) -> Unit = {
                findNavController().navigate(
                    FavoritesFragmentDirections.navgraphActionFragmentFavoritesToFragmentSingleGif(it)
                )
            }

            val onFavoritesClick: (Gif) -> Unit = {
                gifViewModel.onDeleteFavoriteClick(it)
            }

            adapter = GifFavoritesAdapter(onGifHolderClick, onFavoritesClick)
        }
    }

    private fun initObservers() {
        initGifFavoritesObserver()
        initStateObserver()
    }

    private fun initGifFavoritesObserver() {
        gifViewModel.gifFavoritesListEvent.observe(viewLifecycleOwner) { gifList ->
            (binding.fragmentFavoritesGifVerticalRv.adapter as GifFavoritesAdapter).submitList(gifList)
        }
    }

    private fun initStateObserver() {
        gifViewModel.state.observe(viewLifecycleOwner) { state ->
            binding.apply {
                when(state) {
                    States.Idle -> fragmentFavoritesPg.visibility = View.GONE
                    States.Deleted -> { }
                    States.Loading -> fragmentFavoritesPg.visibility = View.VISIBLE
                    else -> fragmentFavoritesPg.visibility = View.GONE
                }
            }
        }
    }

    private fun initSearchFavoritesGifListener() {
        var job: Job? = null
        binding.fragmentFavoritesSearchEt.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(Const.SEARCH_GIF_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        gifViewModel.getFavoriteGifsBySearch(editable.toString())
                    } else {
                        gifViewModel.getAllFavorites()
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "FavoritesFragmentTAG"
    }
}