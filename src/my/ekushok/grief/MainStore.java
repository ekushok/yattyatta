package my.ekushok.grief;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MainStore {
	
	private DBOpenHelper m_helper;

	private SQLiteDatabase	m_db;
	private static final String TBL_NAME = "log2";	

	//NULL�`�F�b�N
	public MainStore( Context context ) {
		m_helper = DBOpenHelper.getInstance(context);
		if( m_helper != null ){
			m_db = m_helper.getWritableDatabase();
		}
		else{
			m_db = null;
		}
	}
	
	
	public void close() {
		m_db.close();
	}

	//���R�[�h�̒ǉ�
	public void add( int price ) {
		String sql = "insert into log2 (timestring,price) values (date('now','localtime'),"+price+")";
		m_db.execSQL(sql);
	}
	
	//���R�[�h�̍X�V
	public void update( int id,int price ) {
		ContentValues val = new ContentValues();
		java.text.SimpleDateFormat formatter 
			= new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		val.put("timestring",formatter.format(new Date()));
		val.put("price", price);
		m_db.update( TBL_NAME, val, "_id=?", new String[] { Integer.toString( id ) });		
	}
	
	//���R�[�h�̓ǂݍ���
	//columnIndex��1�̏ꍇ��timestamp,�Q�̏ꍇ��price���擾
	public ArrayList<String> loadAll(String queryString)
	{
		int i;
		Cursor c;
		ArrayList<String> entries = new ArrayList<String>();
        
		if( m_db == null )
        	return null;

		c = m_db.rawQuery(queryString, null);
		//�ŏ��̍s���w��
        c.moveToFirst();

		for( i = 0; i < c.getCount(); i++ )
		{
			
			entries.add(c.getString(1));
			entries.add(c.getString(2));
			//���̍s��
			c.moveToNext();
 		}
		c.close();

		return entries;
	}
	//���t���ƂɃ��R�[�h���擾
	public ArrayList<String> loadToDay(String queryString)
	{
		int i;
		Cursor c;
		ArrayList<String> entries = new ArrayList<String>();
        LinkedHashMap<String,Integer> map = new LinkedHashMap<String,Integer>();
		if( m_db == null )
        	return null;

		c = m_db.rawQuery(queryString, null);
		//�ŏ��̍s���w��
        c.moveToFirst();

		for( i = 0; i < c.getCount(); i++ )
		{
			if(map.get(c.getString(1)) == null){
				map.put(c.getString(1), c.getInt(2));
			}else{
				map.put(c.getString(1), map.get(c.getString(1))+c.getInt(2));
			}
			//���̍s��
			c.moveToNext();
 		}
		for(String key : map.keySet()){
			entries.add(key);
			entries.add(String.valueOf(map.get(key))+"�~");
		}
		c.close();

		return entries;
	}
	//�S���폜
	public void deleteAll(){
		//m_db.delete(TBL_NAME, null, null);
	}
	public void Undo(){
		Cursor c;
		c = m_db.rawQuery("select _id from log2;", null);
		c.moveToLast();
		
		String last = c.getString(0);
		c.close();
		m_db.execSQL("delete from log2 where _id='"+last+"'");
	}
}
