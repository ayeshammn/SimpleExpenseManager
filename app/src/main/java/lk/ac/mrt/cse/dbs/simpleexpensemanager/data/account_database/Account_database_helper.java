package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.account_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.Arrays;

public class Account_database_helper extends SQLiteOpenHelper {


    public static final String Accounts = "Account";
    public static final String Account_no = "accountNo";
    public static final String BankName = "bankName";
    public static final String HolderName = "accountHolderName";
    public static final String Balance = "balance";

    public static final String Transactions = "Trans";
    public static final String Trans_Date = "date";
    public static final String ExpenseType = "expenseType";
    public static final String Amount = "amount";


    public Account_database_helper(@Nullable Context context) {
        super(context, "200058L.db", null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+Accounts+" " +
                "("
                +Account_no+"  PRIMARY KEY, "
                +BankName + " NOT NULL,"
                +HolderName + "  NOT NULL, "
                +Balance+" DOUBLE NOT NULL CHECK ("+ Balance+" > 0)"+
                ");"
        );

        db.execSQL("CREATE TABLE "+Transactions+" " +
                "("
                +Trans_Date + "  NOT NULL, "
                +Account_no+"  NOT NULL, "
                +ExpenseType + "  NOT NULL, "
                +Amount+" DOUBLE NOT NULL CHECK ("+ Amount+" > 0),"
                + " FOREIGN KEY ("+Account_no+") REFERENCES "+Accounts+"("+Account_no+")ON DELETE CASCADE\n" +
                "ON UPDATE CASCADE);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + Accounts);
        db.execSQL("drop table if exists '" + Transactions + "'");
        onCreate(db);
    }
}


//    public void add_record(String table_name, ContentValues content){
//        SQLiteDatabase db = this.getWritableDatabase();
//        long var ;
//        try{
//            var = db.insertOrThrow(table_name, null,content);
//        }catch(Exception e){
//            var = -1;
//            System.out.println("Error in inserting data");
//        }
//        if(var == -1){
//        }else{
//        }
//    }
//
//    public Cursor getDataWithLimit(String table_name, String [] column, String [][] condition, int limit){
//        SQLiteDatabase db = this.getWritableDatabase();
//        StringBuilder cols = new StringBuilder();
//        if (column.length != 0){
//            for (String s : column) {
//                cols.append(s).append(" , ");
//            }
//            cols = new StringBuilder(cols.substring(0, cols.length() - 2));
//        }
//        StringBuilder cond = new StringBuilder();
//        String[] args = null;
//        if(condition.length != 0){
//            args = new String[condition.length];
//            cond.append(" where ");
//            for (int i = 0;i < condition.length ;i++){
//                if(condition[i].length == 3){
//                    String[] temp = condition[i];
//                    cond.append(temp[0]).append(" ").append(temp[1]).append(" ? AND ");
//                    args[i] = temp[2];
//                }
//            }
//            cond = new StringBuilder(cond.substring(0, cond.length() - 4));
//        }else{
//            cond = new StringBuilder();
//        }
//
//        String lim = "";
//        if(limit != 0){
//            lim = " limit "+String.valueOf(limit);
//        }
//        String sql = "select "+cols+" from "+table_name+ Arrays.deepToString(condition) +lim;
//        return db.rawQuery(sql,args);
//    }
//
//    public Cursor get_record(String table_name, String [] columns, String [][] conditions){
//        return getDataWithLimit(table_name, columns, conditions,0);
//    }
//
//
//    public boolean updateData(String table_name,ContentValues content, String[ ] condition) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String cond = condition[0] + " " + condition[1] + " ? ";
//        String[] args = {condition[2]};
//        long res;
//        try {
//            res = db.update(table_name, content, cond, args);
//        } catch (Exception e) {
//
//            res = -1;
//        }
//
//        return res != -1;
//    }
//    public Integer delete_record(String table_name, String column, String pk){
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete(table_name, column+" = ?", new String[] {pk});
//    }
//
//    public void delete_table(String table_name){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from "+table_name);
//    }
//
//
//
//    }
//
