package com.linsir.androidcameralibrary;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by linSir
 * date at 8/2/17.
 * describe:
 */

public class Util {


    /**
     * 将List转换成数组[1,3,5]
     */
    public static String IngListToString(List<Integer> list) {

        if (list.size() == 0){
            return "null";
        }

        String result = "[";
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                result += String.valueOf(list.get(i)) + ",";
            } else {
                result += String.valueOf(list.get(i)) + "]";
            }
        }
        return result;
    }


    /**
     * bitmap转file
     */

    public static File saveBitmapFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/img.png");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    /**
     * 将时间转换成时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }



}
