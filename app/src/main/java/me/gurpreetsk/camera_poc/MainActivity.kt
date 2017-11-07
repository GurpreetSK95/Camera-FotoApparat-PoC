package me.gurpreetsk.camera_poc

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.fotoapparat.Fotoapparat
import io.fotoapparat.log.Loggers.logcat
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.parameter.selector.FlashSelectors.*
import io.fotoapparat.parameter.selector.FocusModeSelectors.*
import io.fotoapparat.parameter.selector.LensPositionSelectors.back
import io.fotoapparat.parameter.selector.Selectors.firstAvailable
import io.fotoapparat.parameter.selector.SizeSelectors.biggestSize
import io.fotoapparat.result.PhotoResult
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

  lateinit var fotoapparat: Fotoapparat

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    fotoapparat = setupCamera()

    buttonTakePhoto.setOnClickListener({ takeImage() })
    buttonFlash.setOnClickListener({ toggleFlash() })
  }

  override fun onStart() {
    super.onStart()
    fotoapparat.start()
  }

  override fun onStop() {
    super.onStop()
    fotoapparat.stop()
  }

  private fun takeImage() {
    val clickedImage: PhotoResult = fotoapparat.takePicture()

    doAsync {
      val root = Environment.getExternalStorageDirectory().absolutePath + "/camera-poc"
      val directory = File(root)
      directory.mkdirs()
      val imageName = "image" + Random().nextInt(100000) + ".png"
      val location = File(directory, imageName)
      try {
        clickedImage.saveToFile(location)
        Log.i("MainActivity", "Saved: " + location)
      } catch (e: Exception) {
        e.printStackTrace()
      }
      uiThread {
        startActivity(Intent(this@MainActivity, ViewPhotoActivity::class.java))
      }
    }
  }

  private fun toggleFlash() {
//    fotoapparat.updateParameters(
//        UpdateRequest.builder()
//            .flash(if (FlashSelectors.off()) torch() else off())
//            .build())
  }

  private fun setupCamera(): Fotoapparat =
      Fotoapparat.with(MainActivity@ this)
          .into(cameraView)
          .previewScaleType(ScaleType.CENTER_CROP)
          .photoSize(biggestSize())
          .lensPosition(back())
          .focusMode(
              firstAvailable(         // (optional) use the first focus mode which is supported by device
                  continuousFocus(),
                  autoFocus(),        // in case if continuous focus is not available on device, auto focus will be used
                  fixed()             // if even auto focus is not available - fixed focus mode will be used
              )
          )
          .flash(
              firstAvailable(      // (optional) similar to how it is done for focus mode, this time for flash
                  autoRedEye(),
                  autoFlash(),
                  torch()
              )
          )
          .logger(logcat())
          .build()

}
