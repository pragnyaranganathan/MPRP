package in.epochconsulting.erpnext.mprp.implementation;

import android.content.Context;

import com.android.volley.Response;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by pragnya on 9/3/18.
 */

public interface ServiceLocater {
    void executeGetVolleyRequest(Context context, String url, Type typeObj, Map<String,String> params, Map<String, String> headers, Response.Listener listener, Response.ErrorListener errorListener);
    void executeGetStringRequest(Context context, String url,  Response.Listener listener, Response.ErrorListener errorListener);
}
