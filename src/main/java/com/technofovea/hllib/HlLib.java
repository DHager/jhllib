package com.technofovea.hllib;

import com.sun.jna.FunctionMapper;
import com.sun.jna.NativeLibrary;
import com.technofovea.hllib.methods.FullLibrary;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.technofovea.hllib.enums.HlOption;
import com.technofovea.hllib.methods.ManagedLibrary;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides access to the singleton libary interface with the HlLib
 * DLL. It is possible to either request the raw interface, or the safer proxied
 * "managed" interface. These are not separate instances, just different levels
 * of safety for accessing the singleton.
 * 
 * @author Darien Hager
 */
public class HlLib {

    public static final String LIBRARY_NAME = "hllib";
    public static final String ARCH_PROPERTY = "sun.arch.data.model";
    public static final String ARCH_32 = "32";
    public static final String ARCH_64 = "64";
    public static final String ARCH_32_SUFFIX = "_32";
    public static final String ARCH_64_SUFFIX = "_64";
    private static final Logger logger = LoggerFactory.getLogger(HlLib.class);

    public static final String LIBRARY_VERSION = "2.4.0";
    static ManagedLibrary instance = null;
    static FullLibrary rawInstance;
    static InvocationManager handler;

    static void setup() {


        FunctionMapper func_mapper = new FunctionMapper() {

            public String getFunctionName(NativeLibrary lib, Method target) {
                // Add "hl" and uppercase the first letter.
                String original = target.getName();
                StringBuilder temp = new StringBuilder(original);
                if (original.startsWith("wad")) {
                    temp.replace(0, 3, "WAD");
                } else if (original.startsWith("ncf")) {
                    temp.replace(0, 3, "NCF");
                }
                // Capitalize first letter if not already done
                temp.replace(0, 1, temp.substring(0, 1).toUpperCase());
                temp.insert(0, "hl"); // Prefix with "hl"
                return temp.toString();
            }
        };

        logger.debug("Setting JNA type and function mappers");
        Map<String, Object> options = new HashMap<String, Object>();
        options.put(Library.OPTION_TYPE_MAPPER, new HlTypeMapper());
        options.put(Library.OPTION_FUNCTION_MAPPER, func_mapper);

        logger.debug("Detecting architecture from property {}", ARCH_PROPERTY);
        String arch = System.getProperty(ARCH_PROPERTY);
        final String libName;
        if (ARCH_32.equals(arch)) {
            libName = LIBRARY_NAME + ARCH_32_SUFFIX;
        } else if (ARCH_64.equals(arch)) {
            libName = LIBRARY_NAME + ARCH_64_SUFFIX;
        } else {
            logger.warn("Unable to determine architecture (32-bit vs 64-bit) for HlLib. Defaulting to 32bit.");
            libName = LIBRARY_NAME + ARCH_32_SUFFIX;
        }
        logger.info("Loading native library: {}", libName);
        rawInstance = (FullLibrary) Native.loadLibrary(libName, FullLibrary.class, options);

        logger.debug("Calling library initialization function");
        rawInstance.initialize();

        logger.debug("Creating dynamic proxy for library access");
        handler = new InvocationManager(rawInstance);
        ManagedLibrary proxy = (ManagedLibrary) Proxy.newProxyInstance(
                FullLibrary.class.getClassLoader(),
                new Class[]{ManagedLibrary.class},
                handler);

        instance = proxy;


        String version = instance.getString(HlOption.VERSION);
        if (!LIBRARY_VERSION.equalsIgnoreCase(version)) {
            logger.warn("Version of HlLib, '{}',did not match the expected version, '{}'", version, LIBRARY_VERSION);
        }

    }

    @Override
    protected void finalize() throws Throwable {
        if (rawInstance != null) {
            rawInstance.shutdown();
        }
    }

    /**
     * Creates or retrieves the singleton which manages the overhead of packages
     * and startup/shutdown code.
     * 
     * @return HlLibrary proxy/implementation
     */
    public static ManagedLibrary getLibrary() {
        synchronized (HlLib.class) {
            if (instance == null) {
                setup();
            }
            return instance;
        }
    }

    /**
     * Use with caution! This provides the raw experience, the mostly-unmanaged
     * library with no proxying, startup/shutdown code, automatic package
     * selection, etc. You can crash your JVM with ease. Do not use the raw
     * and managed libraries at the same time, or chaos may ensue.
     *
     * It is recommended you only use this if you have access to the source
     * files to debug possible errors.
     *
     * Note that the "initialize" library call will already have been made.
     * @todo make public again if needed
     * @return HlLibrary proxy/implementation
     */
    private static FullLibrary getRawLibrary() {
        synchronized (HlLib.class) {
            if (rawInstance == null) {
                setup();
            }
            return rawInstance;
        }

    }
}
