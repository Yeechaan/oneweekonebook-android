package com.lee.oneweekonebook.ui.add

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.database.model.*
import com.lee.oneweekonebook.databinding.FragmentAddBookBinding
import com.lee.oneweekonebook.ui.MainActivity
import com.lee.oneweekonebook.ui.PermissionResultListener
import com.lee.oneweekonebook.ui.add.viewmodel.AddBookViewModel
import com.lee.oneweekonebook.ui.add.viewmodel.AddBookViewModelFactory
import com.lee.oneweekonebook.utils.PhotoRotateAdapter
import com.orhanobut.logger.Logger
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class AddBookFragment : Fragment() {

    var binding: FragmentAddBookBinding? = null
    private val args by navArgs<AddBookFragmentArgs>()

    private var currentPhotoPath: String = ""
    private var savedPhotoPath: String = ""
    private var bookType = 0

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as MainActivity).registerPermissionResultListener(permissionResultListener)

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = AddBookViewModelFactory(bookDao)
        val addBookViewModel = ViewModelProvider(this, viewModelFactory).get(AddBookViewModel::class.java)

        binding = FragmentAddBookBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = this@AddBookFragment

                imageViewCover.setOnClickListener {
                    val popupMenu = PopupMenu(requireContext(), it)
                    setPopupImageSelection(popupMenu)
                }

                buttonDone.setOnClickListener {
                    val title = editTextTitle.text.toString()
                    val writer = editTextWriter.text.toString()
                    val publisher = editTextPublisher.text.toString()

                    if (args.bookId.isNullOrEmpty()) {
                        addBookViewModel.saveBook(Book(title = title, writer = writer, publisher = publisher, coverImage = savedPhotoPath, type = bookType))
                    } else {
                        addBookViewModel.updateBook(Book(id = args.bookId!!.toInt(), title = title, writer = writer, publisher = publisher, coverImage = savedPhotoPath, type = bookType))
                    }

                    findNavController().navigateUp()
                }

                // 수정
                args.bookId?.let {
                    addBookViewModel.getBook(it.toInt())
                }

                addBookViewModel.book.observe(viewLifecycleOwner, {
                    editTextTitle.setText(it.title)
                    editTextWriter.setText(it.writer)
                    editTextPublisher.setText(it.publisher)
                    savedPhotoPath = it.coverImage
                })

                radioGroup.setOnCheckedChangeListener { _, id ->
                    bookType = when (id) {
                        R.id.radio_wish -> BOOK_TYPE_WISH
                        R.id.radio_reading -> BOOK_TYPE_READING
                        R.id.radio_done -> BOOK_TYPE_DONE
                        else -> BOOK_TYPE_UNKNOWN
                    }

                }
            }

        return binding?.root
    }

    private fun setPopupImageSelection(popupMenu: PopupMenu) {
        popupMenu.menuInflater.inflate(R.menu.option_image, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->

            when (item.itemId) {
                R.id.m1 -> {
                    (activity as MainActivity).requirePermission()

                    Toast.makeText(requireContext(), getString(R.string.take_picture), Toast.LENGTH_SHORT).show()
                }
                R.id.m2 -> {
                    // 갤러리에서 가져오기
                    permissionGalleryLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))

                    Toast.makeText(requireContext(), getString(R.string.picture_from_gallery), Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun deleteImageFromSandbox() {
        File(currentPhotoPath).delete()
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = requireContext().filesDir

        return File.createTempFile(
            "${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Logger.d("photoFile error")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.lee.oneweekonebook.fileprovider",
                        it
                    )

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                    permissionCameraLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    private fun saveMediaToStorage(bitmap: Bitmap): String {
        var savedFilePath = ""
        val filename = "${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}.jpg"
        var fileOutputStream: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            requireContext().contentResolver?.also { resolver ->

                //Content resolver will process the contentValues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                savedFilePath = imageUri.toString()

                //Opening an outputStream with the Uri that we got
                fileOutputStream = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

            val image = File(imagesDir, filename)
            savedFilePath = image.path

            fileOutputStream = FileOutputStream(image)
        }

        fileOutputStream?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }

        return savedFilePath
    }

    private val permissionResultListener = object : PermissionResultListener {
        override fun onGranted() {
            (activity as MainActivity).unregisterPermissionResultListener()
            dispatchTakePictureIntent()
        }

        override fun onDenied(showAgain: Boolean) {
            Toast.makeText(requireContext(), getString(R.string.permission_denied_camera), Toast.LENGTH_LONG).show()
        }
    }

    private var permissionCameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                // 원본 사진 저장 순서 : 앱 내부 사진 저장 -> Gallery 복사(uri 저장) -> 앱 내부 사진 삭제
                val rotatedImageBitmap = PhotoRotateAdapter.getRotatedImageBitmap(File(currentPhotoPath), requireContext())

                // copy photo from Internal Storage to Gallery
                savedPhotoPath = saveMediaToStorage(rotatedImageBitmap)
                deleteImageFromSandbox()

                val imageUri = Uri.parse(savedPhotoPath)
                binding?.imageViewCover?.setImageURI(imageUri)
            }
        }
    }

    private var permissionGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                savedPhotoPath = result.data?.data.toString()
                val imageUri = Uri.parse(savedPhotoPath)
                binding?.imageViewCover?.setImageURI(imageUri)
            }
        }
    }

}