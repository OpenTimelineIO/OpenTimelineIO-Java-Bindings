// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline;

import java.io.IOException;

import static io.opentimeline.OTIOFactory.OTIO_VERSION;

/**
 * This class uses NativeUtils to load native libraries from the JAR archive.
 * In case it is unable to load them from the JAR archive it falls back to try loading from the system.
 */
public class LibraryLoader {
    private static boolean libLoaded = false;

    private static String getPlatformName() {
        String osName = System.getProperty("os.name").toLowerCase();
        // All macOS architectures share the same binary
        if (osName.contains("mac")) {
            return "Darwin";
        }

        // determine CPU architecture name
        String architecture = "";
        if (System.getProperty("os.arch").endsWith("86")) {
            architecture = "x86";
        } else if (System.getProperty("os.arch").equals("amd64") || System.getProperty("os.arch").equals("x86_64")) {
            architecture = "amd64";
        } else if (System.getProperty("os.arch").equals("arm") || System.getProperty("os.arch").equals("aarch32")) {
            architecture = "aarch32";
        } else if (System.getProperty("os.arch").equals("arm64") || System.getProperty("os.arch").equals("aarch64")) {
            architecture = "aarch64";
        }

        // Determine the OS name
        String outOsName = "";
        if (osName.contains("win")) {
            outOsName = "Windows";
        } else if (osName.contains("nux") || osName.contains("nix")) {
            // Android dalvik bytecode need special handling
            if (System.getProperty("java.vm.name").toLowerCase().contains("dalvik")) {
                outOsName = "Android";
                architecture = ""; // We do not need to specify architecture for Android
            } else {
                outOsName = "Linux";
            }
        }

// build the platform name
        String delimiter = (outOsName.isEmpty() || architecture.isEmpty()) ? "" : "-";
        String platform = outOsName + delimiter + architecture;

        return platform;
    }

    public static void load(String name) {
        if (libLoaded)
            return;
        final String libname = System.mapLibraryName(name);
        final String opentimelibname = System.mapLibraryName("opentime");
        final String OTIOlibname = System.mapLibraryName("opentimelineio");
        final String platformName = getPlatformName();
        final String libPkgPath = "/" + platformName + "/" + libname;
        final String libOpentimePath = "/" + platformName + "/" + opentimelibname;
        final String libOTIOPath = "/" + platformName + "/" + OTIOlibname;
        try {
            NativeUtils.loadLibraryFromJar(libOpentimePath);
            NativeUtils.loadLibraryFromJar(libOTIOPath);
            NativeUtils.loadLibraryFromJar(libPkgPath);
            libLoaded = true;
        } catch (IllegalArgumentException | IOException e) {
            System.loadLibrary("opentime");
            System.loadLibrary("opentimelineio");
            System.loadLibrary(name);
            libLoaded = true;
        } catch (Exception e) {
            libLoaded = false;
            System.err.println("Unable to load native library.");
        }
    }
}
