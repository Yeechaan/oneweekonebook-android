package com.lee.oneweekonebook.ui.wish

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.lee.oneweekonebook.databinding.FragmentWishBookAddBinding

class WishBookAddFragment : Fragment() {

    lateinit var binding: FragmentWishBookAddBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWishBookAddBinding.inflate(inflater, container, false).apply {

            buttonDone.setOnClickListener {
                // save
            }

            imageViewCover.setOnClickListener {
                // 직접 or 갤러리에서 선택
                getCoverImage(10)
            }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 10) {
            val selectedImage = data?.extras?.get("data") as Bitmap
            binding.imageViewCover.setImageBitmap(selectedImage)
        }

        if (requestCode == 11) {
            Toast.makeText(requireContext(), data?.data.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    private fun getCoverImage(status: Int) {
        when(status) {
            10 -> {
                val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePhotoIntent, 10)
            }
            11 -> {
                val getPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(getPhotoIntent, 11)
            }
        }
    }
}