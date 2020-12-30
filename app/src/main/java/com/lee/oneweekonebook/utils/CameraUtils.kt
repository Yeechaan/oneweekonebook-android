package com.lee.oneweekonebook.utils

import android.content.Intent
import android.provider.MediaStore

val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
