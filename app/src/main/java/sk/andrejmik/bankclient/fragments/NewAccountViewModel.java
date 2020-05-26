package sk.andrejmik.bankclient.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import sk.andrejmik.bankclient.objects.Account;
import sk.andrejmik.bankclient.repository_interface.IAccountRepository;
import sk.andrejmik.bankclient.repository_interface.RepositoryFactory;
import sk.andrejmik.bankclient.utils.Event;
import sk.andrejmik.bankclient.utils.NetworkHelper;

public class NewAccountViewModel extends ViewModel
{
    MutableLiveData<Event<Event.LoadEvent>> onEvent = new MutableLiveData<>();
    private MutableLiveData<Account> mAccountLiveData;
    public LiveData<Account> account;
    private IAccountRepository mAccountRepository;
    
    public NewAccountViewModel()
    {
        mAccountRepository = RepositoryFactory.getAccountRepository();
        mAccountLiveData = new MutableLiveData<>();
        mAccountLiveData.postValue(new Account());
        account = mAccountLiveData;
    }
    
    public MutableLiveData<Account> getAccountLiveData()
    {
        return mAccountLiveData;
    }
    
    public void setAccountLiveData(Account account)
    {
        mAccountLiveData.setValue(account);
    }
    
    public void saveAccount()
    {
        onEvent.postValue(new Event<>(Event.LoadEvent.STARTED));
        if (!NetworkHelper.isNetworkAvailable())
        {
            onEvent.postValue(new Event<>(Event.LoadEvent.STARTED));
            return;
        }
        mAccountRepository.save(mAccountLiveData.getValue());
    }
}
