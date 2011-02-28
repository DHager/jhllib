package com.technofovea.hllib;

import com.technofovea.hllib.enums.PackageType;
import com.technofovea.hllib.masks.FileMode;
import com.technofovea.hllib.methods.ManagedLibrary;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Darien Hager
 */
public class PointerInvalidationTest {

    static ManagedLibrary fixture;

    @BeforeClass
    public static void BeforeClass() throws Exception {
        String path = new File(PointerInvalidationTest.class.getClassLoader().getResource(".").toURI()).getAbsolutePath();
        System.setProperty("jna.library.path", path);
        fixture = HlLib.getLibrary();
    }

    @Before
    public void setup() throws Exception {
        Assert.assertTrue(HlLib.handler.lackey.openPackages.size() == 0);
        Assert.assertTrue(HlPackage.cache.map.size() == 0);
    }

    @After
    public void teardown() throws Exception {
        Assert.assertTrue(HlLib.handler.lackey.openPackages.size() == 0);
        Assert.assertTrue(HlPackage.cache.map.size() == 0);
    }

    @Test(expected = IllegalStateException.class)
    public void packageRootFailure() throws Exception {

        File target = new File(GcfFinder.getTestGcf().toURI());
        FileMode fm = new FileMode();
        fm.set(FileMode.INDEX_MODE_READ);

        PackageType pt = fixture.getPackageType(target);

        HlPackage pkg = fixture.createPackage(pt);
        Assert.assertNotNull(pkg);
        boolean r_bind = fixture.bindPackage(pkg);
        Assert.assertTrue(r_bind);
        boolean r_open = fixture.packageOpenFile(target.getAbsolutePath(), fm);
        Assert.assertTrue(r_open);

        DirectoryItem rootItem = fixture.packageGetRoot();

        for (int i = 0; i < fixture.folderGetCount(rootItem); i++) {
            DirectoryItem child = fixture.folderGetItem(rootItem, i);
        }


        Assert.assertFalse(rootItem.isClosed());
        Assert.assertFalse(pkg.isClosed());
        fixture.packageRemove(pkg);
        Assert.assertTrue(rootItem.isClosed());
        Assert.assertTrue(pkg.isClosed());


        // This should throw an exception
        fixture.folderGetCount(rootItem);


        Assert.fail();


    }

    @Test(expected = IllegalStateException.class)
    public void streamFailure() throws Exception {

        File target = new File(GcfFinder.getTestGcf().toURI());
        FileMode fm = new FileMode();
        fm.set(FileMode.INDEX_MODE_READ);


        PackageType pt = fixture.getPackageType(target);

        HlPackage pkg = fixture.createPackage(pt);
        Assert.assertNotNull(pkg);
        boolean r_bind = fixture.bindPackage(pkg);
        Assert.assertTrue(r_bind);
        boolean r_open = fixture.packageOpenFile(target.getAbsolutePath(), fm);
        Assert.assertTrue(r_open);

        DirectoryItem rootItem = fixture.packageGetRoot();

        List<HlStream> streams = new ArrayList<HlStream>();

        for (DirectoryItem child : rootItem.getChildren()) {
            if (child.isFile()) {
                HlStream s = fixture.packageCreateStream(child);
                streams.add(s);
                Assert.assertFalse(s.isClosed());
            }
        }

        fixture.packageRemove(pkg);

        for (HlStream s : streams) {
            Assert.assertTrue(s.isClosed());
        }
        // This should throw an exception
        fixture.streamGetMode(streams.get(0));

        Assert.fail();


    }
}
