package local.hal.st32.android.akanetin;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebViewClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 2016/07/19　追加インポート　音声認識用
 */
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;

/**
 * 2016/09/07 カレンダーよう
 */
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.ArrayList;

/**
 * 2016/09/08 天気よう
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private TextToSpeech tts;
    private static final int REQUEST_CODE = 1000;
    private TextView textView;
    private ImageView imageView;
    private WebView myWebView;
    private ImageView imTenki1;
    private ImageView imTenki2;
    private TextView tvTenkiText;
    private TextView tvTitle;


    /**
     *天気情報のURL
     */
    private static final String WEATHERINFO_URL ="http://weather.livedoor.com/forecast/webservice/json/v1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akane_map_width);
        tts = new TextToSpeech(this,this);

        myWebView = (WebView)findViewById(R.id.webView);
        myWebView.setVisibility(myWebView.INVISIBLE);
        imTenki1 = (ImageView)findViewById(R.id.tenki1);
        imTenki2 = (ImageView)findViewById(R.id.tenki2);
        imTenki1.setVisibility(imTenki1.INVISIBLE);
        imTenki2.setVisibility(imTenki2.INVISIBLE);
        tvTenkiText = (TextView)findViewById(R.id.tenkiText);
        tvTitle = (TextView)findViewById(R.id.tvTitle);



        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        toDay();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

//            textView = (TextView)findViewById(R.id.akaneText);
            imageView = (ImageView)findViewById(R.id.akanetin);
            imageView.setImageResource(R.drawable.basic7);
//            textView.setText("webすんねんな");
            tts.speak("おはようございますマスター", TextToSpeech.QUEUE_FLUSH, null);
        } else {
            System.out.println("Oops!");
        }
    }

    @Override
    protected void onDestroy(){

        if(null != tts){
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void speech(){
        // 音声認識が使えるか確認する
        try {
            // 音声認識の　Intent インスタンス
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

            // 英語
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH.toString() );
            // 日本語
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.JAPAN.toString() );

            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "喋ってや");
            // インテント発行
            startActivityForResult(intent, REQUEST_CODE);
        }
        catch (ActivityNotFoundException e) {
            TextView textView = (TextView)findViewById(R.id.akaneText);
            textView.setText("No Activity " );
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // 認識結果を ArrayList で取得
            ArrayList<String> candidates = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if(candidates.size() > 0) {
                // 認識結果候補で一番有力なものを表示
//                textView.setText( candidates.get(0));
                imageView = (ImageView)findViewById(R.id.akanetin);
                imageView.setImageResource(R.drawable.basic7);
                String str = candidates.get(0);
                akaneFunction(str);
            }
        }
    }

    public void voiceOn(View view){
        speech();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        switch (itemId){

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void TextNone(View view){
        textView = (TextView)findViewById(R.id.akaneText);
        imageView = (ImageView)findViewById(R.id.akanetin);
        textView.setText("");
        imageView.setImageResource(R.drawable.basic1);
    }


    /**
     * 入力された音声データで機能分岐する昨日
     * @param voice
     */
    public void akaneFunction(String voice){

        delete();
        switch (voice){
            case "検索":
                openWeb(voice);
                break;

            case "戻る":
                myWebView.goBack();
                break;

            case "天気":
                RestAccess access = new RestAccess();
                access.execute(WEATHERINFO_URL, "270000");
                break;

            case "予定":
                break;

            default:
                tts.speak("ちゃんと聞き取れませんでした。", TextToSpeech.QUEUE_FLUSH, null);
                break;
        }


    }

    /**
     * 日付を出力するためのクラス
     */
    public void toDay(){
        TextView tvMonth = (TextView)findViewById(R.id.month);
        TextView tvDay = (TextView)findViewById(R.id.day);
        TextView tvWeek = (TextView)findViewById(R.id.week);
        Calendar cal = Calendar.getInstance();
        tvMonth.setText(String.valueOf(cal.get(Calendar.MONTH)+1));
        tvDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        int intweek = cal.get(Calendar.DAY_OF_WEEK);
        String strWeek = "";
        switch(intweek){
            case 1:
                strWeek = "日";
                break;
            case 2:
                strWeek = "月";
                break;
            case 3:
                strWeek = "火";
                break;
            case 4:
                strWeek = "水";
                break;
            case 5:
                strWeek = "木";
                break;
            case 6:
                strWeek = "金";
                break;
            case 7:
                strWeek = "土";
                break;
        }
        tvWeek.setText(strWeek);

    }

    /**
     *　web機能のメンバクラス
     */
    public void openWeb (String voice){
        String search = voice.substring(2);
        myWebView.setVisibility(myWebView.VISIBLE);
        tvTitle.setText("検索");
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("https://www.google.co.jp/?q="+search+"");
        Log.e("URL","https://www.google.co.jp/#q="+search+"");
    }

    /**
     * 天気機能のメンバクラス
     */
    /**
     * 非同期でお天気データを取得するクラス
     */
    private class RestAccess extends AsyncTask<String, Void, String>{
        /**
         * ログに記載するタグ用の文字列
         */
        private static final String DEBUG_TAG = "RestAccess";

        @Override
        public String doInBackground(String... params){
            String urlStr = params[0];
            String id = params[1];

            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";

            try{
                URL url = new URL(urlStr + "?city=" + id);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                is = con.getInputStream();

                result = is2String(is);
            }catch (MalformedURLException ex){
                Log.e(DEBUG_TAG,"URL変換失敗",ex);
            }catch (IOException ex){
                Log.e(DEBUG_TAG,"通信失敗",ex);
            }finally {
                if(con != null) {
                    con.disconnect();
                }
                if (is != null) {
                    try{
                        is.close();
                    }catch (IOException ex){
                        Log.e(DEBUG_TAG,"InputStream解放失敗",ex);
                    }
                }
            }

            return result;
        }

        @Override
        public void onPostExecute(String result){
            String title = "";
            String text = "";
            String dateLabel = "";
            String telop = "";
            try{
                JSONObject rootJSON = new JSONObject(result);
                title = rootJSON.getString("title");
                JSONObject descriptionJSON = rootJSON.getJSONObject("description");
                text = descriptionJSON.getString("text");
                JSONArray forecasts = rootJSON.getJSONArray("forecasts");
                JSONObject forecastNow = forecasts.getJSONObject(0);
                dateLabel = forecastNow.getString("dateLabel");
                telop  = forecastNow.getString("telop");
            }catch (JSONException ex){
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
            }

            String msg = "今日の天気は" + telop + "\n" + text;

            weatherOutPut(telop,msg);

        }

        /**
         * InputStreamオブジェクトを文字列に変換するメソッド
         * 変換文字コードはUTF-8
         * @param is 変換された文字列
         * @return 変換された文字列
         * @throws IOException 変換に失敗したときに発生
         */
        private String is2String(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            char[] b = new char[1024];
            int line;
            while(0<=(line = reader.read(b))){
                sb.append(b,0,line);
            }
            return  sb.toString();
        }
    }

    /**
     * 黒板消しを押した時の処理
     */
    public void delete(View view){
        myWebView = (WebView)findViewById(R.id.webView);
        myWebView.setVisibility(myWebView.INVISIBLE);
        imTenki1 = (ImageView)findViewById(R.id.tenki1);
        imTenki2 = (ImageView)findViewById(R.id.tenki2);
        imTenki1.setVisibility(imTenki1.INVISIBLE);
        imTenki2.setVisibility(imTenki2.INVISIBLE);
        tvTitle.setText("");
        tvTenkiText.setText("");
        tts.stop();
        tts.speak("静かにしておきますね", TextToSpeech.QUEUE_FLUSH, null);
    }

    public void delete(){
        myWebView = (WebView)findViewById(R.id.webView);
        myWebView.setVisibility(myWebView.INVISIBLE);
        imTenki1 = (ImageView)findViewById(R.id.tenki1);
        imTenki2 = (ImageView)findViewById(R.id.tenki2);
        imTenki1.setVisibility(imTenki1.INVISIBLE);
        imTenki2.setVisibility(imTenki2.INVISIBLE);
        tvTitle.setText("");
        tvTenkiText.setText("");
        tts.stop();
        tts.speak("静かにしておきますね", TextToSpeech.QUEUE_FLUSH, null);
    }

    /**
     * 天気を出力するためのクラス
     */
    public void weatherOutPut(String weather, String msg){

        tvTitle.setText("今日の天気");
        /**
         * 天気によって表示する天気画像を変更する
         */
        switch (weather){
            case "晴れ":
                imTenki1.setVisibility(imTenki1.VISIBLE);
                imTenki1.setImageResource(R.drawable.tenki_hare);
                msg += "洗濯日和ですねマスター";
                break;
            case "曇り":
                imTenki1.setVisibility(imTenki1.VISIBLE);
                imTenki1.setImageResource(R.drawable.tenki_kumori);
                break;
            case "雨":
                imTenki1.setVisibility(imTenki1.VISIBLE);
                imTenki1.setImageResource(R.drawable.tenki_ame);
                break;
            case "雪":
                imTenki1.setVisibility(imTenki1.VISIBLE);
                imTenki1.setImageResource(R.drawable.tenki_yuki);
                break;
            case "晴のち曇":
                imTenki1.setVisibility(imTenki1.VISIBLE);
                imTenki2.setVisibility(imTenki2.VISIBLE);
                imTenki1.setImageResource(R.drawable.tenki_hare);
                tvTenkiText.setText("のち");
                imTenki2.setImageResource(R.drawable.tenki_kumori);
                break;
            case "晴のち雨":
                imTenki1.setVisibility(imTenki1.VISIBLE);
                imTenki2.setVisibility(imTenki2.VISIBLE);
                imTenki1.setImageResource(R.drawable.tenki_hare);
                tvTenkiText.setText("のち");
                imTenki2.setImageResource(R.drawable.tenki_ame);
                break;
            case "曇時々晴":
                imTenki1.setVisibility(imTenki1.VISIBLE);
                imTenki2.setVisibility(imTenki2.VISIBLE);
                imTenki1.setImageResource(R.drawable.tenki_kumori);
                tvTenkiText.setText("時々");
                imTenki2.setImageResource(R.drawable.tenki_hare);
                break;
            case "曇時々雨":
                imTenki1.setVisibility(imTenki1.VISIBLE);
                imTenki2.setVisibility(imTenki2.VISIBLE);
                imTenki1.setImageResource(R.drawable.tenki_kumori);
                tvTenkiText.setText("時々");
                imTenki2.setImageResource(R.drawable.tenki_ame);
                break;
            case "曇のち雨":
                imTenki1.setVisibility(imTenki1.VISIBLE);
                imTenki2.setVisibility(imTenki2.VISIBLE);
                imTenki1.setImageResource(R.drawable.tenki_kumori);
                tvTenkiText.setText("のち");
                imTenki2.setImageResource(R.drawable.tenki_ame);
                break;
            case "雨時々曇":
                imTenki1.setVisibility(imTenki1.VISIBLE);
                imTenki2.setVisibility(imTenki2.VISIBLE);
                imTenki1.setImageResource(R.drawable.tenki_ame);
                tvTenkiText.setText("時々");
                imTenki2.setImageResource(R.drawable.tenki_kumori);
                break;
            case "暴風雨":
                imTenki1.setVisibility(imTenki1.VISIBLE);
                imTenki1.setImageResource(R.drawable.tenki_bohu);
                break;
            default:
                msg = "知らないパターンです。新しく登録お願いしますマスター";
                break;

        }
        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);

    }




}
