package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.implement;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.account_database.Account_database_helper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private  Account_database_helper dbHelper;
    private  DateFormat dateFormat;

    public PersistentTransactionDAO(Account_database_helper dbHelper) {
        this.dbHelper = dbHelper;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        SQLiteDatabase db= dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Account_database_helper.Trans_Date, this.dateFormat.format(date));
        contentValues.put(Account_database_helper.Account_no, accountNo);
        contentValues.put(Account_database_helper.ExpenseType, String.valueOf(expenseType));
        contentValues.put(Account_database_helper.Amount, amount);

        db.insert(Account_database_helper.Transactions, null, contentValues);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query="SELECT * FROM " + Account_database_helper.Transactions + " ORDER BY " + Account_database_helper.Trans_Date + " DESC ";
        Cursor cursor = db.rawQuery(query,null);

        ArrayList<Transaction> transactions = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {

                Transaction transaction= new Transaction();
                try {
                    transaction.setDate(this.dateFormat.parse(cursor.getString(0)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                transaction.setAccountNo(cursor.getString(1));
                transaction.setExpenseType(ExpenseType.valueOf(cursor.getString(2).toUpperCase()));
                transaction.setAmount(cursor.getDouble(3));
                transactions.add(transaction);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                    "SELECT * FROM " + Account_database_helper.Transactions + " ORDER BY " + Account_database_helper.Trans_Date + " DESC " +
                            " LIMIT ?;"
                    , new String[]{Integer.toString(limit)}
            );

            ArrayList<Transaction> transactions = new ArrayList<>();
            if(cursor.moveToFirst()){
                do {

                    Transaction transaction= new Transaction();
                    try {
                        transaction.setDate(this.dateFormat.parse(cursor.getString(0)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    transaction.setAccountNo(cursor.getString(1));
                    transaction.setExpenseType(ExpenseType.valueOf(cursor.getString(2).toUpperCase()));
                    transaction.setAmount(cursor.getDouble(3));
                    transactions.add(transaction);
                }while (cursor.moveToNext());
            }
            cursor.close();
            return transactions;
    }
}
