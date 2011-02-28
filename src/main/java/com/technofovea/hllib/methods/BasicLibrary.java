package com.technofovea.hllib.methods;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.technofovea.hllib.datatypes.Attribute;
import com.technofovea.hllib.DirectoryItem;
import com.technofovea.hllib.HlPackage;
import com.technofovea.hllib.HlStream;
import com.technofovea.hllib.enums.DirectoryItemType;
import com.technofovea.hllib.enums.HlAttributeType;
import com.technofovea.hllib.enums.HlOption;
import com.technofovea.hllib.enums.PackageAttribute;
import com.technofovea.hllib.enums.PackageType;
import com.technofovea.hllib.enums.SortField;
import com.technofovea.hllib.enums.SortOrder;
import com.technofovea.hllib.enums.StreamType;
import com.technofovea.hllib.enums.Validation;
import com.technofovea.hllib.masks.FileMode;
import com.technofovea.hllib.masks.FindType;
import com.technofovea.hllib.masks.SeekMode;

/**
 * Methods which exist on the DLL and can be used without significant Java
 * mediation.
 * @author Darien Hager
 */
public interface BasicLibrary extends Library{

    public static final int HL_DEFAULT_PACKAGE_TEST_BUFFER_SIZE = 8;







//
// Get/Set
//
    public boolean getBoolean(HlOption option);

    public void setBoolean(HlOption option, boolean value);

    public int getInteger(HlOption option);

    public void setInteger(HlOption option, int value);

    public int getUnsignedInteger(HlOption option);

    public void setUnsignedInteger(HlOption option, int value);

    public float getFloat(HlOption option);

    public void setFloat(HlOption option, float fValue);

    public String getString(HlOption option);

    public void setString(HlOption option, String value);

    public Pointer getVoid(HlOption option);

    public void setVoid(HlOption option, Pointer value);

//
// Attributes
//
    public boolean attributeGetBoolean(HlAttributeType attr);

    public void attributeSetBoolean(HlAttributeType attr, String name, boolean value);

    public int attributeGetInteger(HlAttributeType attr);

    public void attributeSetInteger(HlAttributeType attr, String name, int value);

    public int attributeGetUnsignedInteger(HlAttributeType attr);

    public void attributeSetUnsignedInteger(HlAttributeType attr, String name, int value, boolean hexadecimal);

    public float attributeGetFloat(HlAttributeType attr);

    public void attributeSetFloat(HlAttributeType attr, String name, float value);

    public String attributeGetString(HlAttributeType attr);

    public void attributeSetString(HlAttributeType attr, String name, String value);

//
// Directory Item
//
    public DirectoryItemType itemGetType(DirectoryItem item);

    public String itemGetName(DirectoryItem item);

    public int itemGetID(DirectoryItem item);

    public Pointer itemGetData(DirectoryItem item);

    public HlPackage itemGetPackage(DirectoryItem item);

    public DirectoryItem itemGetParent(DirectoryItem item);

    public boolean itemExtract(DirectoryItem item, String path);

//
// Directory Folder
//
    public int folderGetCount(DirectoryItem item);

    public DirectoryItem folderGetItem(DirectoryItem item, int index);

    public DirectoryItem folderGetItemByName(DirectoryItem item, String name, FindType find);

    public DirectoryItem folderGetItemByPath(DirectoryItem item, String path, FindType find);

    public void folderSort(DirectoryItem item, SortField field, SortOrder order, boolean recurse);

    public DirectoryItem folderFindFirst(DirectoryItem folder, String search, FindType find);

    public DirectoryItem folderFindNext(DirectoryItem folder, DirectoryItem item, String search, FindType find);

    public int folderGetSize(DirectoryItem item, boolean recurse);

    public int folderGetSizeOnDisk(DirectoryItem item, boolean recurse);

    public long folderGetSizeEx(DirectoryItem item, boolean recurse);

    public long folderGetSizeOnDiskEx(DirectoryItem item, boolean recurse);

    public int folderGetFolderCount(DirectoryItem item, boolean recurse);

    public int folderGetFileCount(DirectoryItem item, boolean recurse);

//
// Directory File
//
    public int fileGetExtractable(DirectoryItem item);

    public Validation fileGetValidation(DirectoryItem item);

    public int fileGetSize(DirectoryItem item);

    public int fileGetSizeOnDisk(DirectoryItem item);

    public void fileReleaseStream(DirectoryItem item, HlStream streamObj);

//
// Stream
//
    public StreamType streamGetType(HlStream streamObj);

    public boolean streamGetOpened(HlStream streamObj);

    public int streamGetMode(HlStream streamObj);

    public boolean streamOpen(HlStream streamObj, FileMode mode);

    public void streamClose(HlStream streamObj);

    public int streamGetStreamSize(HlStream streamObj);

    public int streamGetPointer(HlStream streamObj);

    public int streamSeek(HlStream streamObj, long offset, SeekMode seekMode);

    public boolean streamReadChar(HlStream streamObj, ByteByReference chr);

    public int streamRead(HlStream streamObj, Memory data, int bytes);

    public boolean streamWriteChar(HlStream streamObj, char c);

    public int streamWrite(HlStream streamObj, Memory data, int bytes);

//
// Package
//
    public boolean bindPackage(HlPackage pkg);

    public PackageType getPackageTypeFromName(String name);

    public PackageType getPackageTypeFromMemory(Memory buffer, int bufferSize);

    public PackageType getPackageTypeFromStream(HlStream streamObj);

    public PackageType packageGetType();

    public String packageGetExtension();

    public String packageGetDescription();

    public boolean packageGetOpened();

    public boolean packageOpenFile(String fileName, FileMode mode);

    public boolean packageOpenMemory(Memory data, int dataSize, FileMode mode);

    public boolean packageOpenProc(Pointer userData, FileMode mode);

    public boolean packageOpenStream(HlStream streamObj, FileMode mode);

    public boolean packageDefragment();

    public DirectoryItem packageGetRoot();

    public int packageGetAttributeCount();

    public String packageGetAttributeName(PackageAttribute attr);

    public boolean packageGetAttribute(PackageAttribute attr, Attribute saveTo);

    public int packageGetItemAttributeCount();

    //TODO split off Package and Item attributes into a separate enum?
    // Need more documentation about them first.
    public String packageGetItemAttributeName(PackageAttribute attr);
    public boolean packageGetItemAttribute(DirectoryItem item, PackageAttribute attr, Attribute saveTo);

    public void packageReleaseStream(HlStream streamObj);

    public String ncfFileGetRootPath();

    public void ncfFileSetRootPath(String rootPath);

    public boolean wadFileGetImageSizePaletted(DirectoryItem file, IntByReference paletteDataSize, IntByReference pixelDataSize);

    public boolean wadFileGetImageDataPaletted(DirectoryItem file, IntByReference width, IntByReference height, ByteByReference paletteData, ByteByReference pixelData);

    public boolean wadFileGetImageSize(DirectoryItem file, IntByReference pixelDataSize);

    public boolean wadFileGetImageData(DirectoryItem file, IntByReference width, IntByReference height, ByteByReference pixelData);

}
