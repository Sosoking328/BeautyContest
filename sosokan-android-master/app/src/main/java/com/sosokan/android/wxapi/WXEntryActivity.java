package com.sosokan.android.wxapi;

/**
 * Created by AnhZin on 12/29/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.sosokan.android.models.WechatAuth;
import com.sosokan.android.ui.activity.PrefManager;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
/*import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;*/
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import com.tencent.mm.sdk.openapi.WXAPIFactory;
/*import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;*/

/**
 * Activity with no view to handle any responses or requests from WeChat. This activity can
 * never be moved or renamed or WeChat authentication won't work. Also notice that this
 * activity is being exported in the manifest. Lots of examples shows the WXEntryActivity
 * combined with an activity doing other things related to your app. However, this seemed like a poor
 * separation of functionality as well as something that's going to get ugly if you want interaction
 * with WeChat beyond authentication. Treating this activity as more of a broadcast listener keeps
 * things simple and detached from the rest of your application.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    public static final String APP_ID = "wx79982ec7495cf6b0";
    public static final String APP_SEC = "61355a79aeead68b6d306b12e703aa36";
    public static String token;
    public static WechatAuth wechatAuth;
    private IWXAPI api;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle any communication from WeChat and then terminate activity. This class must be an activity
        // or the communication will not be received from WeChat.
        api = WXAPIFactory.createWXAPI(this, APP_ID, false);

        api.handleIntent(getIntent(), this);

        finish();
    }

    /**
     * Called when WeChat is initiating a request to your application. This is not used for
     * authentication.
     *
     * @param req
     */
    @Override
    public void onReq(BaseReq req) {
    }

    /**
     * Called when WeChat is responding to a request this app initiated. Invoked by WeChat after
     * authorization has been given by the user.
     *
     * @param resp
     */
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                try {
                    SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                    WXEntryActivity.token = sendResp.token;

                    WechatAuth wechatAu = new WechatAuth();
                    wechatAu.setCode(sendResp.token);
                    this.wechatAuth = wechatAu;
                    PrefManager prefManager = new PrefManager(getApplicationContext());
                    if(prefManager != null)
                    {
                        prefManager.setWechatAuth(wechatAu);
                    }
                   /* Toast.makeText(this, "code = " + ((SendAuth.Resp) resp).token,
                            Toast.LENGTH_SHORT).show();*/
                } catch (Exception e) {
                    Toast.makeText(this, "Exception while parsing token", Toast.LENGTH_LONG).show();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Toast.makeText(this, "User canceled the request", Toast.LENGTH_LONG).show();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Toast.makeText(this, "User denied the request", Toast.LENGTH_LONG).show();
                break;
        }

    }

}