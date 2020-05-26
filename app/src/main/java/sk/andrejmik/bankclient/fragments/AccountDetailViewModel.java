package sk.andrejmik.bankclient.fragments;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import sk.andrejmik.bankclient.objects.Account;
import sk.andrejmik.bankclient.repository_interface.IAccountRepository;
import sk.andrejmik.bankclient.repository_interface.RepositoryFactory;
import sk.andrejmik.bankclient.utils.Event;
import sk.andrejmik.bankclient.utils.NetworkHelper;

public class AccountDetailViewModel extends ViewModel
{
    MutableLiveData<Event<Event.LoadEvent>> onEvent = new MutableLiveData<>();
    private IAccountRepository mAccountRepository;
    private MutableLiveData<Account> mAccountLiveData;
    private String mParam;
    
    private AccountDetailViewModel(String param)
    {
        mParam = param;
        mAccountRepository = RepositoryFactory.getAccountRepository();
        mAccountLiveData = new MutableLiveData<>();
        loadAccount(mParam);
    }
    
    public MutableLiveData<Account> getAccountLiveData()
    {
        return mAccountLiveData;
    }
    
    private void loadAccount(String accountId)
    {
        onEvent.postValue(new Event<>(Event.LoadEvent.STARTED));
        if (!NetworkHelper.isNetworkAvailable())
        {
            onEvent.postValue(new Event<>(Event.LoadEvent.STARTED));
            return;
        }
        mAccountRepository.get(accountId).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Account>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
            
            }
            
            @Override
            public void onNext(Account account)
            {
                mAccountLiveData.postValue(account);
            }
            
            @Override
            public void onError(Throwable e)
            {
                onEvent.postValue(new Event<>(Event.LoadEvent.UNKNOWN_ERROR));
            }
            
            @Override
            public void onComplete()
            {
                onEvent.postValue(new Event<>(Event.LoadEvent.COMPLETE));
            }
        });
    }
    
    public static class AccountDetailViewModelFactory implements ViewModelProvider.Factory
    {
        private String mParam;
        
        public AccountDetailViewModelFactory(String param)
        {
            mParam = param;
        }
        
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
        {
            return (T) new AccountDetailViewModel(mParam);
        }
    }
}
