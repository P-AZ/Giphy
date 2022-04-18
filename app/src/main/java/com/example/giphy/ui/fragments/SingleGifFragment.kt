package com.example.giphy.ui.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.giphy.R
import com.example.giphy.databinding.FragmentSingleGifBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleGifFragment : DialogFragment() {

    private var _binding: FragmentSingleGifBinding? = null
    private val binding get() = _binding!!

    private val args: SingleGifFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleGifBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValues()
        initClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initClickListener() {
        initIconCloseListener()
        initGifListener()
    }

    private fun initGifListener() {
        binding.fragmentSingleGifImg.setOnClickListener {
            val gifUri = Uri.parse(args.gifArg?.images?.original?.url)
            val contentType = "image/jpeg"

            val shareGifIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, gifUri)
                type = contentType
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            try {
                startActivity(shareGifIntent)
            } catch (e: ActivityNotFoundException) {
                Log.e(TAG, e.toString())
                // Define later what app should do if no activity can handle the intent.
            }
        }
    }

    private fun initIconCloseListener() {
        binding.fragmentSingleGifCloseImg.setOnClickListener {
            dismiss()
        }
    }

    private fun initValues() {

        Glide.with(this)
            .asGif()
            .load(args.gifArg?.images?.original?.url)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(binding.fragmentSingleGifImg)
    }

    companion object {
        private const val TAG = "SingleGifFragmentTAG"
    }
}