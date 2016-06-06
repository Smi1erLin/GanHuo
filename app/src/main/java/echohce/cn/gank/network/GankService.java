package echohce.cn.gank.network;

import echohce.cn.gank.model.GankData;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by lin on 2016/6/2.
 */
public interface GankService {
    @GET("api/data/{type}/{num}/{page}")
    Observable<GankData> getGankData(
            @Path("type") String type,
            @Path("num") int num,
            @Path("page") int page
    );
}
