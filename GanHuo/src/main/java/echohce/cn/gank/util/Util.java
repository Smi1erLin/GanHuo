package echohce.cn.gank.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lin on 2016/6/5.
 */
public class Util {
    private static final int MIN_A_HOUR = 60;
    private static final int MIN_A_DAY = 60 * 24;
    private static final int MIN_A_MOUTH = 60 * 24 * 30;

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

    public static void shareText(Context context, String text)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); // 纯文本
        intent.putExtra(Intent.EXTRA_TEXT,text);
        context.startActivity(Intent.createChooser(intent,"干货集中营"));
    }
}
