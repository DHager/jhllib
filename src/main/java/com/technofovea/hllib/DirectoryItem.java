package com.technofovea.hllib;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.technofovea.hllib.enums.DirectoryItemType;
import com.technofovea.hllib.masks.FileMode;
import com.technofovea.hllib.methods.ManagedLibrary;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Darien Hager
 */
public class DirectoryItem {

    static final int COPY_BUFFER_LEN = 4192;
    private static final Logger logger = LoggerFactory.getLogger(DirectoryItem.class);
    Pointer pointer = null;
    HlPackage parentPackage = null;

    DirectoryItem(HlPackage parent, Pointer p) {
        pointer = p;
        parentPackage = parent;
    }

    public Pointer getPointer() {
        return pointer;
    }

    public HlPackage getParentPackage() {
        return parentPackage;
    }

    public boolean isClosed() {
        return (parentPackage.isClosed());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DirectoryItem)) {
            return false;
        }
        DirectoryItem other = (DirectoryItem) obj;
        if (!parentPackage.equals(other.parentPackage)) {
            return false;
        }
        if (!pointer.equals(other.pointer)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return pointer.hashCode() + parentPackage.hashCode();
    }

    public List<DirectoryItem> getChildren() {
        ManagedLibrary lib = HlLib.getLibrary();
        List<DirectoryItem> children = new ArrayList<DirectoryItem>();
        if (isFolder()) {
            for (int i = 0; i < HlLib.getLibrary().folderGetCount(this); i++) {
                DirectoryItem child = lib.folderGetItem(this, i);
                children.add(child);
            }
        }
        return children;
    }

    public boolean isFile() {
        return (HlLib.getLibrary().itemGetType(this) == DirectoryItemType.FILE);
    }

    public boolean isFolder() {
        return (HlLib.getLibrary().itemGetType(this) == DirectoryItemType.FOLDER);
    }

    public String getName() {
        return (HlLib.getLibrary().itemGetName(this));
    }

    public String getPath() {
        return (HlLib.getLibrary().itemGetPath(this));
    }

    public Memory getData() {
        logger.debug("Getting data to memory for file {}", this.getName());

        if (!isFile()) {
            return null;
        }
        ManagedLibrary lib = HlLib.getLibrary();

        long actual_size = lib.itemGetSizeEx(this);

        HlStream stream = lib.fileCreateStream(this);
        try {
            FileMode streamMode = new FileMode();
            streamMode.set(FileMode.INDEX_MODE_READ);
            lib.streamOpen(stream, streamMode);

            Memory m = new Memory(actual_size);

            // Stream only works in smaller int-sized increments, so we need
            // multiple calls.
            long remaining = m.getSize();
            while (remaining > 0) {
                int chunk = (int) Math.min(Integer.MAX_VALUE, remaining);
                int numRead = lib.streamRead(stream, m, chunk);
                if (numRead < 0) {
                    logger.error("Stream reading result was unexpectedly {} when getting data for DirectoryItem", numRead);
                    break;
                }
                remaining -= numRead;
            }
            return m;
        } finally {
            lib.streamClose(stream);
        }
    }

    public void extractToFile(File target) throws IOException {

        logger.debug("Extracting data to filesystem for file {}, dest: {}", this.getPath(), target.getAbsolutePath());
        if (!isFile()) {
            throw new IOException("This DirectoryItem is not a file.");
        }
        
        ManagedLibrary lib = HlLib.getLibrary();

        long actual_size = lib.itemGetSizeEx(this);

        HlStream stream = lib.fileCreateStream(this);
        try {
            FileMode streamMode = new FileMode();
            streamMode.set(FileMode.INDEX_MODE_READ);
            lib.streamOpen(stream, streamMode);

            FileOutputStream fos = new FileOutputStream(target, false);
            try {
                Memory buffer = new Memory(COPY_BUFFER_LEN);
                while (true) {
                    int numRead = lib.streamRead(stream, buffer, (int) buffer.getSize());
                    if (numRead <= 0) {
                        break;
                    }
                    fos.write(buffer.getByteArray(0, numRead));
                }
            } finally {
                fos.close();
            }
        } finally {
            lib.streamClose(stream);
        }
    }
}
