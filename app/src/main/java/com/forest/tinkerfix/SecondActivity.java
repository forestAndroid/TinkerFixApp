package com.forest.tinkerfix;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.forest.library.FixDexUtils;
import com.forest.library.utils.Constants;
import com.forest.library.utils.FileUtils;
import com.forest.tinkerfix.utils.ParamsSort;

import java.io.File;
import java.io.IOException;


public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
    //显示
    public void show(View view) {
        ParamsSort paramsSort = new ParamsSort();
        paramsSort.math(this);
    }

    //修复
    public void fix(View view) {
        File sourceFile = new File(Environment.getExternalStorageDirectory(), Constants.DEX_NAME);
        File targetFile = new File(getDir(Constants.DEX_DIR, Context.MODE_PRIVATE).getAbsoluteFile()
                +File.separator+Constants.DEX_NAME);
        if(targetFile.exists()){
            targetFile.delete();
            Toast.makeText(this,"删除以前的dex",Toast.LENGTH_SHORT).show();
        }
        try {
            FileUtils.copyFile(sourceFile,targetFile);
            Toast.makeText(this,"文件复制成功",Toast.LENGTH_SHORT).show();
            FixDexUtils.loadFixedDex(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
