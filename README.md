# webp-java-sample
How to use Google WebP Image format in Java with spring

# Description
[Java Image I/O](http://docs.oracle.com/javase/7/docs/api/javax/imageio/package-summary.html) reader and writer for the
[Google WebP](https://developers.google.com/speed/webp/) image format and [Webp-imageIo Java Library](https://github.com/lonnyj/webp-imageio) library for java.

# License
webp-java-sample is distributed under the [Apache Software License](https://www.apache.org/licenses/LICENSE-2.0) version 2.0.

# Usage
- install JDK 1.8
- checkout this application on your computer
- Ensure libwebp-imageio.so (for linux) or libwebp-imageio dylib (for osx) (in the /webapp/WEB-INF/lib/) is accessible on the Java native library path (java.library.path system property)
- bootrun and check your application log

# Building
The build should work with either Gradle and Spring boot but There is no test case but I have prepared a [Demo Page](http://webp.leekyoungil.com/).
