
package com.technofovea.hllib;

import com.sun.jna.Memory;
import com.technofovea.hllib.enums.DirectoryItemType;
import com.technofovea.hllib.enums.PackageType;
import com.technofovea.hllib.masks.FileMode;
import com.technofovea.hllib.methods.FullLibrary;
import com.technofovea.hllib.methods.ManagedLibrary;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Darien Hager
 */
public class FileListingTest {

    private static final String TEST_BSP = "utest.bsp";

        static ManagedLibrary fixture;


    @BeforeClass
    public static void BeforeClass() throws Exception {
        String path = new File(FileListingTest.class.getClassLoader().getResource(".").toURI()).getAbsolutePath();
        System.setProperty("jna.library.path",path);

        fixture = HlLib.getLibrary();
        
        
    }

    private void buildList(ManagedLibrary lib, DirectoryItem folder, Set<String> store){
        List<DirectoryItem> children = folder.getChildren();
        for(DirectoryItem child:children){
            if(lib.itemGetType(child) == DirectoryItemType.FOLDER){
                buildList(lib, child, store);
            }else{
                store.add(lib.itemGetPath(child));
            }
        }
    }

    private HlPackage openFile(ManagedLibrary lib, File target) throws Exception{
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

        Set<String> expected = new HashSet<String>();
        expected.add("root\\Core.dll");
        expected.add("root\\DemoPlayer.dll");
        expected.add("root\\FileSystem_Steam.dll");
        expected.add("root\\HLTV-Readme.txt");
        expected.add("root\\Mp3dec.asi");
        expected.add("root\\Mss32.dll");
        expected.add("root\\Mssv12.asi");
        expected.add("root\\Mssv29.asi");
        expected.add("root\\a3dapi.dll");
        expected.add("root\\dbg.dll");
        expected.add("root\\hl.exe");
        expected.add("root\\hlds.exe");
        expected.add("root\\hltv.cfg");
        expected.add("root\\hltv.exe");
        expected.add("root\\hw.dll");
        expected.add("root\\language.inf");
        expected.add("root\\readme.txt");
        expected.add("root\\steam_api.dll");
        expected.add("root\\steam_api_c.dll");
        expected.add("root\\steamclient.dll");
        expected.add("root\\sw.dll");
        expected.add("root\\tier0.dll");
        expected.add("root\\tier0_s.dll");
        expected.add("root\\valve\\cl_dlls\\GameUI.dll");
        expected.add("root\\valve\\cl_dlls\\particleman.dll");
        expected.add("root\\vgui.dll");
        expected.add("root\\vgui2.dll");
        expected.add("root\\voice_miles.dll");
        expected.add("root\\voice_speex.dll");
        expected.add("root\\vstdlib.dll");
        expected.add("root\\vstdlib_s.dll");

        Set<String> found = new HashSet<String>();

        DirectoryItem root = fixture.packageGetRoot();
        buildList(fixture,root , found);

        Assert.assertEquals(expected, found);

        fixture.packageRemove(pkg);
        
    }

    @Test
    public void testBspListing() throws Exception {

        URL testurl = this.getClass().getResource(TEST_BSP);
        File testfile = new File(testurl.toURI());

        
        HlPackage pkg = openFile(fixture, testfile);


        Set<String> expected = new HashSet<String>();
        expected.add("root\\lumps\\utest_l_0.lmp");
        expected.add("root\\lumps\\utest_l_1.lmp");
        expected.add("root\\lumps\\utest_l_2.lmp");
        expected.add("root\\lumps\\utest_l_3.lmp");
        expected.add("root\\lumps\\utest_l_4.lmp");
        expected.add("root\\lumps\\utest_l_5.lmp");
        expected.add("root\\lumps\\utest_l_6.lmp");
        expected.add("root\\lumps\\utest_l_7.lmp");
        expected.add("root\\lumps\\utest_l_9.lmp");
        expected.add("root\\lumps\\utest_l_10.lmp");
        expected.add("root\\lumps\\utest_l_11.lmp");
        expected.add("root\\lumps\\utest_l_12.lmp");
        expected.add("root\\lumps\\utest_l_13.lmp");
        expected.add("root\\lumps\\utest_l_14.lmp");
        expected.add("root\\lumps\\utest_l_16.lmp");
        expected.add("root\\lumps\\utest_l_17.lmp");
        expected.add("root\\lumps\\utest_l_18.lmp");
        expected.add("root\\lumps\\utest_l_19.lmp");
        expected.add("root\\lumps\\utest_l_20.lmp");
        expected.add("root\\lumps\\utest_l_21.lmp");
        expected.add("root\\lumps\\utest_l_27.lmp");
        expected.add("root\\lumps\\utest_l_28.lmp");
        expected.add("root\\lumps\\utest_l_29.lmp");
        expected.add("root\\lumps\\utest_l_30.lmp");
        expected.add("root\\lumps\\utest_l_31.lmp");
        expected.add("root\\lumps\\utest_l_35.lmp");
        expected.add("root\\lumps\\utest_l_37.lmp");
        expected.add("root\\lumps\\utest_l_39.lmp");
        expected.add("root\\lumps\\utest_l_40.lmp");
        expected.add("root\\lumps\\utest_l_43.lmp");
        expected.add("root\\lumps\\utest_l_44.lmp");
        expected.add("root\\lumps\\utest_l_46.lmp");
        expected.add("root\\lumps\\utest_l_47.lmp");
        expected.add("root\\lumps\\utest_l_51.lmp");
        expected.add("root\\lumps\\utest_l_52.lmp");
        expected.add("root\\lumps\\utest_l_55.lmp");
        expected.add("root\\lumps\\utest_l_56.lmp");
        expected.add("root\\lumps\\utest_l_59.lmp");
        expected.add("root\\materials\\maps\\utest\\dev\\dev_blendmeasure2_wvt_patch.vmt");
        expected.add("root\\materials\\maps\\utest\\dev\\dev_blendmeasure_wvt_patch.vmt");
        expected.add("root\\utest.ent");
        expected.add("root\\utest.zip");
        Set<String> found = new HashSet<String>();

        DirectoryItem root = fixture.packageGetRoot();
        buildList(fixture,root , found);

        for(String s: found){
            //System.out.println("expected.add(\""+s+"\");");
        }

        Assert.assertEquals(expected, found);

        fixture.packageRemove(pkg);


        

    }
}
