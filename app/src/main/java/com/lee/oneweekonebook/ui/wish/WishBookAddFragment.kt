package com.lee.oneweekonebook.ui.wish

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.FragmentWishBookAddBinding
import com.lee.oneweekonebook.utils.pickPhotoIntent
import com.lee.oneweekonebook.utils.takePhotoIntent
import com.orhanobut.logger.Logger
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val REQUEST_TAKE_PHOTO = 1
const val REQUEST_IMAGE_CAPTURE = 10
const val PICK_IMAGE_GALLERY = 11

class WishBookAddFragment : Fragment() {

    lateinit var binding: FragmentWishBookAddBinding
    lateinit var currentPhotoPath: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWishBookAddBinding.inflate(inflater, container, false).apply {

            buttonDone.setOnClickListener {
                // save
            }

            imageViewCover.setOnClickListener {
                val popupMenu = PopupMenu(requireContext(), it)
                setPopupImageSelection(popupMenu)
            }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            Logger.d(data?.data.toString())
//            val selectedImage = data?.extras?.get("data") as Bitmap
//            binding.imageViewCover.setImageBitmap(selectedImage)
//
            galleryAddPic()
        }

        if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK) {
            Toast.makeText(requireContext(), data?.data.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    private fun setPopupImageSelection(popupMenu: PopupMenu) {
        popupMenu.menuInflater.inflate(R.menu.option_image, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->

            when (item.itemId) {
                R.id.m1 -> {
                    // 직접찍기
                    dispatchTakePictureIntent()
//                    startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE)
                    Toast.makeText(requireContext(), "직접찍기", Toast.LENGTH_SHORT).show()
                }
                R.id.m2 -> {
                    // 갤러리에서 가져오기
                    startActivityForResult(pickPhotoIntent, PICK_IMAGE_GALLERY)
                    Toast.makeText(requireContext(), "갤러리에서 가져오기", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            Logger.d(takePictureIntent.resolveActivity(requireContext().packageManager))
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity?.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
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
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            activity?.sendBroadcast(mediaScanIntent)
        }
    }

}