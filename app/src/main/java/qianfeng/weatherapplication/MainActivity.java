package qianfeng.weatherapplication;



import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {



    private EditText mEt;
    private TextView mTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEt = ((EditText) findViewById(R.id.et_cityname));
        mTv = ((TextView) findViewById(R.id.tv_result));

    }

    public void search_weather(View view) {


            String cityname = mEt.getText().toString();
            new MyAsyncTask().execute(cityname);


    }


    private class MyAsyncTask extends AsyncTask<String,Void,String> {



        @Override
        protected String doInBackground(String... params) {

            String httpUrl =  "http://apis.baidu.com/apistore/weatherservice/cityname";
            String httpArg = null;

            try {
                httpArg = "cityname=" + URLEncoder.encode(params[0],"UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String jsonResult = request(httpUrl,httpArg);  // httpArg = cityname=%E8%9F%6A; // 代表的是城市的名字

            Log.d("qianfeng", "doInBackground: " + jsonResult);


            return jsonResult;


        }


        @Override
        protected void onPostExecute(String s) {  // 这个s字符串是没有经过解析的字符串
            StringBuffer result = new StringBuffer();
            try {
                JSONObject jo = new JSONObject(s);
                JSONObject retData = jo.getJSONObject("retData");
                String city = retData.getString("city");
                String date = retData.getString("date");
                String weather = retData.getString("weather");
                String temp = retData.getString("temp");
                String WD = retData.getString("WD");
                result.append("城市名称:").append(city).append("\n")
                        .append("日期:").append(date).append("\n")
                        .append("天气:").append(weather).append("\n")
                        .append("气温:").append(temp).append("\n")
                        .append("风向:").append(WD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mTv.setText(result.toString());
        }


            public String request(String httpUrl, String httpArg) {
                BufferedReader reader = null;
                String result = null;
                StringBuffer sbf = new StringBuffer();
                httpUrl = httpUrl + "?" + httpArg;

                try {
                    URL url = new URL(httpUrl);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.setRequestMethod("GET");
                    // 填入apikey到HTTP header
                    connection.setRequestProperty("apikey", "cc10d865caaed39beaca8012c6bd2d9e");
                    connection.connect();
                    InputStream is = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String strRead = null;
                    while ((strRead = reader.readLine()) != null) {
                        sbf.append(strRead);
                        sbf.append("\r\n");
                    }
                    reader.close();
                    result = sbf.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
        }



    }

}
