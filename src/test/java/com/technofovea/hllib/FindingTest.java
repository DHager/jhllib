package com.technofovea.hllib;

import com.sun.jna.Memory;
import com.technofovea.hllib.enums.PackageType;
import com.technofovea.hllib.masks.FileMode;
import com.technofovea.hllib.masks.FindType;
import com.technofovea.hllib.methods.FullLibrary;
import com.technofovea.hllib.methods.ManagedLibrary;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Darien Hager
 */
public class FindingTest {


        static ManagedLibrary fixture;


    @BeforeClass
    public static void BeforeClass() throws Exception {
        DllPathFinder.setJnaPathPath();
        fixture = HlLib.getLibrary();

    }


    private HlPackage openFile(ManagedLibrary lib, File target) throws Exception {
        FileMode fm = new FileMode();
        fm.set(FileMode.INDEX_MODE_READ);

        PackageType pt = lib.getPackageTypeFromName(target.getAbsolutePath());
        if (pt == PackageType.NONE) {
            FileInputStream fis = new FileInputStream(target);
            byte[] testHeader = new byte[FullLibrary.HL_DEFAULT_PACKAGE_TEST_BUFFER_SIZE];
            int loaded = fis.read(testHeader);
            Memory m = new Memory(loaded);
            m.write(0, testHeader, 0, loaded);
            pt = lib.getPackageTypeFromMemory(m, loaded);
        }

        HlPackage pkg = lib.createPackage(pt);
        Assert.assertNotNull(pkg);
        Assert.assertFalse(pkg.isClosed());

        boolean r_bind = lib.bindPackage(pkg);
        Assert.assertTrue(r_bind);

        boolean r_open = lib.packageOpenFile(target.getAbsolutePath(), fm);
        Assert.assertTrue(r_open);

        return pkg;
    }

    @Test
    public void testGcfListing() throws Exception {

        URL testurl = GcfFinder.getTestGcf();
        File testfile = new File(testurl.toURI());


        HlPackage pkg = openFile(fixture, testfile);

        String[] existingItems = new String[]{
            "root\\valve\\cl_dlls\\GameUI.dll",
            "root\\valve\\cl_dlls\\particleman.dll",
        };
        String[] absentItems = new String[]{
            "root\\valve\\cl_dlls\\MissingFile.txt",
            "root\\valve\\particleman.dll",
            "root\\PARTICLEMAN.DLL",
            "PARTICLEMAN.DLL",
        };
        
      
        DirectoryItem root = fixture.packageGetRoot();

        FindType ft = new FindType();
        ft.set(FindType.HL_FIND_CASE_SENSITIVE,false);
        ft.set(FindType.HL_FIND_FILES);
        for(String searchString: existingItems){
            String[] bits = searchString.split("\\\\");
            String fname = bits[bits.length-1];
            DirectoryItem result = fixture.folderGetItemByPath(root, searchString, ft);
            Assert.assertEquals(fname,result.getName());
            Assert.assertNotNull(result);
            Assert.assertTrue(fixture.itemGetSize(result) > 0);
        }

        for(String searchString: absentItems){
            DirectoryItem result = fixture.folderGetItemByPath(root, searchString, ft);
            Assert.assertNull(result);
        }
        
        fixture.packageRemove(pkg);

    }

}
