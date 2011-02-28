package com.technofovea.hllib;

import com.technofovea.hllib.datatypes.Attribute;
import com.technofovea.hllib.enums.PackageAttribute;
import com.technofovea.hllib.enums.PackageType;
import com.technofovea.hllib.masks.FileMode;
import com.technofovea.hllib.methods.ManagedLibrary;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Darien Hager
 */
public class AttributeReadingTest {

    private static final String TEST_GCF_FILE = "root\\readme.txt";
    private static final String TEST_BSP = "utest.bsp";

        static ManagedLibrary fixture;


    @BeforeClass
    public static void BeforeClass() throws Exception{
        String path = new File(AttributeReadingTest.class.getClassLoader().getResource(".").toURI()).getAbsolutePath();
        System.setProperty("jna.library.path",path);
        fixture = HlLib.getLibrary();
    }

    private static HlPackage makePackage(ManagedLibrary lib, File target) {

        PackageType pt = lib.getPackageType(target);

        HlPackage pkg = lib.createPackage(pt);
        Assert.assertNotNull(pkg);

        boolean r_bind = lib.bindPackage(pkg);
        Assert.assertTrue(r_bind);


        FileMode fm = new FileMode();
        fm.set(FileMode.INDEX_MODE_READ, true);
        fm.set(FileMode.INDEX_MODE_VOLATILE);
        boolean r_open = lib.packageOpenFile(target.getAbsolutePath(), fm);
        Assert.assertTrue(r_open);

        return pkg;

    }

    @Test
    public void readGcfAttributes() throws Exception {       

        File target = new File(GcfFinder.getTestGcf().toURI());
        HlPackage pkg = makePackage(fixture, target);

        Attribute holder = new Attribute();

        Map<PackageAttribute, Object> expected = new HashMap<PackageAttribute, Object>();
        expected.put(PackageAttribute.GCF_PACKAGE_BLOCK_LENGTH, 8192);
        expected.put(PackageAttribute.GCF_PACKAGE_ALLOCATED_BLOCKS, 1472);
        expected.put(PackageAttribute.GCF_PACKAGE_USED_BLOCKS, 1472);
        expected.put(PackageAttribute.GCF_PACKAGE_VERSION, 6);

        for (PackageAttribute attrib : expected.keySet()) {
            boolean r_attrget = fixture.packageGetAttribute(attrib, holder);
            Assert.assertTrue("Unable to retrieve attribute of package", r_attrget);
            Assert.assertEquals(expected.get(attrib), holder.getJavaData());
        }

        DirectoryItem root = fixture.packageGetRoot();
        for (DirectoryItem child : root.getChildren()) {
            if (child.getName().equals("steamclient.dll")) {
                fixture.packageGetItemAttribute(child, PackageAttribute.GCF_ITEM_FRAGMENTATION, holder);
                float actual_frag = (Float)holder.getJavaData();
                float expected_frag = 1.1019284f;
                Assert.assertTrue(Math.abs(actual_frag - expected_frag) < 0.001);
            }

        }
    }

    @Test
    public void readBspItemAttributes() throws Exception {

        File target = new File(getClass().getResource(TEST_BSP).toURI());
        HlPackage pkg = makePackage(fixture, target);

        Attribute holder = new Attribute();

        Map<PackageAttribute, Object> expected = new HashMap<PackageAttribute, Object>();
        expected.put(PackageAttribute.BSP_PACKAGE_VERSION, 20);

        for (PackageAttribute attrib : expected.keySet()) {
            boolean r_attrget = fixture.packageGetAttribute(attrib, holder);
            Assert.assertTrue("Unable to retrieve attribute of package", r_attrget);
            Assert.assertEquals(expected.get(attrib), holder.getJavaData());
        }
    }
}
