package com.technofovea.hllib.methods;

import com.technofovea.hllib.DirectoryItem;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.technofovea.hllib.HlPackage;
import com.technofovea.hllib.enums.PackageType;

/**
 * The interface defines functions which exist on the DLL but are not usually exposed via the Java-side API.
 * @author Darien Hager
 */
public interface FullLibrary extends BasicLibrary {

    /**
     * Called to initialize the DLL library.
     */
    public void initialize();

    /**
     * Called to shut down the DLL library.
     */
    public void shutdown();

    public boolean itemGetSize(DirectoryItem item, IntByReference size);

    public boolean itemGetSizeEx(DirectoryItem item, LongByReference size);

    public boolean itemGetSizeOnDisk(DirectoryItem item, IntByReference size);

    public boolean itemGetSizeOnDiskEx(DirectoryItem item, LongByReference size);

    public void itemGetPath(DirectoryItem item, byte[] buffer, int bufsize);

    public boolean createPackage(PackageType ePackageType, IntByReference packageId);

    public void packageClose();

    public void deletePackage(HlPackage pkg);

    public boolean packageGetExtractable(DirectoryItem file, ByteByReference extractable);

    public boolean packageGetFileSize(DirectoryItem file, IntByReference size);

    public boolean packageGetFileSizeOnDisk(DirectoryItem file, IntByReference size);

    public boolean packageCreateStream(DirectoryItem file, PointerByReference stream);

    public boolean fileCreateStream(DirectoryItem item, PointerByReference stream);
}
