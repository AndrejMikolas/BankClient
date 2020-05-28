package sk.andrejmik.bankclient.fragments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import sk.andrejmik.bankclient.api_repository.ApiAccountRepository;
import sk.andrejmik.bankclient.objects.Account;
import sk.andrejmik.bankclient.repository_interface.IAccountRepository;
import sk.andrejmik.bankclient.utils.Event;
import sk.andrejmik.bankclient.utils.LoadEvent;
import sk.andrejmik.bankclient.utils.NetworkHelper;

public class AccountsListViewModel extends ViewModel
{
    MutableLiveData<Event<LoadEvent>> onEvent = new MutableLiveData<>();
    
    private IAccountRepository mAccountRepository;
    private MutableLiveData<List<Account>> mListLiveData;
    
    public AccountsListViewModel()
    {
        mListLiveData = new MutableLiveData<>();
        mAccountRepository = new ApiAccountRepository();
        loadAccounts();
    }
    
    MutableLiveData<List<Account>> getAccountsLiveData()
    {
        return mListLiveData;
    }
    
    /**
     * Load accounts list from repository by param
     */
    void loadAccounts()
    {
        onEvent.postValue(new Event<>(LoadEvent.STARTED));
        if (!NetworkHelper.isNetworkAvailable())
        {
            onEvent.postValue(new Event<>(LoadEvent.NETWORK_ERROR));
            return;
        }
        mAccountRepository.getAll().subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Account>>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
            
            }
            
            @Override
            public void onNext(List<Account> accounts)
            {
                mListLiveData.postValue(accounts);
            }
            
            @Override
            public void onError(Throwable e)
            {
                onEvent.postValue(new Event<>(LoadEvent.UNKNOWN_ERROR));
            }
            
            @Override
            public void onComplete()
            {
                onEvent.postValue(new Event<>(LoadEvent.COMPLETE));
            }
        });
    }
}
