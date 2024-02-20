package com.my.playlistmaker.presentation.library

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.Key.VISIBILITY
import androidx.fragment.app.Fragment
import com.my.playlistmaker.R
import com.my.playlistmaker.databinding.LibraryFragmentBinding


class NumberFragment : Fragment() {

    companion object {
        private const val NUMBER = "number"

        fun newInstance(number: Int) = NumberFragment().apply {
            arguments = Bundle().apply {
                putInt(NUMBER, number)
            }
        }
    }

    private lateinit var binding: LibraryFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = LibraryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(requireArguments().getInt(NUMBER)){
            1 -> {
                binding.libraryNoResultText.text = getString(R.string.libraryFavoritesEmpty)
                binding.libraryNewPlaylistButton.visibility = View.GONE
            }
            2 -> {
                binding.libraryNoResultText.text = getString(R.string.libraryPlayListEmpty)
                binding.libraryNewPlaylistButton.visibility = View.VISIBLE
            }
        }
    }

}