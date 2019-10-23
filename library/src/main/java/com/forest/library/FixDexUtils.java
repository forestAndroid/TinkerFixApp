package com.forest.library;

import android.content.Context;

import com.forest.library.utils.ArrayUtils;
import com.forest.library.utils.Constants;
import com.forest.library.utils.ReflectUtils;

import java.io.File;
import java.util.HashSet;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * 修复工具类
 */
public class FixDexUtils {

    //有可能有多个修复包同时修复
    private static HashSet<File> loadedDex = new HashSet<>();

    static {
        loadedDex.clear();
    }

    public static void loadFixedDex(Context context) {

        File fileDir = context.getDir(Constants.DEX_DIR, Context.MODE_PRIVATE);

        File[] files = fileDir.listFiles();

        for (File file : files) {
            if (file.getName().endsWith(Constants.DEX_SUFFIX) && "classes2.dex".equalsIgnoreCase(file.getName())) {
                loadedDex.add(file);
            }
        }

        //创建类加载器
        createDexClassLoader(context, fileDir);
    }

    private static void createDexClassLoader(Context context, File fileDir) {
        String optimizeDir = fileDir.getAbsolutePath() + File.separator + "opt_dex";
        File fopt = new File(optimizeDir);
        if (!fopt.exists()) {
            fopt.mkdirs();
        }
        for (File dex : loadedDex) {
            DexClassLoader classLoader = new DexClassLoader(dex.getAbsolutePath(),
                    optimizeDir, null, context.getClassLoader());

            hotFix(classLoader, context);
        }
    }

    private static void hotFix(DexClassLoader classLoader, Context context) {

        //获取系统的pathClassLoader(类加载器)
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        //获取自有的dexElements数组
        try {
            Object myElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(classLoader));
            //获取系统的dexElements数组
            Object systemElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(pathClassLoader));
            //合并后，生成新的dexElements数组
            Object dexElements = ArrayUtils.combineArray(myElements, systemElements);
            //获取系统的pathList属性
            Object systemPathList = ReflectUtils.getPathList(pathClassLoader);
            //将新的dexElements数组，通过反射的技术，给系统的pathList属性重新赋值
            ReflectUtils.setField(systemPathList, systemPathList.getClass(), dexElements);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
