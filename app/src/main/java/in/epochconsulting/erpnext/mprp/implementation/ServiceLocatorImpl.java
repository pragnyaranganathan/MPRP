package in.epochconsulting.erpnext.mprp.implementation;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by pragnya on 9/3/18.
 */

public class ServiceLocatorImpl  implements ServiceLocater{
    private static ServiceLocatorImpl serviceLocator;

    public static ServiceLocatorImpl getInstance() {
        if (serviceLocator == null) {
            serviceLocator = new ServiceLocatorImpl();
        }
        return serviceLocator;
    }


    @Override
    public void executeGetVolleyRequest(Context context, String url, Type typeObj, Map<String,String> params, Map<String, String> headers, Response.Listener listener, Response.ErrorListener errorListener) {
        VolleyGSONRequest volleyGSONRequest = new VolleyGSONRequest(Request.Method.GET,url,typeObj,params, headers,listener, errorListener);


        VolleyService.getVolleyService(context).addToRequestQueue(volleyGSONRequest);
    }

    @Override
    public void executeGetStringRequest(Context context,String url,  Response.Listener listener, Response.ErrorListener errorListener){
        VolleyStringRequest volleyStringRequest = new VolleyStringRequest(Request.Method.GET, url, listener, errorListener);

        VolleyService.getVolleyService(context).addToRequestQueue(volleyStringRequest);
    }
}
