package com.heaven7.android.classloader.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.heaven7.android.classloader.app.eclipse.EclipseCompileTest;
import com.heaven7.core.util.PermissionHelper;

import java.io.File;

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
        File file = Environment.getExternalStorageDirectory();
        EclipseCompileTest test = new EclipseCompileTest(this, file);
        test.test();
    }

}
