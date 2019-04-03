package yokohama.osm.adapter;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yokohama.osm.R;

import static android.content.Context.WINDOW_SERVICE;

/**
 * グリッド表示をつかさどるアダプタクラス。
 * BaseAdapterを継承する。
 */
public class GridAdapter extends BaseAdapter {

    /**
     * コンテキスト
     */
    private Context context;

    /**
     * インフレィター
     */
    private LayoutInflater inflater;

    /**
     * レイアウトID
     */
    private int layoutId;

    /**
     * 画像イメージのURLを持つリスト
     */
    private List<String> imageList = new ArrayList<>();

    /**
     * 実画像の半分のサイズの幅
     */
    private int ScreenWidthHalf = 0;

    /**
     * 実画像の半分のサイズの高さ
     */
    private int ScreenHeightHalf = 0;

    /**
     * 唯一のコンストラクタ。
     * このコンストラクタのみにより、当アダプタは
     * 起動を行う。
     *
     * @param context
     * @param layoutId
     * @param iList
     */
    public GridAdapter(Context context, int layoutId, String[] iList) {

        // スーパークラスのデフォルトコンストラクタ呼び出し。
        super();

        // インスタンス変数群に各引数の値をセット。
        this.context = context;
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;

        // String配列の変数を、List<String>型にキャストする。
        Collections.addAll(imageList, iList);

        // 画面の縦長、横幅の半分を計算
        WindowManager wm = (WindowManager)
                context.getSystemService(WINDOW_SERVICE);

        // ウィンドウマネージャがnullでなければ
        if(wm != null){
            // ウィンドウマネージャよりデフォルトディスプレイを取得
            Display disp = wm.getDefaultDisplay();

            // ポイントクラスのインスタンスを生成
            Point size = new Point();

            // ディスプレイをのサイズをポイントとして取得
            disp.getSize(size);

            // 幅を取得
            int screenWidth = size.x;

            // 高さを取得
            int screenHeight = size.y;

            // 高さと幅について二分の一をセット
            ScreenWidthHalf = screenWidth/2;
            ScreenHeightHalf = screenHeight/2;

            // ログ出力する。
            Log.d("debug","ScreenWidthHalf="+ScreenWidthHalf);
            Log.d("debug","ScreenHeightHalf="+ScreenHeightHalf);
        }

    }

    /**
     * 画面表示時に実行されるメソッド。
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        // 引数のViewがnullであるか？
        if (convertView == null)
        { // 真の場合
            view = inflater.inflate(layoutId, parent, false);
        }
        else
            { // 偽の場合
            view =  convertView;
        }

        // イメージビューを取得する
        ImageView img = view.findViewById(R.id.image_view);
        // イメージビューにスケールタイプを設定する
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);

        // Picassoフレームワークによる画像のロードとリサイズと描画の処理
        Picasso.get()
                .load(addUrl(position))
                .resize(ScreenWidthHalf, ScreenHeightHalf)
                .into(img);

        // イメージビューを返却する
        return view;
    }

    /**
     * ネットワークアクセスするURLを設定する
     *
     * @param number
     * @return
     */
    private String addUrl(int number){
        // ログ出力する。
        Log.d("IMPORTANT", "imageList.get(" + number + ")" + imageList.get(number));

        // 引数の数値で指定された画像URLを返却する
        return imageList.get(number);
    }

    /**
     * オーバーライドスタブメソッド
     *
     * @return
     */
    @Override
    public int getCount() {
        // 全要素の数を返す
        return imageList.size();
    }

    /**
     * imageListに引数のインデックスを指定し、
     * 要素である画像イメージURLを返します。
     *
     * @param position
     * @return
     */
    @Override
    public String getItem(int position) {
        return imageList.get(position);
    }

    /**
     * オーバーライドスタブメソッド
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }
}