package com.example.hand_knitted.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hand_knitted.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class MyUtils {


    public static final String[] tool = new String[]{"棒织", "编织", "钩织"};
    public static final String[] group = new String[]{"儿童", "女士", "男式", "婴幼儿", "老年人"};
    public static final String[] style = new String[]{"麻花", "提花", "雪花", "流苏", "叶子花"};


    public static final int[] toolid = {R.id.RBtool1, R.id.RBtool2, R.id.RBtool3};
    public static final int[] groupid = {R.id.RBgroup1, R.id.RBgroup2, R.id.RBgroup3, R.id.RBgroup4, R.id.RBgroup5};
    public static final int[] styleid = {R.id.RBstyle1, R.id.RBstyle2, R.id.RBstyle3, R.id.RBstyle4, R.id.RBstyle5};

    public static final int IMAGE_PICKER = 0;
    public static final int POST_EDIT = 1;

    //获取系统当前时间/以字符串形式存储
    public static String getCurrentDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd.HH:mm:ss");// HH:mm:ss
        //   Log.w("date到底有没有被规格化？", str);
        return simpleDateFormat.format(date);
    }


    public static Uri getImageContentUri(File imageFile,Context context) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static String saveBitmapFile(View toSaveView) {
        //开启缓存
        toSaveView.setDrawingCacheEnabled(true);
        //获取bitmap
        Bitmap bitmapTemp = toSaveView.getDrawingCache();
        bitmapTemp = Bitmap.createBitmap(bitmapTemp);
        //关闭缓存
        toSaveView.setDrawingCacheEnabled(false);
        //保存本地


        File dataDir = Environment.getExternalStorageDirectory();
        String temp = dataDir.getPath() + "/DCIM/Camera/"+"share.png";;
      //  Uri uri = getImageContentUri(temp,context);
        //如果这个文件在本地
        File file = new File(temp);
        //if (file.exists()) {
     //       return temp;
      //  }
        try {
            //创建文件
     //       file.createNewFile();
            //Bitmap写入文件
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmapTemp.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();

            //通知文件扫描刷新
            //  sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            // ToastUtil.makeText(this, getString(R.string.tupianbaocunchenggongbaocunzai, file.getAbsolutePath()), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    private static void ShareImage(Context context, String imagePath){
        if (imagePath != null){
            Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
            File file = new File(imagePath);
            //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));// 分享的内容
            intent.putExtra(Intent.EXTRA_STREAM, getImageContentUri(file,context));// 分享的内容
            intent.setType("image/*");// 分享发送的数据类型
            Intent chooser = Intent.createChooser(intent, "Share screen shot");
            if(intent.resolveActivity(context.getPackageManager()) != null){
                context.startActivity(chooser);
            }
        } else {
            Toast.makeText(context, "文件未保存到本地", Toast.LENGTH_SHORT).show();
        }
    }

    /**截屏分享，供外部调用**/
    public static void shotShare(Context context,View toSaveView){
        //截屏
        String path= saveBitmapFile(toSaveView);
        //分享
        if(path!=null){
            ShareImage(context,path);
        }
    }

    public static String getBmobKey(Context context,String key){

        Properties properties = new Properties();
        try {
                //方法一：通过activity中的context攻取setting.properties的FileInputStream
                InputStream in = context.getAssets().open("appConfig.properties");
                //方法二：通过class获取setting.properties的FileInputStream
                //InputStream in = PropertiesUtill.class.getResourceAsStream("/assets/  setting.properties "));
                properties.load(in);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }


        Log.i("读取到的配置文件为/",properties.getProperty(key));
       // Log.i("读取到的配置文件为/","0000");
        return properties.getProperty(key);


    }


    //去除视图的所有父视图
    public static void removeParentsView(View view) {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeAllViews();
        }
    }
}
