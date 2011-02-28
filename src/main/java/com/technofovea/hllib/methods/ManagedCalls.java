package com.technofovea.hllib.methods;

import com.technofovea.hllib.DirectoryItem;
import com.technofovea.hllib.HlPackage;
import com.technofovea.hllib.HlStream;
import com.technofovea.hllib.enums.PackageType;
import java.io.File;

/**
 * This interface defines methods which do not exist on the DLL itself, but are 
 * handled by an additional layer of Java code.
 * @author Darien Hager
 */
public interface ManagedCalls {

    public int itemGetSize(DirectoryItem item);

    public int itemGetSizeOnDisk(DirectoryItem item);

    public long itemGetSizeEx(DirectoryItem item);

    public long itemGetSizeOnDiskEx(DirectoryItem item);

    public String itemGetPath(DirectoryItem item);

    public HlPackage getCurrentlyBoundPackage();

    public HlPackage createPackage(PackageType ePackageType);

    public PackageType getPackageType(File f);

    /**
     * Replaces packageClose() and packageDelete() with a single call, primarily
     * to make it easier to handle the safe marking of HlPackage and DirectoryItem
     * objects as invalid.
     * @param pkg The package to remove and close.
     */
    public void packageRemove(HlPackage pkg);

    public void packageRemoveAll();

    public boolean packageGetExtractable(DirectoryItem file);

    public int packageGetFileSize(DirectoryItem file);

    public int packageGetFileSizeOnDisk(DirectoryItem file);

    public HlStream packageCreateStream(DirectoryItem file);

    public HlStream fileCreateStream(DirectoryItem item);
}
