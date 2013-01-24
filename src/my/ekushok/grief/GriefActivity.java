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
	private String[] buttonTitleArray = {	"�ʃW���[�X�����������",
											"�����`�ґ򂵂������",
											"���݉�s���������",
											"�^�o�R�����������"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//margeTest
		// View�̎w��
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

		// �C���X�^���X����
		cal = new GregorianCalendar();
		sd = new SumData(this);
		store = new MainStore(this);

		// �{�^�����̑傫�����w��
		text.setHeight((int) (disp.getHeight() * 0.3));
		button1.setHeight((int) (disp.getHeight() * 0.12));
		button2.setHeight((int) (disp.getHeight() * 0.12));
		button3.setHeight((int) (disp.getHeight() * 0.12));
		button3.setHeight((int) (disp.getHeight() * 0.12));

		// TabHost�N���X�����ݒ�
		TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
		tabs.setup();

		// Tab1 �ݒ�
		TabSpec tab1 = tabs.newTabSpec("tab1");
		tab1.setIndicator("�z�[��"); // �^�u�ɕ\�����镶����
		tab1.setContent(R.id.tab1); // �^�u�I�����ɕ\������r���[
		tabs.addTab(tab1); // �^�u�z�X�g�Ƀ^�u�ǉ�

		// Tab2 �ݒ�
		TabSpec tab2 = tabs.newTabSpec("tab2");
		tab2.setIndicator("�ꗗ"); // �^�u�ɕ\�����镶����
		tab2.setContent(R.id.tab2); // �^�u�I�����ɕ\������r���[
		tabs.addTab(tab2); // �^�u�z�X�g�Ƀ^�u�ǉ�

		// Tab4 �ݒ�
		TabSpec tab4 = tabs.newTabSpec("tab4");
		tab4.setIndicator("�O���t"); // �^�u�ɕ\�����镶����
		tab4.setContent(R.id.tab4); // �^�u�I�����ɕ\������r���[
		tabs.addTab(tab4);

		// �����\���ݒ�
		tabs.setCurrentTab(0);

		// ���X�g�r���[�ƃO���b�h�r���[�Ƀf�[�^���i�[
		ViewList();

		// �e�L�X�g�̐ݒ�
		text.setText(String.valueOf(sd.sumAll()) + "�~");

		// �{�^���̃C�x���g�ݒ�
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
		thisMonthButton.setOnClickListener(this);
		prevMonthButton.setOnClickListener(this);

		// AFreeChart�̍쐬
		creater = new ChartCreate(this);

		chartview = (ChartView) findViewById(R.id.chart_view);

		chart = creater.getBarThisMonth(cal.get(Calendar.MONTH),
				cal.get(Calendar.YEAR));
		chartview.setChart(chart);
		
		//�ݒ�̓ǂݍ���
		readPreferences();

	}

	// �I�v�V�������j���[�̐���
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 10, 0, "�ݒ�");
		return true;
	}

	// �I�v�V�������j���[�������ꂽ��Ăяo�����B
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
	//�ݒ��ʂ���߂�����
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == REQUEST_CODE) {
			readPreferences();
		}
	}
	//�ݒ���e�̓ǂݍ���
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
	

	// ListView�ɃA�C�e����o�^
	private void ViewList() {
		SimpleAdapter adapter = new SimpleAdapter(this, getListData(),
				R.layout.simplelist, new String[] { "no", "name" }, new int[] {
						R.id.price, R.id.date });
		list.setAdapter(adapter);
	}

	// �}�b�v�����X�g�ɃZ�b�g
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

	// �}�b�v�f�[�^���擾
	private Map<String, String> getMapData(String[][] values) {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < values.length; i++) {
			map.put(values[i][0], values[i][1]);
		}

		return map;
	}

	// �{�^���������ꂽ���̏���
	public void onClick(View v) {
		if (v == button1) {
			// button�e�[�u�����炻�̃{�^���̉��i���擾��Alog2�e�[�u���ɑ}��
			chartview.invalidate();
			store.add(button1Price, 1);
			ViewList();
			text.setText(String.valueOf(sd.sumAll()) + "�~");
		} else if (v == button2) {
			chartview.invalidate();
			store.add(button2Price, 2);
			ViewList();
			text.setText(String.valueOf(sd.sumAll()) + "�~");
		} else if (v == button3) {
			chartview.invalidate();
			store.add(button3Price, 3);
			ViewList();
			text.setText(String.valueOf(sd.sumAll()) + "�~");
		} else if (v == deleteButton) {
			// �ЂƂ�����
			chartview.invalidate();
			store.Undo();
			ViewList();
			text.setText(String.valueOf(sd.sumAll()) + "�~");
		} else if (v == thisMonthButton) {
			// �����̃O���t�擾
			chartview.invalidate();
			chart = creater.getBarThisMonth(cal.get(Calendar.MONTH),
					cal.get(Calendar.YEAR));
			chartview.setChart(chart);
		} else if (v == prevMonthButton) {
			// �挎�̃O���t�擾�B�挎��12���̏ꍇ�Ƃ���ȊO�̏ꍇ�Ő���ς��̂ŏꍇ�����B
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