package me.gurpreetsk.camera_poc

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_view_photo.*
import java.io.File

class ViewPhotoActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view_photo)

    var names = ""
    getList().forEach {
      names += it.name + "\n"
    }
    listOfImages.text = names
  }

  private fun getList(): Array<File> {
    val root = Environment.getExternalStorageDirectory().absolutePath + "/camera-poc"
    try {
      return File(root).listFiles()
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return arrayOf()
  }
}
