package com.example.grotesquegecko.ui.caffdetails

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.example.grotesquegecko.LoginActivity
import com.example.grotesquegecko.R
import com.example.grotesquegecko.ui.common.glideLoader
import kotlinx.android.synthetic.main.fragment_caff_details.*
import okhttp3.ResponseBody
import permissions.dispatcher.*
import timber.log.Timber
import java.io.*

@RuntimePermissions
class CaffDetailsFragment : RainbowCakeFragment<CaffDetailsViewState, CaffDetailsViewModel>(),
    CaffDetailsAdapter.Listener {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_caff_details

    private lateinit var caffId: String
    private lateinit var caffTitle: String
    private lateinit var userId: String

    private lateinit var adapter: CaffDetailsAdapter

    private var fileName = "SampleFile.caff"

    private val DOWNLOAD_DIR =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    var fileSize: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArguments()

        caffDetailsEmptyListText.text = getString(R.string.caff_details_comments_empty_list_text)

        caffDetailsBackButton.setOnClickListener {
            findNavController().navigate(
                CaffDetailsFragmentDirections.actionNavCaffDetailsToNavCaffs()
            )
        }

        caffDetailsAddNewCommentButton.setOnClickListener {
            val bundle = bundleOf("caffId" to caffId, "caffTitle" to caffTitle)
            findNavController().navigate(
                R.id.action_nav_caff_details_to_nav_add_new_comment, bundle
            )
        }

        Handler(Looper.getMainLooper()).post {
            glideLoader(
                requireContext(),
                caffDetailsCaffPreview,
                "https://gecko.stripedpossum.dev/caff/${caffId}/preview"
            )
        }

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            caffDetailsDownloadButton.isEnabled = false
        }

        caffDetailsDownloadButton.setOnClickListener {
            downloadFileWithPermissionCheck()
        }

        caffDetailsCaffTitle.text = caffTitle
    }

    private fun isExternalStorageReadOnly(): Boolean {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState
    }

    private fun isExternalStorageAvailable(): Boolean {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == extStorageState
    }

    @NeedsPermission(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun downloadFile() {
        caffDetailsDownloading.visibility = View.VISIBLE
        caffDetailsDownloading.text = getString(R.string.caff_details_download_is_in_progress)

        viewModel.downloadCaff(caffId)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnShowRationale(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun showRationaleForCamera(request: PermissionRequest) {
        showRationaleDialog(R.string.caff_details_permission_show_rational, request)
    }

    @OnPermissionDenied(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun onDownloadFileDenied() {
        Timber.d(getString(R.string.caff_details_permission_denied))
    }

    @OnNeverAskAgain(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun onDownloadFileNeverAskAgain() {
        Timber.d(getString(R.string.caff_details_permission_denied))
    }

    private fun showRationaleDialog(@StringRes messageResId: Int, request: PermissionRequest) {
        AlertDialog.Builder(requireContext())
            .setPositiveButton(R.string.login_fragment_forgotten_password_ok) { _, _ -> request.proceed() }
            .setNegativeButton(R.string.login_fragment_forgotten_password_cancel) { _, _ -> request.cancel() }
            .setCancelable(false)
            .setMessage(messageResId)
            .show()
    }

    private fun setupList() {
        adapter = CaffDetailsAdapter(requireContext(), userId)
        adapter.listener = this
        caffDetailsCommentList.adapter = adapter
        caffDetailsCommentList.emptyView = caffDetailsEmptyListText
    }

    override fun onStart() {
        super.onStart()

        viewModel.getUserId()
    }

    private fun initArguments() {
        caffId = arguments?.getString("caffId").toString()
        caffTitle = arguments?.getString("caffTitle").toString()
    }

    override fun render(viewState: CaffDetailsViewState) {
        when (viewState) {
            is Loading -> showLoading()
            is CaffDetailsReady -> showCommentList(viewState)
        }
    }

    private fun showCommentList(viewState: CaffDetailsReady) {
        setupList()
        caffDetailsProgressBar.visibility = View.GONE
        caffDetailsDownloading.visibility = View.GONE
        adapter.submitList(viewState.data)
    }

    private fun showLoading() {
        caffDetailsProgressBar.visibility = View.VISIBLE
        caffDetailsDownloading.visibility = View.GONE
    }

    override fun onCommentEdit(id: String, content: String) {
        viewModel.editComment(caffId, id, content)
    }

    override fun onCommentDelete(id: String) {
        viewModel.deleteComment(caffId, id)
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is CaffDetailsViewModel.Download -> {
                val content = event.response?.headers()?.get("Content-Disposition")
                val contentSplit = content!!.split("filename=")
                fileName = contentSplit[1].replace("filename=", "").replace("\"", "").trim()

                try {
                    if (event.response?.body() != null) {
                        writeResponseBodyToDisk(event.response?.body()!!)
                    }
                } catch (e: java.lang.Exception) {
                    Timber.d(e.message)
                }
            }
            is CaffDetailsViewModel.DownloadWasNotSuccessful -> {
                caffDetailsDownloading.visibility = View.VISIBLE
                caffDetailsDownloading.text =
                    getString(R.string.caff_details_download_was_not_successful)
            }
            is CaffDetailsViewModel.WrongToken -> {
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            is CaffDetailsViewModel.UserId -> {
                userId = event.userId
                viewModel.load(caffId)

            }
        }
    }

    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        return try {
            val downloadedCaffFile =
                File(
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName
                )
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(10000)
                fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(downloadedCaffFile)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Log.d(TAG, "file download: $fileSizeDownloaded of $fileSize")
                }
                outputStream.flush()

                caffDetailsDownloading.text =
                    getString(R.string.caff_details_download_is_successful)
                val finalUri: Uri? = context?.let { copyFileToDownloads(it, downloadedCaffFile) }
                true
            } catch (e: IOException) {
                false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            false
        }
    }
    private fun copyFileToDownloads(context: Context, downloadedFile: File): Uri? {
        val resolver = context.contentResolver
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
                put(MediaStore.MediaColumns.SIZE, fileSize)
            }
            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        } else {
            val authority = "${context.packageName}.provider"
            val destinyFile = File(DOWNLOAD_DIR, fileName)
            FileProvider.getUriForFile(context, authority, destinyFile)
        }?.also { downloadedUri ->
            resolver.openOutputStream(downloadedUri).use { outputStream ->
                val brr = ByteArray(1024)
                var len: Int
                val bufferedInputStream =
                    BufferedInputStream(FileInputStream(downloadedFile.absoluteFile))
                while ((bufferedInputStream.read(brr, 0, brr.size).also { len = it }) != -1) {
                    outputStream?.write(brr, 0, len)
                }
                outputStream?.flush()
                bufferedInputStream.close()
            }
        }
    }
}
