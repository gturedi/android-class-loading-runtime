package test.gturedi.androidclassloadingatruntime;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import dalvik.system.DexClassLoader;

public class MainActivity
        extends AppCompatActivity {

    public static final String JAR_FILE_NAME = "output.jar";
    public static final String TARGET_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ JAR_FILE_NAME;
    public static final String FULL_CLASS_NAME = "robust.shared.TurediTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void test1(View view) {
        try {
            copyJarFromAssetsToSdcard();
            Class clazz = loadClassDynamically(FULL_CLASS_NAME, TARGET_PATH);
            useLoadedClass(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyJarFromAssetsToSdcard()
            throws IOException {
        InputStream in = getAssets().open(JAR_FILE_NAME);
        FileUtils.copyInputStreamToFile(in, new File(TARGET_PATH));
    }

    private Class<?> loadClassDynamically(String fullClassName, String fullPathToApk)
            throws ClassNotFoundException {
        File dexOutputDir = getDir("dex", Context.MODE_PRIVATE);
        DexClassLoader dexLoader = new DexClassLoader(fullPathToApk,
                dexOutputDir.getAbsolutePath(),
                null,
                getClassLoader());
        return Class.forName(fullClassName, true, dexLoader);
    }

    private void useLoadedClass(Class clazz)
            throws Exception {
        Callable<String> callable = (Callable<String>) clazz.newInstance();
        log(callable.call());
    }

    private void log(String msg) {
        Log.w("clazz", msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
