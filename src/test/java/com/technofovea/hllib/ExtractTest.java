package com.technofovea.hllib;

import com.sun.jna.Memory;
import com.sun.jna.Native;

import com.technofovea.hllib.enums.DirectoryItemType;
import com.technofovea.hllib.enums.HlOption;
import com.technofovea.hllib.enums.PackageType;
import com.technofovea.hllib.enums.StreamType;
import com.technofovea.hllib.masks.FileMode;
import com.technofovea.hllib.methods.ManagedLibrary;
import java.io.File;
import java.net.URL;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Darien Hager
 */
public class ExtractTest {

    private static final String TEST_FILE = "root\\readme.txt";
    private static final int TEST_FILE_SIZE = 39818;
    private static final String TEST_FILE_START = "Half-Life\r\nVersion 1.1.1.1\r\nReadme File";
    static ManagedLibrary fixture;

    @BeforeClass
    public static void BeforeClass() throws Exception {
        String path = new File(ExtractTest.class.getClassLoader().getResource(".").toURI()).getAbsolutePath();
        System.setProperty("jna.library.path", path);

        fixture = HlLib.getLibrary();

    }

    @Test
    public void extractReadme() throws Exception {

        URL testurl = GcfFinder.getTestGcf();
        File target = new File(testurl.toURI());

        PackageType pt = fixture.getPackageType(target);

        HlPackage pkg = fixture.createPackage(pt);
        if (pkg == null) {
            String err = fixture.getString(HlOption.ERROR);
            throw new HlLibException("Unable to create package (" + pt.toString() + ") for binding to file: " + err);
        }

        boolean r_bind = fixture.bindPackage(pkg);
        if (!r_bind) {
            String err = fixture.getString(HlOption.ERROR);
            throw new HlLibException("Could not bind package to file: " + err);
        }


        FileMode fm = new FileMode();
        fm.set(FileMode.INDEX_MODE_READ, true);
        fm.set(FileMode.INDEX_MODE_VOLATILE);
        fm.set(FileMode.INDEX_MODE_QUICK_FILEMAPPING);
        boolean r_open = fixture.packageOpenFile(target.getAbsolutePath(), fm);
        if (!r_open) {
            String err = fixture.getString(HlOption.ERROR);
            throw new HlLibException("An error occurred opening the file for reading " + target.getAbsolutePath() + " : " + err);
        }

        DirectoryItem root = fixture.packageGetRoot();
        assert (fixture.itemGetType(root).equals(DirectoryItemType.FOLDER));

        DirectoryItem targetNode = null;
        for (int i = 0; i < fixture.folderGetCount(root); i++) {
            DirectoryItem child = fixture.folderGetItem(root, i);
            if (fixture.itemGetType(child).equals(DirectoryItemType.FILE)) {
                String path = fixture.itemGetPath(child);
                if (path.equals(TEST_FILE)) {
                    targetNode = child;
                    break;
                }
            }
        }
        Assert.assertNotNull(targetNode);
        Assert.assertEquals(pkg, targetNode.parentPackage);

        int extractable = fixture.fileGetExtractable(targetNode);
        Assert.assertTrue(extractable > 0);


        int actual_size = fixture.itemGetSize(targetNode);
        Assert.assertEquals(TEST_FILE_SIZE, actual_size);




        //Pointer stream = fixture.fileCreateStream(targetNode);
        HlStream stream = fixture.fileCreateStream(targetNode);
        Assert.assertNotNull(stream);
        Assert.assertEquals(targetNode.parentPackage, stream.parentPackage);


        FileMode streamMode = new FileMode();
        streamMode.set(FileMode.INDEX_MODE_READ);

        boolean r_streamopen = fixture.streamOpen(stream, streamMode);
        Assert.assertTrue(r_streamopen);

        StreamType type = fixture.streamGetType(stream);
        //System.out.println("Type: "+type.toString());

        Memory m = new Memory(actual_size);
        int numRead = fixture.streamRead(stream, m, (int) m.getSize());
        assert (numRead == m.getSize());
        byte[] buf = new byte[actual_size];
        m.read(0, buf, 0, buf.length);
        //System.out.println(Native.toString(buf));
        String data = Native.toString(buf);
        String foundIntro = data.substring(0, Math.min(data.length(), TEST_FILE_START.length()));
        Assert.assertEquals(TEST_FILE_START, foundIntro);


        // Again, this time using the getData convenience method
        Memory m2 = targetNode.getData();
        Assert.assertEquals(actual_size, m2.getSize());
        byte[] buf2 = new byte[actual_size];
        m2.read(0, buf2, 0, buf2.length);
        String data2 = Native.toString(buf2);

        String foundIntro2 = data2.substring(0, Math.min(data2.length(), TEST_FILE_START.length()));
        Assert.assertEquals(TEST_FILE_START, foundIntro2);

    }
}
