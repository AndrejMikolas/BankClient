package sk.andrejmik.bankclient.fragments;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import sk.andrejmik.bankclient.objects.Account;
import sk.andrejmik.bankclient.repository_interface.IAccountRepository;
import sk.andrejmik.bankclient.repository_interface.RepositoryFactory;
import sk.andrejmik.bankclient.utils.Event;
import sk.andrejmik.bankclient.utils.LoadEvent;
import sk.andrejmik.bankclient.utils.NetworkHelper;

public class NewAccountViewModel extends ViewModel
{
    public LiveData<Account> account;
    MutableLiveData<Event<LoadEvent>> onEvent = new MutableLiveData<>();
    private MutableLiveData<Account> mAccountLiveData;
    private IAccountRepository mAccountRepository;
    //private String mEditAccountId;
    
//    public NewAccountViewModel(String editAccountId)
//    {
//        mEditAccountId = editAccountId;
//        mAccountRepository = RepositoryFactory.getAccountRepository();
//        mAccountLiveData = new MutableLiveData<>();
//        mAccountLiveData.postValue(new Account());
//        account = mAccountLiveData;
//    }
    
    public NewAccountViewModel()
    {
        mAccountRepository = RepositoryFactory.getAccountRepository();
        mAccountLiveData = new MutableLiveData<>();
        mAccountLiveData.postValue(new Account());
        account = mAccountLiveData;
    }
    
    MutableLiveData<Account> getAccountLiveData()
    {
        return mAccountLiveData;
    }
    
    public void setAccountLiveData(Account account)
    {
        mAccountLiveData.postValue(account);
    }
    
    void saveAccount()
    {
        onEvent.postValue(new Event<>(LoadEvent.STARTED));
        Account account = mAccountLiveData.getValue();
        account.setDateCreated(new Date());
        if (!NetworkHelper.isNetworkAvailable())
        {
            onEvent.postValue(new Event<>(LoadEvent.STARTED));
            return;
        }
        mAccountRepository.save(account).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Account>()
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
                onEvent.postValue(new Event<>(LoadEvent.UNKNOWN_ERROR));
            }
            
            @Override
            public void onComplete()
            {
                onEvent.postValue(new Event<>(LoadEvent.COMPLETE));
            }
        });
    }
    
//    public static class NewAccountViewModelFactory implements ViewModelProvider.Factory
//    {
//        private String mParam;
//
//        public NewAccountViewModelFactory(String param)
//        {
//            mParam = param;
//        }
//
//        @NonNull
//        @Override
//        public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
//        {
//            return (T) new NewAccountViewModel(mParam);
//        }
//    }
}
