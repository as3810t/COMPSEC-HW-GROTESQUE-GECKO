package com.example.grotesquegecko.ui.addnewcaff

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.example.grotesquegecko.R
import kotlinx.android.synthetic.main.fragment_add_new_caff.*
import kotlinx.android.synthetic.main.fragment_add_new_comment.*
import timber.log.Timber

class AddNewCaffFragment : RainbowCakeFragment<AddNewCaffViewState, AddNewCaffViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_add_new_caff

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
                ActivityCompat.requestPermissions(requireActivity(),
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        101)
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

            viewModel.createCaff(filePath, title, tags)
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
        val chooseFileIntent = Intent().setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(chooseFileIntent, "Select a CAFF file"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data
            addNewCaffFilePath.setText(selectedFile?.path)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Timber.i("Permission has been denied by user")
                } else {
                    Timber.i("Permission has been granted by user")
                }
            }
        }
    }

}
