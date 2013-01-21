package my.ekushok.grief;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "Grief.db";
	private static final int DB_VERSION = 2;
	private int m_writableDatabaseCount = 0;

	private static DBOpenHelper m_instance = null;
	
	//??? synchronize�̓X���b�h�̔r������
	synchronized static
	public DBOpenHelper getInstance( Context context )
	{
		if ( m_instance == null )
		{
			m_instance = new DBOpenHelper( context.getApplicationContext() );
		}
		
		return m_instance;
	}
	//�R���X�g���N�^
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
	//�f�[�^�x�[�X���������ꂽ��Ăяo�����B�e�[�u��������Ă�B
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(
				"CREATE TABLE IF NOT EXISTS log ( _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , timestamp INTEGER NOT NULL , price INTEGER )"
				);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		db.execSQL(
				"CREATE TABLE IF NOT EXISTS log2 ( _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , timestring TEXT NOT NULL , price INTEGER )"
				);
	}

}
