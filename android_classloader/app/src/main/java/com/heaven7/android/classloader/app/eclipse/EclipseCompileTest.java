package com.heaven7.android.classloader.app.eclipse;

import android.content.Context;
import android.os.Environment;

import com.heaven7.android.classloader.ClassLoaderManager;
import com.heaven7.android.classloader.GeneratedClassLoader;
import com.heaven7.java.base.util.FileUtils;
import com.heaven7.java.base.util.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
        //4, load

        //1
        File storage = dir;

        File dst = new File(storage, "/android.jar");
        if(!dst.exists()){
            BufferedInputStream bis = null;
            OutputStream dexWriter = null;
            int BUF_SIZE = 8 * 1024;
            try {
                bis = new BufferedInputStream(context.getAssets().open("a27/android.jar"));
                dexWriter = new BufferedOutputStream(
                        new FileOutputStream(dst.getAbsolutePath()));
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
        }
        //2.
        String src = "package com.heaven7.android.classloader.app.eclipse; \n" +
                "public class Test{" +
                     "public String toString() {\n" +
                    "    return \"Hallo Heaven7!\";\n" +
                    "}" +
                "}";
        FileUtils.writeTo(new File( storage.getAbsolutePath() + "/Test.java"), src);

        src = "package com.heaven7.android.classloader.app.eclipse; \n" +
                "public class Test1{" +
                "public String toString() {\n" +
                "    return \"Hallo Google!\";\n" +
                "}" +
                "}";
        FileUtils.writeTo(new File( storage.getAbsolutePath() + "/Test1.java"), src);

        //3 .can compile multi class at same time.
        System.err.println("instantiating the compiler and compiling the java file");
        org.eclipse.jdt.internal.compiler.batch.Main ecjMain = new org.eclipse.jdt.internal.compiler.batch.Main(
                new PrintWriter(System.out), new PrintWriter(System.err), false/*noSystemExit*/, null);
        boolean result = ecjMain.compile(new String[]{"-classpath",
                storage.getAbsolutePath() + "/android.jar",
                storage.getAbsolutePath() + "/Test.java",
                storage.getAbsolutePath() + "/Test1.java",
        });
        System.out.println(result);
        //4, load class
        String file = storage.getAbsolutePath() + "/Test.class";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] bytes = IOUtils.readBytes(fis);
            GeneratedClassLoader loader = new ClassLoaderManager(storage).createClassLoader(getClass().getClassLoader());
            Class<?> clazz = loader.defineClass("com.heaven7.android.classloader.app.eclipse.Test", bytes);
            System.out.println(clazz.newInstance().toString());
            loader.reset();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(fis);
        }
    }
}
