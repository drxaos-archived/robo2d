package slick2d;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.util.Log;

import java.io.File;
import java.util.jar.JarFile;

/**
 * Solves the long standing issue of managing the native linking libraries by the developer by auto-magically
 * loading them into the Classpath at runtime. This class absolutely <b>must</b> be called before any of the native
 * library code is expected, or a {@link java.lang.UnsatisfiedLinkError}s will not be prevented. The loader can be disabled
 * by setting the property designated by {@link #DISABLE_AUTO_LOADING_PROPERTY} to the String value "true".
 *
 * @author Joshua Mabrey
 *         Jul 21, 2012
 */
public class NativeLoader {

    /**
     * Property to disable Native loading by this class
     */
    public static final String DISABLE_AUTO_LOADING_PROPERTY = "org.newdawn.slick.NativeLoader.load";

    /**
     * The system property to set for the library path; it points to the temp directory
     * the natives are unpacked into.
     */
    private static final String LIBRARY_SYSTEM_PROPERTY = "java.library.path";

    /**
     * The system property to set for the LWJGL library path; it points to the temp directory
     * the natives are unpacked into. This is redundant in reality, but it can't hurt.
     */
    private static final String LWJGL_LIBRARY_PROPERTY = "org.lwjgl.librarypath";

    /**
     * The system property to set for the Jinput library path; it points to the temp directory
     * the natives are unpacked into. This is redundant in reality, but it can't hurt.
     */
    private static final String JINPUT_LIBRARY_PROPERTY = "net.java.games.input.librarypath";

    /**
     * Listing of the file names of the Linux natives
     */
    private static final String[] LINUX_LIBRARIES = {
            "libjinput-linux.so",
            "libjinput-linux64.so",
            "liblwjgl.so",
            "liblwjgl64.so",
            "libopenal.so",
            "libopenal64.so"
    };

    /**
     * Listing of the file names of the Macintosh natives
     */
    private static final String[] MAC_LIBRARIES = {
            "libjinput-osx.jnilib",
            "liblwjgl.jnilib",
            "libopenal.dylib"
    };

    /**
     * Listing of the file names of the Windows natives
     */
    private static final String[] WINDOWS_LIBRARIES = {
            "jinput-dx8.dll",
            "jinput-dx8_64.dll",
            "jinput-raw.dll",
            "jinput-raw_64.dll",
            "lwjgl.dll",
            "lwjgl64.dll",
            "OpenAL32.dll",
            "OpenAL64.dll"
    };

    /**
     * Flag used by the loader to prevent double loading. If a loading call succeeds then this flag will be set to
     * true, and any further calls result in a single boolean comparison/no-op.
     */
    private static boolean alreadyLoaded = false;

    private static String nativesDir;

    /**
     * Determines the current operating system, extracts the OS dependent native libraries to a temp directory, and
     * adds that temporary directory to the classpath. Once the load call succeeds any furthur calls are short-circuted
     * for performance reasons.
     */
    public static final void load(String nativesDir) {
        NativeLoader.nativesDir = new File(nativesDir).getAbsolutePath();

        if (!librariesShouldBeLoaded()) {

            Log.info("Loading native libraries automatically");

            int OS = LWJGLUtil.getPlatform();
            try {
                NativeLoader.loadLibraries(OS);
                alreadyLoaded = true;
            } catch (Exception e) {
                Log.error("Unable to load native libraries. They must be set manually with '-Djava.library.path'", e);
                //We failed. We shouldn't kill the application however, linking *may* have succeeded because of user manually setting location
            }
        } else {
            Log.info("Not automatically loading native libraries.");
        }
    }

    /**
     * Attempts to call the native libraries this class is supposed to automatically load. If it successfully uses the classes
     * requiring the native libraries, it returns true, otherwise it returns false. This prevents the introduction of NativeLoader
     * from being a compatibility problem. Also uses the class level loaded flag to short circuit the expensive evaluation
     * of the loading state of the native libraries.
     *
     * @return whether the libraries NativeLoader should be loaded.
     */
    private static final boolean librariesShouldBeLoaded() {
        if (alreadyLoaded) {
            return true;//Short circuit evaluation
        }

        if (System.getProperty(DISABLE_AUTO_LOADING_PROPERTY, "true").equalsIgnoreCase("false")) {
            return true;//Respect user configuration to disable loading
        }

        boolean lwjglAvailable;

        try {
            lwjglAvailable = testLibraryAvailable("lwjgl", "lwjgl64");
        } catch (SecurityException e) {
            return false;//We shouldn't load libraries automatically if the SecurityManager won't let us
        }

        return lwjglAvailable;
    }

    /**
     * Tests a libraries current availability. If a library is load-able, either in 32 or 64 bit versions, this
     * method returns true. If libName64 is null then it is assumed that a 64 bit version should not be checked.
     * Do not attempt to split checks across two different calls if a 64 bit version is being examined.
     *
     * @param libName   The system independent 32 bit library name
     * @param libName64 The system independent 64 bit library name (optional)
     * @return The availability of the library, in 32 or 64 bit.
     * @throws SecurityException if a SecurityManager is preventing library loading
     */
    private static final boolean testLibraryAvailable(String libName, String libName64) throws SecurityException {
        try {
            System.loadLibrary(libName);
            Log.debug("Library " + libName + " is already available.");
            return true;
        } catch (UnsatisfiedLinkError e) {//library not found
            Log.debug("Library " + libName + " is not already available.");
            if (libName64 != null) {
                return testLibraryAvailable(libName64, null);
            }
            return false;
        } catch (SecurityException e1) {
            SecurityException stopLoadingException = new SecurityException("Unable to load libraries because of a SecurityManager", e1);
            throw stopLoadingException;
        }
    }

    /**
     * @param platform An integer representing the platform.
     */
    private static final void loadLibraries(int platform) {
        String[] libraries;
        JarFile nativeResourceJar = null;

        switch (platform) {//switch to get the system dependent library names
            case LWJGLUtil.PLATFORM_LINUX:
                libraries = LINUX_LIBRARIES;
                break;
            case LWJGLUtil.PLATFORM_MACOSX:
                libraries = MAC_LIBRARIES;
                break;
            case LWJGLUtil.PLATFORM_WINDOWS:
                libraries = WINDOWS_LIBRARIES;
                break;
            default:
                //If future developers add support for more operating systems without updating the switch then:
                throw new IllegalStateException("Encountered an unknown platform while loading native libraries");
        }

        //Set the properties to alert the JVM where to find the natives
        System.setProperty(LIBRARY_SYSTEM_PROPERTY,
                nativesDir + File.pathSeparator + System.getProperty(LIBRARY_SYSTEM_PROPERTY));//Additive to not overwrite other library paths

        //These two aren't additive because we should only point to one version of the libraries
        System.setProperty(LWJGL_LIBRARY_PROPERTY, nativesDir);
        System.setProperty(JINPUT_LIBRARY_PROPERTY, nativesDir);

        Log.info("LIBRARY_SYSTEM_PROPERTY: " + nativesDir);
    }
}