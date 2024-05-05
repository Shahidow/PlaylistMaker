package com.my.playlistmaker.presentation.library.playlist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.my.playlistmaker.databinding.FragmentNewPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : Fragment() {

    private lateinit var binding: FragmentNewPlaylistBinding
    private val vm by viewModel<NewPlaylistViewModel>()
    private var playlistImageUri: Uri? = null
    private var playlistName: String = ""
    private var playlistDescription: String = ""
    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { _, _ -> }
            .setNegativeButton("Завершить") { _, _-> findNavController().navigateUp() }

        val imageCover = binding.editPlaylistCover
        val nameEditText = binding.editPlaylistName
        val descriptionEditText = binding.editPlaylistDescription
        val createPlaylistButton = binding.createPlaylistButton
        createPlaylistButton.isEnabled = false

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                uri?.let {
                    imageCover.setImageURI(uri)
                    imageCover.scaleType = ImageView.ScaleType.CENTER_CROP
                    playlistImageUri = uri
                }
            }

        binding.backFromNewPlaylist.setOnClickListener {
            checkDialog()
        }

        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                createPlaylistButton.isEnabled = !s.isNullOrEmpty()
                playlistName = s.toString()
            }
        })

        descriptionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                playlistDescription = s.toString()
            }
        })

        imageCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        createPlaylistButton.setOnClickListener{
            vm.createPlaylist(playlistImageUri, playlistName, playlistDescription)
            playlistImageUri?.let { uri -> saveImageToPrivateStorage(uri) }
            Toast.makeText(requireContext(), "Плейлист $playlistName создан", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PlaylistMaker")
        if (!filePath.exists()) filePath.mkdirs()
        val file = File(filePath, playlistName)
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun checkDialog(){
        if(playlistImageUri == null && playlistName == "" && playlistDescription == "") {
            findNavController().navigateUp()
        } else {
            confirmDialog.show()
        }
    }
}