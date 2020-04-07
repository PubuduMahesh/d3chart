package com.codenerdz.example.d3chart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codenerdz.example.d3chart.model.ConfirmedModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // save a reference to show the pie chart
    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPieChart();
    }

    public void initPieChart()
    {
        View stub = findViewById(R.id.pie_chart_stub);

        if (stub instanceof ViewStub)
        {
            ((ViewStub)stub).setVisibility(View.VISIBLE);

            webview = (WebView)findViewById(R.id.pie_chart_webview);

            WebSettings webSettings =
                    webview.getSettings();

            webSettings.setJavaScriptEnabled(true);

            webview.setWebChromeClient(
                    new WebChromeClient());

            webview.setWebViewClient(new WebViewClient()
            {
                @Override
                public void onPageFinished(
                        WebView view,
                        String url)
                {

                    // after the HTML page loads,
                    // load the pie chart
                    loadPieChart();
                }
            });

            // note the mapping from  file:///android_asset
            // to Android-D3jsPieChart/assets or
            // Android-D3jsPieChart/app/src/main/assets
            webview.loadUrl("file:///android_asset/" +
                    "html/piechart.html");
            /*webview.loadUrl("file:///android_asset/" +
                    "html/linechart.html");*/
            webSettings.setDomStorageEnabled(true);
        }
    }

    public void loadPieChart()
    {
        slDailyCasesTracker();
        int dataset[] = new int[] {5,10,15,20,35};

        // use java.util.Arrays to format
        // the array as text
        String text = Arrays.toString(dataset);

        // pass the array to the JavaScript function
        //webview.loadUrl("javascript:loadPieChart("+text+")");
    }

    private void slDailyCasesTracker()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://pomber.github.io/covid19/timeseries.json";
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (null != response) {
                            try {

                                JSONArray jsonArraySL = new JSONArray();
                                JSONArray jsonArrayUSA = new JSONArray();
                                jsonArraySL = response.getJSONArray("Sri Lanka");
                                jsonArrayUSA = response.getJSONArray("US");
                                StringBuilder textSL = new StringBuilder();
                                StringBuilder textUSA = new StringBuilder();
                                final List<ConfirmedModel> data = new ArrayList<ConfirmedModel>();
                                textSL.append("[");
                                textUSA.append("[");
                                for(int i=0;i<jsonArraySL.length();i++)
                                {
                                    textSL.append("{\"date\":\""+jsonArraySL.getJSONObject(i).
                                            get("date").toString()+"\",\"confirmed\":\""+jsonArraySL.getJSONObject(i).get("confirmed").toString()
                                            +"\"},");

                                }
                                for(int i=0;i<jsonArrayUSA.length();i++)
                                {
                                    textUSA.append("{\"date\":\""+jsonArrayUSA.getJSONObject(i).
                                            get("date").toString()+"\",\"confirmed\":\""+jsonArrayUSA.getJSONObject(i).get("confirmed").toString()
                                            +"\"},");

                                }

                                textSL.deleteCharAt(textSL.length()-1);
                                textUSA.deleteCharAt(textUSA.length()-1);
                                textSL.append("]");
                                textUSA.append("]");

                                webview.loadUrl("javascript:loadPieChart("+textSL.toString()+","+textUSA.toString()+")");
                                System.out.println();


                            } catch (Exception e) {
                                //e.printStackTrace();
                            }
                        }
                    }
                },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }


}
