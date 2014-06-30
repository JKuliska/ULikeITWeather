ULikeITWeather
=======

Android application dealing with current weather and weather forecast.


Features
========

- obtains current position of a user
- based on the position, the weather for a given area is downloaded in Json format
- data is parsed and an additional picture of a weather state is shown
- has options to change between metric and imperial units


Installation
============

The built signed app e.g. *ULikeITWeather.apk* can be installed from command line:
```
adb install ULikeITWeather.apk
```
This will install the apk in the internal memory of current opened emulator.
To install it on SD-card, use:
```
adb install -s ULikeITWeather.apk
```
Usage
=====

use when you need to know a detailed information about weather at your current position. 
Compatible with Android API10 and higher.


Changelog
=========

0.5.0.0 - first version of the app (not working on all devices)

0.6.0.1 - from the initial version up to this version the overall structure was changed to make the app more reliable and compatible


Building project
================

This chapter describes how to build APK with Gradle and prepare app for publishing.

You don't need to install Gradle on your system, because there is a [Gradle Wrapper](http://www.gradle.org/docs/current/userguide/gradle_wrapper.html). The wrapper is a batch script on Windows, and a shell script for other operating systems. When you start a Gradle build via the wrapper, Gradle will be automatically downloaded and used to run the build.

1. Clone this repository
2. Open configuration file _/app/src/main/java/com/example/ulikeitweather/app/ULikeITWeatherConfig.java_ and set constants as required (see below for more info)
3. Open main build script _/app/build.gradle_ and set constants as required (see below for more info)
4. Run `gradlew assemble` in console
5. APK should be available in /app/build/apk directory

**Note:** You will also need a "local.properties" file to set the location of the SDK in the same way that the existing SDK requires, using the "sdk.dir" property. Example of "local.properties" on Windows: `sdk.dir=C:\\adt-bundle-windows\\sdk`. Alternatively, you can set an environment variable called "ANDROID\_HOME".

**Tip:** Command `gradlew assemble` builds both - debug and release APK. You can use `gradlew assembleDebug` to build debug APK. You can use `gradlew assembleRelease` to build release APK. Debug APK is signed by debug keystore. Release APK is signed by own keystore, stored in _/extras/keystore_ directory.

**Signing process:** Keystore passwords are automatically loaded from property file during building the release APK. Path to this file is defined in "keystore.properties" property in "gradle.properties" file. If this property or the file does not exist, user is asked for passwords explicitly.


ULikeITWeatherConfig.java
------------------

This is the main configuration file and there are some important constants: addresses to API endpoints, API keys to 3rd party services etc. There are also some true/false switches. It is very important to correctly set these switches before building the APK.

* DEV\_API - true for development API endpoint
* LOGS - true for showing logs

**Important:** Following configuration should be used for release APK, intended for publishing on Google Play:

```java
public static final boolean DEV_API = false;
public static final boolean LOGS = false;
``` 


build.gradle
------------

This is the main build script and there are 4 important constants for defining version code and version name.

* VERSION\_MAJOR
* VERSION\_MINOR
* VERSION\_PATCH
* VERSION\_BUILD

See [Versioning Your Applications](http://developer.android.com/tools/publishing/versioning.html#appversioning) in Android documentation for more info.


Dependencies
============

* [Android Support Library v4](http://developer.android.com/tools/extras/support-library.html)
* [AppCompat](https://developer.android.com/reference/android/support/v7/appcompat/package-summary.html)
* [Jackson](http://jackson.codehaus.org/)
* [Universal Image Loader](https://github.com/nostra13/Android-Universal-Image-Loader)


Testing
=======

* Test overdraws
* Test offline/empty/progress states
* Test slow internet connection
* Test reboot (boot receivers, alarms, services)
* Monkey test (fast clicking, changing orientation)


Publishing
==========

* Check true/false switches in the main configuration file
* Set proper versions in the main build script
* Update text info in changelog/about/help
* Add analytics events for new features
* Set Android key hash on developers.facebook.com


Developed by
============

* [Juraj Kuliška]


License
=======

    Copyright 2012 Juraj Kuliška

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.