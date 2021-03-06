package my.ekushok.grief;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.afree.chart.AFreeChart;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class GriefActivity extends Activity implements View.OnClickListener {
	/** Called when the activity is first created. */
	private final int REQUEST_CODE = 0;
	public int sum = 0;
	private ArrayList<String> data;
	private MainStore store;
	private ListView list;
	private TextView text;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button deleteButton;
	private Button thisMonthButton;
	private Button prevMonthButton;
	private ChartCreate creater;
	private AFreeChart chart;
	private ChartView chartview;
	private SumData sd;
	private Calendar cal;
	private int button1Price;
	private int button2Price;
	private int button3Price;
	private String[] buttonTitleArray = {	"缶ジュース買っちゃった",
											"ランチ贅沢しちゃった",
											"飲み会行っちゃった",
											"タバコ買っちゃった"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//margeTest
		// Viewの指定
		list = (ListView) findViewById(R.id.listView1);
		text = (TextView) findViewById(R.id.sum);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		deleteButton = (Button) findViewById(R.id.button4);
		thisMonthButton = (Button) findViewById(R.id.thisMonthButton);
		prevMonthButton = (Button) findViewById(R.id.prevMonthButton);
		WindowManager windowmanager = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display disp = windowmanager.getDefaultDisplay();

		// インスタンス生成
		cal = new GregorianCalendar();
		sd = new SumData(this);
		store = new MainStore(this);

		// ボタン等の大きさを指定
		text.setHeight((int) (disp.getHeight() * 0.3));
		button1.setHeight((int) (disp.getHeight() * 0.12));
		button2.setHeight((int) (disp.getHeight() * 0.12));
		button3.setHeight((int) (disp.getHeight() * 0.12));
		button3.setHeight((int) (disp.getHeight() * 0.12));

		// TabHostクラス初期設定
		TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
		tabs.setup();

		// Tab1 設定
		TabSpec tab1 = tabs.newTabSpec("tab1");
		tab1.setIndicator("ホーム"); // タブに表示する文字列
		tab1.setContent(R.id.tab1); // タブ選択時に表示するビュー
		tabs.addTab(tab1); // タブホストにタブ追加

		// Tab2 設定
		TabSpec tab2 = tabs.newTabSpec("tab2");
		tab2.setIndicator("一覧"); // タブに表示する文字列
		tab2.setContent(R.id.tab2); // タブ選択時に表示するビュー
		tabs.addTab(tab2); // タブホストにタブ追加

		// Tab4 設定
		TabSpec tab4 = tabs.newTabSpec("tab4");
		tab4.setIndicator("グラフ"); // タブに表示する文字列
		tab4.setContent(R.id.tab4); // タブ選択時に表示するビュー
		tabs.addTab(tab4);

		// 初期表示設定
		tabs.setCurrentTab(0);

		// リストビューとグリッドビューにデータを格納
		ViewList();

		// テキストの設定
		text.setText(String.valueOf(sd.sumAll()) + "円");

		// ボタンのイベント設定
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
		thisMonthButton.setOnClickListener(this);
		prevMonthButton.setOnClickListener(this);

		// AFreeChartの作成
		creater = new ChartCreate(this);

		chartview = (ChartView) findViewById(R.id.chart_view);

		chart = creater.getBarThisMonth(cal.get(Calendar.MONTH),
				cal.get(Calendar.YEAR));
		chartview.setChart(chart);
		
		//設定の読み込み
		readPreferences();

	}

	// オプションメニューの生成
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 10, 0, "設定");
		return true;
	}

	// オプションメニューが押されたら呼び出される。
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		switch (item.getItemId()) {
		case 10:
			Intent intent = new Intent(this, MyPreferencesActivity.class);
			startActivityForResult(intent, REQUEST_CODE);
			return true;
		}
		return false;
	}
	//設定画面から戻ったら
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == REQUEST_CODE) {
			readPreferences();
		}
	}
	//設定内容の読み込み
	private void readPreferences() {
		        SharedPreferences preferences = PreferenceManager
		                .getDefaultSharedPreferences(this);
		        
		        button1.setText(buttonTitleArray[Integer.parseInt(preferences.getString("button1", "0"))]);
		        button1Price = Integer.parseInt(preferences.getString("p_button1", "100"));
		        button2.setText(buttonTitleArray[Integer.parseInt(preferences.getString("button2", "1"))]);
		        button2Price = Integer.parseInt(preferences.getString("p_button2", "1000"));
		        button3.setText(buttonTitleArray[Integer.parseInt(preferences.getString("button3", "2"))]);
		        button3Price = Integer.parseInt(preferences.getString("p_button3", "4000"));
	}
	

	// ListViewにアイテムを登録
	private void ViewList() {
		SimpleAdapter adapter = new SimpleAdapter(this, getListData(),
				R.layout.simplelist, new String[] { "no", "name" }, new int[] {
						R.id.price, R.id.date });
		list.setAdapter(adapter);
	}

	// マップをリストにセット
	private List<Map<String, String>> getListData() {
		List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
		data = store.loadToDay("select _id,timestring,price from log2 ");
		Collections.reverse(data);
		for (int i = 0; i < data.size(); i += 2) {
			listData.add(getMapData(new String[][] { { "no", data.get(i) },
					{ "name", data.get(i + 1) } }));
		}
		return listData;
	}

	// マップデータを取得
	private Map<String, String> getMapData(String[][] values) {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < values.length; i++) {
			map.put(values[i][0], values[i][1]);
		}

		return map;
	}

	// ボタンが押された時の処理
	public void onClick(View v) {
		if (v == button1) {
			// buttonテーブルからそのボタンの価格を取得後、log2テーブルに挿入
			chartview.invalidate();
			store.add(button1Price, 1);
			ViewList();
			text.setText(String.valueOf(sd.sumAll()) + "円");
		} else if (v == button2) {
			chartview.invalidate();
			store.add(button2Price, 2);
			ViewList();
			text.setText(String.valueOf(sd.sumAll()) + "円");
		} else if (v == button3) {
			chartview.invalidate();
			store.add(button3Price, 3);
			ViewList();
			text.setText(String.valueOf(sd.sumAll()) + "円");
		} else if (v == deleteButton) {
			// ひとつ取り消す
			chartview.invalidate();
			store.Undo();
			ViewList();
			text.setText(String.valueOf(sd.sumAll()) + "円");
		} else if (v == thisMonthButton) {
			// 今月のグラフ取得
			chartview.invalidate();
			chart = creater.getBarThisMonth(cal.get(Calendar.MONTH),
					cal.get(Calendar.YEAR));
			chartview.setChart(chart);
		} else if (v == prevMonthButton) {
			// 先月のグラフ取得。先月が12月の場合とそれ以外の場合で西暦が変わるので場合分け。
			chartview.invalidate();
			if (new GregorianCalendar().get(Calendar.MONTH) == 0) {
				chart = creater.getBarThisMonth(11, cal.get(Calendar.YEAR) - 1);
			} else {
				chart = creater.getBarThisMonth(cal.get(Calendar.MONTH) - 1,
						cal.get(Calendar.YEAR));
			}
			chartview.setChart(chart);
		}
	}
}