package echohce.cn.gank.network;

import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lin on 2016/6/2.
 */
public class GankSingleTon {
    private static final String BASE_URL_GANK_IO = "http://gank.io/";

    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static GankService gankService;

    /**
     * 获取GankService单例
     * @return
     */
    public static GankService getGankService() {
        if (gankService == null) {
            synchronized (GankSingleTon.class) {
                if (gankService == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL_GANK_IO)
                            .addCallAdapterFactory(rxJavaCallAdapterFactory)
                            .addConverterFactory(gsonConverterFactory)
                            .build();
                    gankService = retrofit.create(GankService.class);
                }
            }
        }
        return gankService;
    }
}
