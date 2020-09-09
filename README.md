OpenTimelineIO-Java-bindings
=======
[![OpenTimelineIO](images/OpenTimelineIO@3xDark.png)](http://opentimeline.io)
==============

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

You can add OpenTimelineIO as a dependency to your gradle project as:
```
<insert gradle dep>
```

You can add OpenTimelineIO as a dependency to your maven project as:
```
<insert maven dep>
```


Building OpenTimelineIO-Java-Bindings
------------------------

OpenTimelineIO-Java-Bindings have been built and tested on Ubuntu 18.04LTS, Windows 10 and macOS using [Gradle](https://gradle.org/install/) and [CMake](https://cmake.org/download/).
After installing Gradle and CMake follw these steps:

```console
git clone --recurse-submodules https://github.com/OpenTimelineIO/OpenTimelineIO-Java-Bindings

cd otio-java

gradle build # this builds and runs all tests
```

You can find the generated jar file in the `build/libs` directory.

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
OpenTimelineIO is open source software. Please see the [LICENSE.txt](LICENSE.txt) for details.

Nothing in the license file or this project grants any right to use Pixar or any other contributor’s trade names, trademarks, service marks, or product names.

Contact
-------

For more information, please visit http://opentimeline.io/
or https://github.com/PixarAnimationStudios/OpenTimelineIO
or join our discussion forum: https://lists.aswf.io/g/otio-discussion
