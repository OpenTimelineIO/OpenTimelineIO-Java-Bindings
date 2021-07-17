OpenTimelineIO-Java-bindings
=======
[![OpenTimelineIO](images/opentimelineio-color.svg)](http://opentimeline.io)
==============

[![Supported VFX Platform Versions](https://img.shields.io/badge/vfx%20platform-2016--2020-lightgrey.svg)](http://www.vfxplatform.com/)

Main web site: http://opentimeline.io/

Documentation: 

Discussion group: https://lists.aswf.io/g/otio-discussion

Slack channel: https://academysoftwarefdn.slack.com/messages/CMQ9J4BQC
To join, create an account here first: https://slack.aswf.io/

PUBLIC BETA NOTICE
------------------

OpenTimelineIO is currently in Public Beta. That means that it may be missing
some essential features and there are large changes planned. During this phase
we actively encourage you to provide feedback, requests, comments, and/or
contributions.

Overview
--------

OpenTimelineIO is an interchange format and API for editorial cut information.
OTIO is not a container format for media, rather it contains information about
the order and length of cuts and references to external media. This repository 
contains the Java bindings built over the C++ core which you can find [here](https://github.com/PixarAnimationStudios/OpenTimelineIO).

OTIO includes both a file format and an API for manipulating that format.
It also implements a dependency-less library for dealing strictly with time, `opentime`.

You can provide adapters for your video editing tool or pipeline as needed.

Quick-Start
-----------

> :warning: **This is pre-release software**: We're releasing these bindings to encourage feedback and participation, but we expect that there may be some breaking changes along the way. Please consider this if you decide to use the bindings in their current state.

You can add OpenTimelineIO as a gradle dependency to your `build.gradle` with:

```gradle
implementation 'io.opentimeline:opentimelineio:0.14.0-beta-2'
```

You can add OpenTimelineIO as a maven dependency to your pom.xml with:

```xml
<dependency>
  <groupId>io.opentimeline</groupId>
  <artifactId>opentimelineio</artifactId>
  <version>0.14.0-beta-2</version>
</dependency>
```


Building OpenTimelineIO-Java-Bindings
------------------------

OpenTimelineIO-Java-Bindings have been built and tested on Ubuntu 18.04LTS, Windows 10 and macOS using [Gradle](https://gradle.org/install/) and [CMake](https://cmake.org/download/).
After installing Gradle and CMake follw these steps:

```shell
git clone --recurse-submodules https://github.com/OpenTimelineIO/OpenTimelineIO-Java-Bindings

cd otio-java

gradle build # this builds and runs all tests
```

You can find the generated jar file in the `build/libs` directory.

Building OpenTimelineIO-Java-Bindings for Android
------------------------

This has been built and tested on Ubuntu 18.04LTS.

Install Android Studio according to these [steps](https://developer.android.com/studio).

Install NDK and CMake using [sdkmanager](https://developer.android.com/studio/command-line/sdkmanager) as follows:

`sdkmanager "ndk;22.0.7026061" "cmake;3.10.2.4988404"`

You can find `sdkmanager` in `$ANDROID_HOME/tools/bin/`. `$ANDROID_HOME` is the location of Android SDK which is generally `~/Android/Sdk`.

Set the `ANDROID_HOME` environment variable and from the root directory of the project run:

```shell
gradle clean
gradle build -x test -PandroidBuild -Psdk_path=$ANDROID_HOME
```

You'll find the JAR in `build/libs`

#### How to include the Android JAR in your project

Copy the JAR to the `libs` directory and add this to the app level `build.gradle`:

```groovy
android {
    ...

    ndkVersion '22.0.7026061'

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    ...
}

task unjar {
    ant.unzip(src: 'libs/java-opentimelineio-0.14.0.jar', dest: 'JARUnzip')

    copy {
        from 'JARUnzip/arm64-v8a'
        into 'src/main/jniLibs/arm64-v8a'
    }
    copy {
        from 'JARUnzip/x86_64'
        into 'src/main/jniLibs/x86_64'
    }
}

build.dependsOn unjar

dependencies {
    ...
    implementation fileTree(dir: "libs", include: ["*.jar"])
    implementation files('libs/java-opentimelineio-0.14.0.jar')
    ...
}

```


Architecture Support Matrix
---------------------------

| Operating System | x86_64/amd64       | x86                      | aarch64/arm64            | aarch32                  |
|------------------|--------------------|--------------------------|--------------------------|--------------------------|
| **Linux**        | :heavy_check_mark: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: |
| **Windows**      | :heavy_check_mark: | :heavy_multiplication_x: | :heavy_multiplication_x: | :heavy_multiplication_x: |
| **macOS**        | :heavy_check_mark: | :heavy_multiplication_x: | EXPERIMENTAL             | :heavy_multiplication_x: |

Apple Silicon support is experimental. The pre-built JAR includes support, but it is **untested** since we can't run CI tests on Apple Silicon.

Examples
--------

Looking through the unit tests is a great way to see what OTIO can do. 
See [here](https://github.com/OpenTimelineIO/OpenTimelineIO-Java-Bindings/tree/master/src/test/java/io/opentimeline).

Developing
----------

If you want to contribute to the project, please see: https://opentimelineio.readthedocs.io/en/latest/tutorials/contributing.html

You can get the latest development version via:

`git clone --recurse-submodules https://github.com/OpenTimelineIO/OpenTimelineIO-Java-Bindings.git`

License
-------
OpenTimelineIO is open source software. Please see the [LICENSE](LICENSE) for details.

Nothing in the license file or this project grants any right to use Pixar or any other contributor’s trade names, trademarks, service marks, or product names.

Contact
-------

For more information, please visit http://opentimeline.io/
or https://github.com/PixarAnimationStudios/OpenTimelineIO
or join our discussion forum: https://lists.aswf.io/g/otio-discussion
