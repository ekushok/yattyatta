package my.ekushok.grief;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import org.afree.chart.AFreeChart;
import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class GriefActivity extends Activity implements View.OnClickListener{
    /** Called when the activity is first created. */
	
	public int sum = 0;
	private ArrayList<String> data;
	private MainStore store;
	private ListView list;
	private GridView grid;
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
	//private Calendar cal;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Viewの指定
        list = (ListView)findViewById(R.id.listView1);
        grid = (GridView)findViewById(R.id.gridView1);
        text = (TextView)findViewById(R.id.sum);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        deleteButton = (Button) findViewById(R.id.button4);
        thisMonthButton = (Button)findViewById(R.id.thisMonthButton);
        prevMonthButton = (Button)findViewById(R.id.prevMonthButton);
        WindowManager windowmanager = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = windowmanager.getDefaultDisplay();
        
		cal = new GregorianCalendar();
        sd = new SumData(this);
        store = new MainStore(this);
        
        text.setHeight((int)(disp.getHeight()*0.3));
        button1.setHeight((int)(disp.getHeight()*0.12));
        button2.setHeight((int)(disp.getHeight()*0.12));
        button3.setHeight((int)(disp.getHeight()*0.12));
        
     // TabHostクラス初期設定        
        TabHost tabs = (TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();
        
        // Tab1 設定
        TabSpec tab1 = tabs.newTabSpec("tab1");
        tab1.setIndicator("ホーム");      // タブに表示する文字列
        tab1.setContent(R.id.tab1); // タブ選択時に表示するビュー
        tabs.addTab(tab1);              // タブホストにタブ追加
        
        // Tab2 設定
        TabSpec tab2 = tabs.newTabSpec("tab2");
        tab2.setIndicator("一覧A");      // タブに表示する文字列
        tab2.setContent(R.id.tab2); // タブ選択時に表示するビュー
        tabs.addTab(tab2);              // タブホストにタブ追加
        
        //Tab3 設定
        TabSpec tab3 = tabs.newTabSpec("tab3");
        tab3.setIndicator("一覧B");      // タブに表示する文字列
        tab3.setContent(R.id.tab3); // タブ選択時に表示するビュー
        tabs.addTab(tab3);
        
        //Tab4 設定
        TabSpec tab4 = tabs.newTabSpec("tab4");
        tab4.setIndicator("グラフ");      // タブに表示する文字列
        tab4.setContent(R.id.tab4); // タブ選択時に表示するビュー
        tabs.addTab(tab4);
        
        // 初期表示設定
        tabs.setCurrentTab(0);
        
        //リストビューとグリッドビューにデータを格納
		ViewList();
		ViewGrid();
		
		//テキストの設定
		text.setText(String.valueOf(sd.sumAll())+"円");

		//ボタンのイベント設定
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
		thisMonthButton.setOnClickListener(this);
		prevMonthButton.setOnClickListener(this);

		
		//AFreeChartの作成
		creater = new ChartCreate(this);

		chartview = (ChartView) findViewById(R.id.chart_view);

		chart = creater.getLineThisMonth(cal.get(Calendar.MONTH)
											,cal.get(Calendar.YEAR));
		chartview.setChart(chart);
    }
    
    //ListViewにアイテムを登録
    private void ViewList(){
    	
        //data = store.loadAll("select _id,timestring,price from log2 ");
        data = store.loadToDay("select _id,timestring,price from log2 ");
    	Collections.reverse(data);
        //ListViewのアイテムとしてrowitem.xmlを定義してある
        ArrayAdapter<String> arrayAdapter
        	= new ArrayAdapter<String>(this,R.layout.rowitem,data);
        list.setAdapter(arrayAdapter);
    }
    //GridViewにアイテムを登録
    private void ViewGrid(){
        data = store.loadToDay("select _id,timestring,price from log2 ");
        Collections.reverse(data);
        
        ArrayAdapter<String> arrayAdapter
        	= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,data);
        grid.setAdapter(arrayAdapter);
    }
    //ボタンが押された時の処理
	public void onClick(View v) {
		if(v == button1){
			store.add(100);
			ViewList();
			ViewGrid();
			text.setText(String.valueOf(sd.sumAll())+"円");
		}
		else if(v == button2){
			store.add(1000);
			ViewList();
			ViewGrid();
			text.setText(String.valueOf(sd.sumAll())+"円");
		}
		else if(v == button3){
			store.add(3000);
			ViewList();
			ViewGrid();
			text.setText(String.valueOf(sd.sumAll())+"円");
		}
		else if(v == deleteButton){
			store.Undo();
			ViewList();
			ViewGrid();
			text.setText(String.valueOf(sd.sumAll())+"円");
		}
		else if(v == thisMonthButton){
			chartview.invalidate();
			chart = creater.getLineThisMonth(cal.get(Calendar.MONTH)
												,cal.get(Calendar.YEAR));
			chartview.setChart(chart);
		}
		else if(v == prevMonthButton){
			chartview.invalidate();
			if(new GregorianCalendar().get(Calendar.MONTH) == 0){
				chart = creater.getLineThisMonth(11,cal.get(Calendar.YEAR)-1);
			}else{
				chart = creater.getLineThisMonth(cal.get(Calendar.MONTH)-1
													,cal.get(Calendar.YEAR));
			}
			chartview.setChart(chart);
		}
	}
}