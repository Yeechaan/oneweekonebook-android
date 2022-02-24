package com.lee.oneweekonebook.utils

import com.orhanobut.logger.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val LOGGER_DIRECTORY_NAME = "logger"
const val LOGGER_TEXT_EXTENSION = ".txt"
const val LOGGER_ZIP_EXTENSION = ".zip"
const val LOGGER_MAX_FILE_SIZE = 500 * 1024
const val LOGGER_MAX_FILE_COUNT = 5

class CustomDiskLogAdapter(
    folder: File,
    maxFileSize: Int = LOGGER_MAX_FILE_SIZE,
    maxFileCount: Int = LOGGER_MAX_FILE_COUNT
) : LogAdapter {

    private val formatStrategy: FormatStrategy

    init {
        val logStrategy = MydDiskLogStrategy(folder, maxFileSize, maxFileCount)

        formatStrategy = CsvFormatStrategy.newBuilder()
            .logStrategy(logStrategy)
            .tag(BuildConfig.APPLICATION_ID)
            .build()
    }

    override fun isLoggable(priority: Int, tag: String?): Boolean {
        return true
    }

    override fun log(priority: Int, tag: String?, message: String) {
        formatStrategy.log(priority, tag, message)
    }
}


class MydDiskLogStrategy(
    private val folder: File,
    private val maxFileSize: Int,
    private val maxFileCount: Int = 0
) : LogStrategy {

    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private var _fileWriter: FileWriter? = null
    private var logFile: File? = null
    private var fileIndex = 0

    override fun log(level: Int, tag: String?, message: String) {
        // log level 이 INFO 이상이면 파일에 저장 (INFO, WARN, ERROR, ASSERT)
        if (level >= Logger.INFO) {
            scope.launch {
                saveLog(message)
            }
        }
    }


    private fun saveLog(message: String) {
        var fileWriter: FileWriter? = null

        try {
            fileWriter = getFileWriter()
            fileWriter.append(message)
            fileWriter.flush()
//            fileWriter.close()
        } catch (e: IOException) {
            if (fileWriter != null) {
                try {
                    fileWriter.flush()
                    fileWriter.close()
                } catch (e: IOException) {
                    e.message
                }
                _fileWriter = null
            }
        }
    }

    private fun getFileWriter(): FileWriter {
        if (_fileWriter != null && logFile != null) {
            if (logFile!!.length() < maxFileSize) {
                return _fileWriter!!
            } else {
                deleteOldFile(folder)
            }
        }

        try {
            _fileWriter?.close()
        } catch (e: Exception) {
            e.message
        }

        val logFile = getNextLogFile(folder)
        _fileWriter = FileWriter(logFile, true)

        return _fileWriter!!
    }


    private fun getNextLogFile(folder: File): File {
        if (!folder.exists())
            folder.mkdirs()

        deleteDifferentDateFile(folder)

        logFile = File(
            folder,
            "${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())}_$fileIndex$LOGGER_TEXT_EXTENSION"
        )
        fileIndex++

        return logFile!!
    }

    private fun deleteDifferentDateFile(folder: File) {
        val subFiles = folder.listFiles()

        if (!subFiles.isNullOrEmpty()) {
            subFiles.sortBy { it.name }
            val lastFileName = subFiles.last().name.split("_")

            if (lastFileName[0] != SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) && subFiles.count() >= maxFileCount) {
                subFiles[0].delete()
            }
        }
    }

    private fun deleteOldFile(folder: File) {
        val subFiles = folder.listFiles()

        if (!subFiles.isNullOrEmpty()) {
            subFiles.sortBy { it.name }
            if (subFiles.count() >= maxFileCount) {
                for (i in 0..(subFiles.count() - maxFileCount)) {
                    subFiles[i].delete()
                }
            }
        }
    }
}