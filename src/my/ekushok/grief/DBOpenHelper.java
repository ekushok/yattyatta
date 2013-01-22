package my.ekushok.grief;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "Grief.db";
	private static final int DB_VERSION = 2;
	private int m_writableDatabaseCount = 0;

	private static DBOpenHelper m_instance = null;
	
	//??? synchronizeはスレッドの排他制御
	synchronized static
	public DBOpenHelper getInstance( Context context )
	{
		if ( m_instance == null )
		{
			m_instance = new DBOpenHelper( context.getApplicationContext() );
		}
		
		return m_instance;
	}
	//コンストラクタ
	public DBOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		
	}

	@Override
	synchronized public SQLiteDatabase getWritableDatabase()
	{
		SQLiteDatabase db = super.getWritableDatabase();
		if ( db != null )
		{
			++m_writableDatabaseCount;
		}
			
		return db;
	}

	synchronized public void closeWritableDatabase( SQLiteDatabase database )
	{
		if ( m_writableDatabaseCount > 0 && database != null )
		{
			--m_writableDatabaseCount;
			if ( m_writableDatabaseCount == 0 )
			{
				database.close();
			}
		}
	}
	//データベースが生成されたら呼び出される。テーブルを作ってる。
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(
				"CREATE TABLE IF NOT EXISTS log2 ( _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , timestring TEXT NOT NULL , price INTEGER ,type INTEGER)"
				);
		db.execSQL(
				"CREATE TABLE IF NOT EXISTS button ( _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , type INTEGER,name TEXT,price INTEGER)"
				);
		//buttonテーブルの初期値設定
		db.execSQL("insert into button (type,name,price) values (1,'缶ジュース買っちゃった',100)");
		db.execSQL("insert into button (type,name,price) values (2,'ランチ贅沢しちゃった',1000)");
		db.execSQL("insert into button (type,name,price) values (3,'飲み会行っちゃった',3000)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(
				"CREATE TABLE IF NOT EXISTS log2 ( _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , timestring TEXT NOT NULL , price INTEGER ,type INT)"
				);
	}

}
