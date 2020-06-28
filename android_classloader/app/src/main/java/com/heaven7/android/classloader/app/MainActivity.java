package com.heaven7.android.classloader.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.heaven7.android.classloader.app.eclipse.EclipseCompileTest;
import com.heaven7.core.util.PermissionHelper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity {

    private final PermissionHelper mHelper = new PermissionHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper.startRequestPermission(new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE},
                new int[]{1,2},
                new PermissionHelper.ICallback() {
            @Override
            public void onRequestPermissionResult(String s, int i, boolean b) {
                System.out.println("permission result >>> "  + s + " , state = " + b);
            }
            @Override
            public boolean handlePermissionHadRefused(String s, int i, Runnable runnable) {
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onClickTestEcCompile(View v){
       // File file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        final File file = Environment.getExternalStorageDirectory(); //not work when compile 29+.
        // should add ' android:requestLegacyExternalStorage="true" ' in application node

        EclipseCompileTest test = new EclipseCompileTest(this, file);
        test.test();
    }

    public void onClickTestCodeGen(View view) {
        String template  = "package com.xx.xx; public class $CLASSNAME{ private String attr1; private String attr2;" +
                "public $RETURN jump($IN){ return $IN.toString(); }" +
                "}";
        Map<String, String> map = new HashMap<>();
        map.put("CLASSNAME", "Test");
        map.put("RETURN", "String");
        map.put("IN", "TestUtils");

        String extra = "public void test1(){return;}";
        String imports = "import test.TestUtils;\n";
        DynamicCodeGenerator codeGenerator = new DynamicCodeGenerator(template, map, extra, imports);
        System.out.println(codeGenerator.generate());
    }

    public void onClickTestProxyView(View view) {
        startActivity(new Intent(this, TestProxyViewAc.class));
    }

    public void onClickTestCustomLayout(View view) {
        startActivity(new Intent(this, TestCustomLayoutAc.class));
    }
}
