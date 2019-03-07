package in.epochconsulting.erpnext.mprp.implementation;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;


import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static java.net.Proxy.Type.HTTP;


/**
 * Created by pragnya on 9/3/18.
 */

public class VolleyGSONRequest<T> extends Request<T> {
    private final Gson gson;
    private final Type type;
    private final Response.Listener<T> listener;
    private Map<String, String> mHeaders;
    private Map<String, String> mParams;
    private String mUrl;
    private int mMethod;

    public VolleyGSONRequest(int method, String url, Type type, Map<String , String>  params,
                             Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener) {

        super(method, url, errorListener);
        this.type = type;
        gson = new Gson();
        this.listener = listener;
        mHeaders =headers;
        mParams = params;
        this.mUrl = url;
        this.mMethod = method;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }
    @Override
    public Map<String, String> getParams() throws AuthFailureError{
        return mParams !=null? mParams:super.getParams();

    }

    @Override
    public String getUrl() {
        if(mMethod == Request.Method.GET && mParams != null) {

            StringBuilder stringBuilder = new StringBuilder(mUrl);
            Iterator<Map.Entry<String, String>> iterator = mParams.entrySet().iterator();
            int i = 1;
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if (i == 1) {
                    stringBuilder.append("?" + entry.getKey() + "=" + entry.getValue());
                } else {
                    stringBuilder.append("&" + entry.getKey() + "=" + entry.getValue());
                }
                iterator.remove(); // avoids a ConcurrentModificationException
                i++;
            }

            //added this to take care of white spaces in my url..on 26th Oct 2017
            mUrl = stringBuilder.toString().replace(" ","%20");

        }
        return mUrl;
    }
    protected static final String TYPE_UTF8_CHARSET = "charset=UTF-8";
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        try {

            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));


            return (Response<T>) Response.success
                    (
                            gson.fromJson(json, type),
                            HttpHeaderParser.parseCacheHeaders(response)
                    );
        } catch (UnsupportedEncodingException e) {

            return Response.error(new ParseError(e));

        } catch (JsonSyntaxException e) {

            return Response.error(new ParseError(e));
        }


    }

    @Override
    protected void deliverResponse(T response) {

        listener.onResponse(response);
    }

    //This should be the error response's parse network response method
    /*@Override
     protected static final String TYPE_UTF8_CHARSET = "charset=UTF-8";
    protected Response<String> parseNetworkResponse(
            NetworkResponse response) {
        try {
            String type = response.headers.get(HTTP.CONTENT_TYPE);
            if (type == null) {
                Log.d(LOG_TAG, "content type was null");
                type = TYPE_UTF8_CHARSET;
                response.headers.put(HTTP.CONTENT_TYPE, type);
            } else if (!type.contains("UTF-8")) {
                Log.d(LOG_TAG, "content type had UTF-8 missing");
                type += ";" + TYPE_UTF8_CHARSET;
                response.headers.put(HTTP.CONTENT_TYPE, type);
            }
        } catch (Exception e) {
            //print stacktrace e.g.
        }
        return super.parseNetworkResponse(response);
    }*/
}
