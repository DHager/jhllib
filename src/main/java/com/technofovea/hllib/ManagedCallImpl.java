
package com.technofovea.hllib;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.technofovea.hllib.enums.PackageType;
import com.technofovea.hllib.methods.FullLibrary;
import com.technofovea.hllib.methods.ManagedCalls;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Darien Hager
 */
class ManagedCallImpl implements ManagedCalls {

    private static final Logger logger = LoggerFactory.getLogger(DirectoryItem.class);
    static final int PATH_BUFFER_SIZE = 512;
    byte[] pathBuffer = new byte[PATH_BUFFER_SIZE];
    FullLibrary lib;
    Set<HlPackage> openPackages = new HashSet<HlPackage>();
    HlPackage currentPackage = null;

    ManagedCallImpl(FullLibrary lib) {
        this.lib = lib;
    }

    public void addPackage(HlPackage pkg) {
        openPackages.add(pkg);
    }

    public void delPackage(HlPackage pkg) {
        openPackages.remove(pkg);
        pkg.markClosed();
        if (pkg == currentPackage) {
            logger.debug("Active package removed, current package is now null");
            currentPackage = null;
        }
    }

    public void setCurrentPackage(HlPackage pkg) {
        currentPackage = pkg;
    }

    public HlPackage getCurrentlyBoundPackage() {
        return currentPackage;
    }

    public int itemGetSize(DirectoryItem item) {
        IntByReference temp = new IntByReference();
        boolean success = lib.itemGetSize(item, temp);
        if (success) {
            return temp.getValue();
        } else {
            logger.error("Failed getting size for DirectoryItem: {}", this.itemGetPath(item));
            return -1;
        }
    }

    public int itemGetSizeOnDisk(DirectoryItem item) {
        IntByReference temp = new IntByReference();
        boolean success = lib.itemGetSizeOnDisk(item, temp);
        if (success) {
            return temp.getValue();
        } else {
            logger.error("Failed getting size-on-disk for DirectoryItem: {} ", this.itemGetPath(item));
            return -1;
        }
    }

    public long itemGetSizeEx(DirectoryItem item) {
        LongByReference temp = new LongByReference();
        boolean success = lib.itemGetSizeEx(item, temp);
        if (success) {
            return temp.getValue();
        } else {
            logger.error("Failed getting size for DirectoryItem: {}", this.itemGetPath(item));
            return -1;
        }
    }

    public long itemGetSizeOnDiskEx(DirectoryItem item) {
        LongByReference temp = new LongByReference();
        boolean success = lib.itemGetSizeOnDiskEx(item, temp);
        if (success) {
            return temp.getValue();
        } else {
            logger.error("Failed getting size-on-disk for DirectoryItem: {} ", this.itemGetPath(item));
            return -1;
        }
    }

    public String itemGetPath(DirectoryItem item) {
        lib.itemGetPath(item, pathBuffer, pathBuffer.length);
        return Native.toString(pathBuffer);
    }

    public HlPackage createPackage(PackageType ePackageType) {
        IntByReference temp = new IntByReference();
        boolean created = lib.createPackage(ePackageType, temp);
        if (!created) {
            logger.error("Failed creating a new package of type {}", ePackageType);
            return null;
        } else {
            HlPackage pkg = HlPackage.create(temp.getValue());
            return pkg;
        }
    }

    public void packageRemoveAll() {
        for (HlPackage pkg : openPackages) {
            packageRemove(pkg);
        }
    }

    public void packageRemove(HlPackage pkg) {
        if (currentPackage == pkg) {
            delPackage(pkg);
            return;
        }

        // Bind to alternate package, remove it, then bind back to original
        HlPackage origPkg = currentPackage;
        lib.bindPackage(pkg);
        lib.packageClose();
        lib.deletePackage(pkg);
        delPackage(pkg);
        assert (pkg != origPkg);
        currentPackage = origPkg;
    }

    public boolean packageGetExtractable(DirectoryItem file) {
        ByteByReference temp = new ByteByReference();
        if (lib.packageGetExtractable(file, temp)) {
            byte b = temp.getValue();
            if (b == 0x00) {
                return false;
            } else {
                return true;
            }
        } else {
            logger.error("Failed determining if a directory item could be extracted: {}", this.itemGetPath(file));

            return false;
        }
    }

    public int packageGetFileSize(DirectoryItem item) {
        IntByReference temp = new IntByReference();
        if (lib.packageGetFileSize(item, temp)) {
            return temp.getValue();
        } else {
            logger.error("Failed determining package file size for item: {}", this.itemGetPath(item));
            return -1;
        }
    }

    public int packageGetFileSizeOnDisk(DirectoryItem item) {
        IntByReference temp = new IntByReference();
        if (lib.packageGetFileSizeOnDisk(item, temp)) {
            return temp.getValue();
        } else {
            logger.error("Failed determining package file size-on-disk for item: {}", this.itemGetPath(item));
            return -1;
        }
    }

    public HlStream packageCreateStream(DirectoryItem item) {
        PointerByReference temp = new PointerByReference();
        if (lib.packageCreateStream(item, temp)) {
            return new HlStream(HlLib.getLibrary().getCurrentlyBoundPackage(), temp.getValue());
        } else {
            logger.error("Failed creating a stream for package of item: {}", this.itemGetPath(item));
            return null;
        }
    }

    public HlStream fileCreateStream(DirectoryItem item) {
        PointerByReference temp = new PointerByReference();
        if (lib.fileCreateStream(item, temp)) {
            return new HlStream(HlLib.getLibrary().getCurrentlyBoundPackage(), temp.getValue());
        } else {
            logger.error("Failed creating a stream for item: {}", this.itemGetPath(item));
            return null;
        }
    }

    public PackageType getPackageType(File target) {
        if (!target.canRead()) {
            logger.error("Could not determine package type for file, not readable: {}",target.getAbsolutePath());
            return null;
        }
        PackageType pt = lib.getPackageTypeFromName(target.getAbsolutePath());
        if (pt == PackageType.NONE) {
            try {
                FileInputStream fis = new FileInputStream(target);
                byte[] testHeader = new byte[FullLibrary.HL_DEFAULT_PACKAGE_TEST_BUFFER_SIZE];
                int loaded = fis.read(testHeader);
                Memory m = new Memory(loaded);
                m.write(0, testHeader, 0, loaded);

                pt = lib.getPackageTypeFromMemory(m, loaded);
            } catch (IOException e) {
                logger.error("Could not determine package type from direct file access",e);
            }
        }
        return pt;
    }
}
