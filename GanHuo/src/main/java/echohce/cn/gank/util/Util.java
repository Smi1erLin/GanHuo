package echohce.cn.gank.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lin on 2016/6/5.
 */
public class Util {
    // 一些简单的常量
    private static final int MIN_A_HOUR = 60;
    private static final int MIN_A_DAY = 60 * 24;
    private static final int MIN_A_MOUTH = 60 * 24 * 30;

    /**
     * 将从gank.io获取的时间格式，解析出自己想要的内容
     * @param time
     * @return
     */
    public static String getFormatTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            long ms = sdf.parse(time).getTime();
            long diff = new Date().getTime() - ms;
            int min = (int) (diff / (1000 * 60));
            if (min < 5) {
                return "刚刚";
            } else if (min < MIN_A_HOUR) {
                return min + "分钟前";
            } else if (min < MIN_A_DAY) {
                return min / 60 + "小时前";
            } else if (min < MIN_A_MOUTH) {
                return min / (60 * 24) + "天前";
            } else return "Long long ago";
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return "滴滴答";
    }

    /**
     * @param context Context上下文环境
     * @param text    要保存的字符内容
     */
    public static void shareText(Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); // 纯文本
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, "干货集中营"));
    }

    /**
     * @param context Context上下文环境
     * @param uri     图片文件对应uri
     */
    public static void shareImage(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*"); // 可以传递文字也可以传递图片
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(intent, "干货集中营"));
    }

    /**
     * @param context   Context上下文环境
     * @param desc      图片描述
     * @param bitmap    Bitmap对象
     * @param imageView ImageView ∵ SnackBar需要View
     * @param isSaved   是否是保存，因为分享的时候也会需要调用这个方法获取uri。
     * @return
     */
    public static Uri saveImage(Context context, String desc, Bitmap bitmap, ImageView imageView, boolean isSaved) {
        String imgDir = Environment.getExternalStorageDirectory().getPath() + "/GanHuo";
        String fileName = desc + ".png";

        File fileDir = new File(imgDir);
        if (!fileDir.exists())
            fileDir.mkdir();

        File imageFile = new File(fileDir, fileName);
        if (!imageFile.exists()){
            try {
                FileOutputStream fos = new FileOutputStream(imageFile);
                boolean compress = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                if (compress)
                    showSnackBar(imageView, "福利图已经保存");
                else showSnackBar(imageView, "福利图保存失败");
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (isSaved) showSnackBar(imageView, "文件已存在~" );

        Uri uri = Uri.fromFile(imageFile);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        return uri;
    }

    /**
     * Function: showSnackBar
     * @param imageView
     * @param s
     */
    private static void showSnackBar(ImageView imageView,String s) {
        final Snackbar sb = Snackbar.make(imageView, s, Snackbar.LENGTH_SHORT);
        sb.setAction("知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.dismiss();
            }
        });
        sb.show();
    }
}
