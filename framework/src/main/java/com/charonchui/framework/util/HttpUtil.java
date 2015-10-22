package com.charonchui.framework.util;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Utility for http connection.
 * Based on volley.
 *
 * Created by CharonChui
 */
public class HttpUtil {
    private static HttpUtil instance;
    private RequestQueue mQueue;

    private HttpUtil(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }

    public static synchronized HttpUtil getInstance(Context context) {
        if (instance == null) {
            instance = new HttpUtil(context.getApplicationContext());
        }
        return instance;
    }

    public Request sendGetRequest(String url, final HttpListener listener) {
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (listener != null) {
                    listener.onResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    listener.onErrorResponse(error);
                }
            }
        });
        mQueue.add(stringRequest);
        return stringRequest;
    }

    public Request sendPostRequest(String url, final Map<String, String> map, final HttpListener listener) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (listener != null) {
                    listener.onResponse(response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    listener.onErrorResponse(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        mQueue.add(stringRequest);
        return stringRequest;
    }

    public interface HttpListener {

        void onResponse(String response);

        void onErrorResponse(VolleyError error);
    }
}
