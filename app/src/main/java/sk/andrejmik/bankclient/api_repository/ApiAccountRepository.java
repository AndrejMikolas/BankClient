package sk.andrejmik.bankclient.api_repository;

import android.database.Observable;

import java.util.List;

import sk.andrejmik.bankclient.objects.Account;
import sk.andrejmik.bankclient.repository_interface.IAccountRepository;

public class ApiAccountRepository implements IAccountRepository
{
    @Override
    public Observable<Account> get(Object id)
    {
        return null;
    }
    
    @Override
    public Observable<List<Account>> getAll()
    {
        return null;
    }
    
    @Override
    public void save(Account data)
    {
    
    }
    
    @Override
    public void delete(Object id)
    {
    
    }
}
