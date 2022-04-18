package com.example.giphy.ui

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.giphy.R
import com.example.giphy.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonCancellable.start

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.activity_main_fragment_container_navhost) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activityMainBottomNavigation.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()
        val song = R.raw.background_song
        mediaPlayer = MediaPlayer.create(this, song)
        mediaPlayer?.apply {
            setVolume(100f, 100f)
            isLooping = true
            start()
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.also { mediaPlayer ->
            if(mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
        }
    }
}