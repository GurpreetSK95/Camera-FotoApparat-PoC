package me.gurpreetsk.camera_poc

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

//    clickedImage.saveToFile(file)
  }

  private fun toggleFlash() {
//    fotoapparat.updateParameters(
//      UpdateRequest.builder()
//          .flash(if () torch() else off())
//          .build()
//    )
  }

  private fun setupCamera(): Fotoapparat =
      Fotoapparat.with(MainActivity@ this)
          .into(cameraView)
          .previewScaleType(ScaleType.CENTER_INSIDE)
          .photoSize(biggestSize())
          .lensPosition(back())
          .focusMode(
              firstAvailable(  // (optional) use the first focus mode which is supported by device
                  continuousFocus(),
                  autoFocus(),        // in case if continuous focus is not available on device, auto focus will be used
                  fixed()             // if even auto focus is not available - fixed focus mode will be used
              )
          )
          .flash(firstAvailable(      // (optional) similar to how it is done for focus mode, this time for flash
              autoRedEye(),
              autoFlash(),
              torch()
          ))
//        .frameProcessor(myFrameProcessor)   // (optional) receives each frame from preview stream
          .logger(logcat())
          .build()

}
