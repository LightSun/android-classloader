package com.heaven7.android.classloader.app.eclipse;

import android.content.Context;
import android.os.Environment;

import com.heaven7.java.base.util.FileUtils;
import com.heaven7.java.base.util.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

//https://stackoverflow.com/questions/11223408/how-to-implement-a-java-compiler-and-dex-converter-into-an-android-app
public class EclipseCompileTest {

    private final Context context;
    private final File dir;

    public EclipseCompileTest(Context context, File dir) {
        this.context = context;
        this.dir = dir;
    }

    public void test(){
        //1.copy android.jar
        //2. generate .java
        //3. compile

        //1
        File storage = dir;
        BufferedInputStream bis = null;
        OutputStream dexWriter = null;
        int BUF_SIZE = 8 * 1024;
        try {
            bis = new BufferedInputStream(context.getAssets().open("a27/android.jar"));
            dexWriter = new BufferedOutputStream(
                    new FileOutputStream(storage.getAbsolutePath() + "/android.jar"));
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while((len = bis.read(buf, 0, BUF_SIZE)) > 0) {
                dexWriter.write(buf, 0, len);
            }
            dexWriter.flush();
        } catch (Exception e) {
            System.err.println("Error while copying from assets: " + e.getMessage());
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(dexWriter);
        }
        //2.
        String src = "package com.heaven7.android.classloader.app.eclipse; \n" +
                "public class Test{" +
                     "public String toString() {\n" +
                    "    return \"Hallo Welt!\";\n" +
                    "}" +
                "}";
        FileUtils.writeTo(new File( storage.getAbsolutePath() + "/Test.java"), src);

        //3
        System.err.println("instantiating the compiler and compiling the java file");
        org.eclipse.jdt.internal.compiler.batch.Main ecjMain = new org.eclipse.jdt.internal.compiler.batch.Main(
                new PrintWriter(System.out), new PrintWriter(System.err), false/*noSystemExit*/, null);
        boolean result = ecjMain.compile(new String[]{"-classpath",
                storage.getAbsolutePath() + "/android.jar",
                storage.getAbsolutePath() + "/Test.java"});
        System.out.println(result);
    }
}
