/*
 * Copyright (c) 1998 - 2012. University Corporation for Atmospheric Research/Unidata
 * Portions of this software were developed by the Unidata Program at the
 * University Corporation for Atmospheric Research.
 *
 * Access and use of this software shall impose the following obligations
 * and understandings on the user. The user is granted the right, without
 * any fee or cost, to use, copy, modify, alter, enhance and distribute
 * this software, and any derivative works thereof, and its supporting
 * documentation for any purpose whatsoever, provided that this entire
 * notice appears in all copies of the software, derivative works and
 * supporting documentation.  Further, UCAR requests that the user credit
 * UCAR/Unidata in any publications that result from the use of this
 * software or in any product that includes this software. The names UCAR
 * and/or Unidata, however, may not be used in any advertising or publicity
 * to endorse or promote any products or commercial entity unless specific
 * written permission is obtained from UCAR/Unidata. The user also
 * understands that UCAR/Unidata is not obligated to provide the user with
 * any support, consulting, training or assistance of any kind with regard
 * to the use, operation and performance of this software nor to provide
 * the user with any updates, revisions, new versions or "bug fixes."
 *
 * THIS SOFTWARE IS PROVIDED BY UCAR/UNIDATA "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UCAR/UNIDATA BE LIABLE FOR ANY SPECIAL,
 * INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING
 * FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 * NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION
 * WITH THE ACCESS, USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ucar.nc2.grib;

import junit.framework.TestCase;
import ucar.nc2.NetcdfFile;
import ucar.nc2.util.DiskCache;

import java.io.File;

/**
 * Describe
 *
 * @author caron
 * @since 2/16/12
 */
public class TestGribDiskCache extends TestCase {

  public void testDiskCache() throws Exception {
    String cacheDir = "C:/tmp2/testGribDiskCache/";
    File cache = new File(cacheDir);
    if (cache.exists()) {
      for (File data : cache.listFiles()) data.delete();
      cache.delete();
    }
    assert !cache.exists();

    DiskCache.setCachePolicy(true);
    DiskCache.setRootDirectory(cacheDir);
    assert DiskCache.getRootDirectory().equals(cacheDir);
    assert cache.exists();

    String dataDir = "Q:\\cdmUnitTest\\testCache";
    File dd = new File(dataDir);

    for (File data : dd.listFiles()) {
      if (data.getName().endsWith(".gbx8"))
        data.delete();
      if (data.getName().endsWith(".gbx9"))
        data.delete();
      if (data.getName().endsWith(".ncx"))
        data.delete();
    }

    for (File data : dd.listFiles()) {
      System.out.printf("Open %s%n", data.getPath());
      NetcdfFile ncfile = NetcdfFile.open(data.getPath());
      ncfile.close();
    }

    for (File data : dd.listFiles()) {
      assert !data.getName().endsWith(".gbx8");
      assert !data.getName().endsWith(".gbx9");
      assert !data.getName().endsWith(".ncx");
      if (data.getName().endsWith(".grib1") || data.getName().endsWith(".grib2")) {
        String index = data.getPath()+".ncx";
        File indexFile = DiskCache.getCacheFile(index);
        assert indexFile != null;
        assert indexFile.exists();
      }
    }


  }

}
