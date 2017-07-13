package com.sosokan.android.networking;

import com.sosokan.android.models.AdvertiseApi;
import com.sosokan.android.models.Banner;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.models.CommentNew;
import com.sosokan.android.models.ContentType;
import com.sosokan.android.models.FavoriteNew;
import com.sosokan.android.models.Flag;
import com.sosokan.android.models.ImageHeaderApi;
import com.sosokan.android.models.Status;
import com.sosokan.android.models.Token;
import com.sosokan.android.models.UserApi;
import com.sosokan.android.models.VideoSplash;
import com.sosokan.android.mvp.advertise.AdvertiseResponse;
import com.sosokan.android.mvp.flagchoice.FlagChoiceResponse;
import com.sosokan.android.mvp.userProfile.UserProfileResponse;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by macintosh on 3/15/17.
 */

public interface NetworkService {

    @GET("api/categories/")
    Observable<List<CategoryNew>> getListCategory();


    @GET("api/userprofiles/")
    Observable<UserProfileResponse> getUserProfile();

    @GET("api/userprofiles/")
    Observable<UserProfileResponse> getUserProfile(@Query("legacy_id") String legacyId);

    @GET("api/users/")
    Observable<UserApi> getUser();

    @GET("api/splashes/")
    Observable<List<VideoSplash>> getListVideoSplash();

    @GET("api/ads/")
    Observable<AdvertiseResponse> getListAdvertise();

    @GET("api/ads/")
    Observable<AdvertiseResponse> getListAdvertise(@Query("categoryId") String categoryId);

    @GET("api/ads/")
    Observable<AdvertiseApi> getAdvertise(@Query("legacy_id") String legacyId);

    @GET("api/ads/{id}")
    Observable<AdvertiseApi> getAdvertise(@Path("id") int id);


    @GET("api/adimages/")
    Observable<List<ImageHeaderApi>> getListAdvertiseImages();


    @GET("api/favorites/")
    Observable<List<FavoriteNew>> getListFavorite();

    /*@GET("api/banners/")
    Observable<List<Banner>> getListBanner();
*/

    @GET("api/banners/")
    Observable<List<Banner>> getListBanner( @QueryMap Map<String, String> params);

    @GET("api/comments/")
    Observable<List<CommentNew>> getListComment();

    @GET("api/flags/")
    Observable<List<Flag>> getListFlag();

    @GET("api/flagchoices/")
    Observable<FlagChoiceResponse> getListFlagChoice();

    @GET("api/contenttypes/")
    Observable<List<ContentType>> getListContentType();

    @GET("api/statuses/")
    Observable<List<Status>> getListStatus();

    @GET("api/tokens/")
    Observable<List<Token>> getListToken();


}
