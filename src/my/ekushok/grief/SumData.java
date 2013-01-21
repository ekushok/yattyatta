package my.ekushok.grief;

import java.util.ArrayList;

import android.content.Context;

public class SumData {
	private MainStore store;

	SumData(Context context){
		store = new MainStore(context);
	}
	public int sumAll(){
		int sum=0;
		int count=0;
		ArrayList<String> data = new ArrayList<String>();
		
		//data = store.loadAll("select _id,timestring,price from log2 where date('timestring','localtime') = date(CURRENT_TIME,'localtime')");
		data = store.loadAll("select _id,timestring,price from log2 where timestring = date('now','localtime')");
		for(String value:data){
			if(count%2==1){ 
				sum += Integer.parseInt(value);
			}
			count++;
		}
		return sum;
	}
}
