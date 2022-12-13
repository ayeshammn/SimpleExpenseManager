package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.implement;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.account_database.Account_database_helper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private final Account_database_helper dbHelper;



    public PersistentAccountDAO(Account_database_helper dbHelper){
        this.dbHelper = dbHelper;

    }


    @Override
    public List<String> getAccountNumbersList() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query="SELECT " + Account_database_helper.Account_no+ " FROM " + Account_database_helper.Accounts;
        Cursor cursor = db.rawQuery(query,null);
        ArrayList<String> accountNumbers = new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            accountNumbers.add(cursor.getString(0));
        }

        cursor.close();
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM "+ Account_database_helper.Accounts;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do {
                // Create new Account object
                Account account = new Account();
                account.setAccountNo(cursor.getString(0));
                account.setBankName(cursor.getString(1));
                account.setAccountHolderName(cursor.getString(2));
                account.setBalance(cursor.getDouble(3));


                accounts.add(account);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + Account_database_helper.Accounts + " WHERE " + Account_database_helper.Account_no + "=?;"
                , new String[]{accountNo});
        Account account;
        if (cursor != null && cursor.moveToFirst()) {
            account = new Account();
            account.setAccountNo(cursor.getString(0));
            account.setBankName(cursor.getString(1));
            account.setAccountHolderName(cursor.getString(2));
            account.setBalance(cursor.getDouble(3));

        } else {
            throw new InvalidAccountException("Account "+accountNo+" is Invalid");
        }
        cursor.close();
        return account;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase DB = dbHelper.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(Account_database_helper.Account_no, account.getAccountNo());
        content.put(Account_database_helper.BankName, account.getBankName());
        content.put(Account_database_helper.HolderName, account.getAccountHolderName());
        content.put(Account_database_helper.Balance, account.getBalance());
        DB.insert(Account_database_helper.Accounts,null,content);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + Account_database_helper.Accounts + " WHERE " + Account_database_helper.Account_no + "=?;"
                , new String[]{accountNo}
        );

        if (cursor.moveToFirst()) {
            db.delete(
                    Account_database_helper.Accounts,
                    Account_database_helper.Account_no + " = ?",
                    new String[]{accountNo}
            );
        } else {
            throw new InvalidAccountException("Invalid account number");
        }
        cursor.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Account account = this.getAccount(accountNo);

        if (account != null) {

            switch (expenseType) {
                case EXPENSE:
                    account.setBalance(account.getBalance() - amount);
                    break;
                case INCOME:
                    account.setBalance(account.getBalance() + amount);
                    break;
            }

            db.execSQL(
                    "UPDATE " + Account_database_helper.Accounts +
                            " SET " + Account_database_helper.Balance + " = ?" +
                            " WHERE " + Account_database_helper.Account_no + " = ?",
                    new String[]{Double.toString(account.getBalance()), accountNo});

        } else {
            throw new InvalidAccountException("Invalid account number");
        }
    }
}

