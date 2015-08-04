DEPRECATED
==========

ShaderCamera v0.1
=================

![ShaderCamera Screenshot][1]

Simple glsl shaders usage with the latest Camera2 apis in Android 5.0+.
-------------------------


Check out `SimpleCameraRenderer` for the basics on how to extend these classes to use shaders
and `MainActivity` for how to use the CameraFragment, since it encapsulates all camera functionality
and doesn't do anything with the UI itself.

Base code originally by [Jernej Virag](https://github.com/izacus/AndroidOpenGLVideoDemo) and re-mixed
with the Camera2 samples from Google.

Any questions/comments/etc - [@trippedout](http://twitter.com/trippedout)

Dependencies
------------

Need a 5.0+ device with access to [Camera2 apis](https://developer.android.com/reference/android/hardware/camera2/package-summary.html),
most of the newest devices on the market will suffice,
I don't think the emulator works in all cases, or at all. Let me know.

Also uses [Butterknife](https://github.com/trippedout/ShaderCamera) by Jake Wharton for injection
since i've been using it alot lately and it is awesome. Added through gradle so don't
worry about anything on your end.


TODO
----

* PLAYING WITH PREVIEW ONLY - need to look into more about the api to determine best way for us to
actually RECORD the cool stuff we're showing on screen, rather than just showing it. It's a toy for now.

* Handles only square perspective for now, due to silly aspect ratio business. Should be simple future
fix for handling normal full portait and landscape imagery.

* Refactor current `CameraFragment`/`SurfaceTextureListener`/`CameraRenderer` interactions. should be an easier way to continue
to keep them separate but make them more manageable with less setup required.

* Sample project with many more shader examples and maybe a way to extend shaders into their own class,
similar to how I used to do it with [GPU-Image](https://github.com/trippedout/android-gpuimage)


Issues
------

* On some devices, app running from Android Studio will crash on initial load, with segmentation fault,
but will continue to work fine on subsequent loads. Seen on Nexus 5.

* Camera tends to crash after leaving app and coming back in, secondary onResume doing something silly.



[1]: https://raw.githubusercontent.com/trippedout/ShaderCamera/master/screens/screen-1.jpg
