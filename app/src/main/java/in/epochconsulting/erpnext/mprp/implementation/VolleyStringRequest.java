package in.epochconsulting.erpnext.mprp.implementation;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

/**
 * Created by pragnya on 9/3/18.
 */

public class VolleyStringRequest extends StringRequest {
    private final Response.Listener<String> listener;
    final String mUrl;
    final int mMethod;

    public VolleyStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(url, listener, errorListener);
        this.listener = listener;
        this.mUrl = url;
        this.mMethod = method;


    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {

            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));


            return  Response.success
                    (
                            json,
                            HttpHeaderParser.parseCacheHeaders(response)
                    );
        } catch (UnsupportedEncodingException e) {

            return Response.error(new ParseError(e));

        } catch (JsonSyntaxException e) {

            return Response.error(new ParseError(e));
        }
    }


    @Override
    protected void deliverResponse(String response) {

        listener.onResponse(response);

    }

}
