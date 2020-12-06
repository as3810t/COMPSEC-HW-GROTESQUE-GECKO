package com.example.grotesquegecko.ui.addnewcaff

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.example.grotesquegecko.R
import com.example.grotesquegecko.util.getFileName
import kotlinx.android.synthetic.main.fragment_add_new_caff.*
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AddNewCaffFragment : RainbowCakeFragment<AddNewCaffViewState, AddNewCaffViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_add_new_caff

    var selectedFile: Uri? = null
    val PERMISSION_REQUEST_CODE = 101
    val SELECT_FILE_REQUEST_CODE = 111

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
    }

    private fun setupButtons() {
        addNewCaffBackButton.setOnClickListener {
            findNavController().navigate(
                AddNewCaffFragmentDirections.actionNavAddNewCaffToNavCaffs()
            )
        }

        addNewCaffBrowseButton.setOnClickListener {
            val permission = ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
            if (permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
                return@setOnClickListener
            }
            doBrowseFile()
        }

        addNewCaffUploadButton.setOnClickListener {
            val filePath: String = addNewCaffFilePath.text.toString().trim()
            val title: String = addNewCaffTitleEdit.text.toString().trim()
            val tags: String = addNewCaffTagsEdit.text.toString().trim()
            if (filePath.isEmpty()) {
                addNewCaffFilePath.error =
                        getString(R.string.add_new_caff_choose_caff_file)
                addNewCaffFilePath.requestFocus()
                return@setOnClickListener
            }
            if (title.isEmpty()) {
                addNewCaffTitleEdit.error =
                    getString(R.string.add_new_caff_write_title)
                addNewCaffTitleEdit.requestFocus()
                return@setOnClickListener
            }

            val parcelFileDescriptor =
                context?.contentResolver?.openFileDescriptor(selectedFile!!, "r", null)

            val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
            val file =
                File(context?.cacheDir, context?.contentResolver?.getFileName(selectedFile!!)!!)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

            viewModel.createCaff(selectedFile, title, tags, file)
        }
    }

    override fun render(viewState: AddNewCaffViewState) {
        when (viewState) {
            is Caff -> caff()
            is CaffFailed -> caffFailed()
            is Loading -> loading()
            is AddNewCaffReady -> commentReady()
        }
    }

    private fun commentReady() {
        addNewCaffProgressBar.visibility = View.GONE
        textViewCaffFailed.visibility = View.GONE
        findNavController().navigate(
                AddNewCaffFragmentDirections.actionNavAddNewCaffToNavCaffs()
        )
    }

    private fun loading() {
        addNewCaffProgressBar.visibility = View.VISIBLE
    }

    private fun caffFailed() {
        addNewCaffProgressBar.visibility = View.GONE
        textViewCaffFailed.visibility = View.VISIBLE
    }

    private fun caff() {
        addNewCaffProgressBar.visibility = View.GONE
        textViewCaffFailed.visibility = View.GONE
    }

    private fun doBrowseFile() {
        val chooseFileIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/octet-stream"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(
            Intent.createChooser(chooseFileIntent, "Select a CAFF file"),
            SELECT_FILE_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            selectedFile = data?.data
            addNewCaffFilePath.setText(selectedFile?.path)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Timber.i("Permission has been denied by user")
                } else {
                    Timber.i("Permission has been granted by user")
                }
            }
        }
    }

}
