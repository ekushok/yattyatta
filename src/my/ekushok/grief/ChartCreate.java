package my.ekushok.grief;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.plot.PlotOrientation;
import org.afree.data.category.DefaultCategoryDataset;
import android.content.Context;

public class ChartCreate {

	private MainStore store;

	ChartCreate(Context context){
		store = new MainStore(context);
	}
	
	AFreeChart getLineThisMonth(int month,int year){
		 //グラフにするデータの作成
		ArrayList<String> data = store.loadAll("select _id,timestring,price from log2");
        ArrayList<Calendar> time = new ArrayList<Calendar>();
        ArrayList<String> price = new ArrayList<String>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		//データを日付と価格に分ける
        for(int i=0;i<data.size();i++){
        	if(i%2==0){
				time.add(new GregorianCalendar());
        		try {
					time.get(i/2).setTime(formatter.parse(data.get(i)));
				} catch (ParseException e) {
				}
        	}
        	if(i%2==1){
        		price.add(data.get(i));
        	}
        }
        //日付ごとの合計金額の配列を用意。そして加算。
        double[] priceOnDay = new double[31];
        for(int i=0;i<31;i++){
        	priceOnDay[i]=0;
        }
        for(int i=0;i<time.size();i++){
        	for(int j=1;j<=31;j++){
        		if(time.get(i).get(Calendar.DATE)==j
        				&&time.get(i).get(Calendar.MONTH)== month 
        				&&time.get(i).get(Calendar.YEAR)==year ){
        			priceOnDay[j-1] += Integer.parseInt(price.get(i));
        		}
        	}
        }
        
        for(int i=0;i<31;i++){
        	if(priceOnDay[i]!=0){
        		dataset.addValue(priceOnDay[i], "今月", String.valueOf(i+1)+"日");
        	}
        }
        /*
        dataset.addValue(1000, "今月", "1");
        dataset.addValue(2000, "今月", "2");
        dataset.addValue(1500, "今月", "3");
        dataset.addValue(3000, "今月", "4");
        */
        
		AFreeChart chart = ChartFactory.createLineChart(
				(month+1)+"月の出費", 
				"日付[日]",
				"金額[円]",
				dataset,
				PlotOrientation.VERTICAL,
				false,
				false,
				false);
		
		return chart;
	}
}
