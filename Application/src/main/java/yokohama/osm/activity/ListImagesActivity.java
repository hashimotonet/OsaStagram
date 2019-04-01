package yokohama.osm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.camera2basic.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import yokohama.osm.adapter.GridAdapter;
import yokohama.osm.util.BaseUtil;

public class ListImagesActivity extends AppCompatActivity {

    private static String[] _URLs;

    private String _id;

    private static final String[] photos = {
            "http://52.68.126.14:8080/PhotoGallery/hashimoto/1.jpg",
            "http://52.68.126.14:8080/PhotoGallery/hashimoto/2.jpg",
            "http://52.68.126.14:8080/PhotoGallery/hashimoto/3.jpg",
            "http://52.68.126.14:8080/PhotoGallery/hashimoto/4.jpg",
            "http://52.68.126.14:8080/PhotoGallery/hashimoto/5.jpg",
            "http://52.68.126.14:8080/PhotoGallery/hashimoto/6.jpg",
            "http://52.68.126.14:8080/PhotoGallery/hashimoto/7.jpg",
            "http://52.68.126.14:8080/PhotoGallery/hashimoto/8.jpg",
            "http://52.68.126.14:8080/PhotoGallery/hashimoto/9.jpg",
            "http://52.68.126.14:8080/PhotoGallery/hashimoto/10.jpg",
            "http://52.68.126.14:8080/PhotoGallery/hashimoto/11.jpg",
            "http://52.68.126.14:8080/PhotoGallery/hashimoto/12.jpg",
            "http://52.68.126.14:8080/PhotoGallery/hashimoto/13.jpg",
            "http://52.68.126.14:8080/PhotoGallery/hashimoto/14.jpg"
            };

    private String photo;

    public ListImagesActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            Log.i("IMPORTANT","onCreate()");

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // IDに紐づく画像イメージURLをすべて取得。
            Intent intent = getIntent();
            String id = intent.getStringExtra("id");
            String status = intent.getStringExtra("status");
            Log.i("IMPORTANT", "id = " + id + " : status = " + status) ;

            final ListImages task = new ListImages();
            task.execute(id, status);
        } catch(Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * インターネット接続インナークラス
     */
    private class ListImages extends AsyncTask<String, Void, String> {

        /**
         * コンストラクタ
         */
        ListImages() {
            Log.i("IMPORTANT","ListImages Constructor.");
        }

        @Override
        public String doInBackground(String... params) {

            Log.i("IMPORTANT","ListImages#doInBackground()");

            //可変長引数でPOSTパラメータ書式生成
            String queryString = "id=" + params[0] + "&status=" + params[1];
            //String queryString = "id=hashimoto";
            //IDと画像データを使って接続URL文字列を作成。
            String urlStr = "http://52.68.126.14:8080/PhotoGallery/ListImages";

            //要求受信結果である応答を格納。
            String result = "";

            //http接続を行うHttpURLConnectionオブジェクトを宣言。finallyで確実に解放するためにtry外で宣言。
            HttpURLConnection con = null;

            //http接続のレスポンスデータとして取得するInputStreamオブジェクトを宣言。同じくtry外で宣言。
            InputStream is = null;

            try {
                //URLオブジェクトを生成。
                URL url = new URL(urlStr);

                //URLオブジェクトからHttpURLConnectionオブジェクトを取得。
                con = (HttpURLConnection) url.openConnection();

                //http接続メソッドを設定。
                con.setRequestMethod("POST");

                // データを書き込む
                con.setDoOutput(true);

                // 応答取得
                con.setDoInput(true);

                // ヘッダ設定
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                con.setUseCaches(false);

                //接続。
                con.connect();

                // POSTデータ送信処理
                OutputStream out = null;

                try {
                    out = con.getOutputStream();
                    out.write(queryString.getBytes("UTF-8"));
                    out.flush();
                    Log.d("IMPORTANT", "flush");
                } catch (IOException e) {
                    // POST送信エラー
                    e.printStackTrace();
                    result = "POST送信エラー";
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }

                // 接続ステータスコード取得
                //
                int status = con.getResponseCode();

                Log.d("IMPORTANT", "status = " + status);

                //HttpURLConnectionオブジェクトからレスポンスデータを取得。
                is = con.getInputStream();

                //レスポンスデータであるInputStreamオブジェクトを文字列に変換。
                result = convertInputStreamToString(is);

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                //HttpURLConnectionオブジェクトがnullでないなら解放。
                if (con != null) {
                    con.disconnect();
                }
                //InputStreamオブジェクトがnullでないなら解放。
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            Log.d("IMPORTANT", result);

            //サーバ応答文字列を返す。
            return result;
        }

        private String[] page2UrlStrings(String pageFace) {
            String result[] = null;
            result = pageFace.split("\n");
            return result;
        }

        @Override
        public void onPostExecute(String result) {

            Log.i("IMPORTANT","onPostExecute()");

            String success = result;
            String message = "";
            String[] lines = result.split("\n");
            int length = lines.length - 1;
            String[] url = new String[length];

            List<String> array = new ArrayList<String>();
            for (String line : lines) {
                if (!line.startsWith("http://")) {
                    continue;
                }
                array.add(line);
            }

            array = BaseUtil.sort(array);

            int index = 0;
            for (String line : array) {
                url[index] = line;
                //Log.d("IMPORTANT", "line = " + line);
                index++;
            }

            // GridViewのインスタンスを生成
            GridView gridview = findViewById(R.id.gridview);

            // BaseAdapter を継承したGridAdapterのインスタンスを生成
            GridAdapter adapter = new GridAdapter(
                    getApplicationContext(),
                    R.layout.grid_items,
                    url);

            // gridViewにadapterをセット
            gridview.setAdapter(adapter);

            //リスト項目が選択された時のイベントを追加
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    photo = url[position];
                    String msg = position + "番目のアイテムがクリックされました";
                    //msg += msg + " \n " + photo;
                    //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    //
                    // インテント、URLを引数に、一画面イメージの描画アクティビティを表示する。
                    //
                    Intent intent = new Intent(ListImagesActivity.this, SpreadImageActivity.class);
                    loadSpreadImageActivity(intent, photo);
                }
            });
//            // 一覧画面取得完了を出力。
//            if (success.startsWith("success")) {
//                message = "一覧取得が完了しました！";
//            } else {
//                message = "一覧取得が失敗しました。";
//            }
//
//            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

        }

        private void loadSpreadImageActivity(Intent intent, String url) {
            intent.putExtra("url", url);
            startActivity(intent);
        }

        public String convertInputStreamToString(InputStream is) throws IOException {
            InputStreamReader reader = new InputStreamReader(is);
            StringBuilder builder = new StringBuilder();
            char[] buf = new char[1024];
            int numRead;
            while (0 <= (numRead = reader.read(buf))) {
                builder.append(buf, 0, numRead);
                //Log.d("IMPORTANT", "builder.toString() = " + builder.toString());
            }
            return builder.toString();
        }
    }
}