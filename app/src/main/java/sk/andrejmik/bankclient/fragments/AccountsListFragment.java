package sk.andrejmik.bankclient.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import sk.andrejmik.bankclient.R;
import sk.andrejmik.bankclient.databinding.AccountsListFragmentBinding;
import sk.andrejmik.bankclient.list_adapters.AccountsAdapter;
import sk.andrejmik.bankclient.objects.Account;

public class AccountsListFragment extends Fragment
{
    private AccountsListFragmentBinding mBinding;
    private View mRootView;
    private AccountsListViewModel mViewModel;
    
    private AccountsAdapter mAccountsAdapter;
    
    private Observer<List<Account>> userListUpdateObserver = new Observer<List<Account>>()
    {
        @Override
        public void onChanged(List<Account> accountsArrayList)
        {
            mAccountsAdapter = new AccountsAdapter(accountsArrayList);
            mBinding.recyclerviewAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
            mBinding.recyclerviewAccounts.setAdapter(mAccountsAdapter);
        }
    };
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.accounts_list_fragment, container, false);
        mRootView = mBinding.getRoot();
        return mRootView;
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AccountsListViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getAccountsLiveData().observe(getViewLifecycleOwner(), userListUpdateObserver);
    }
    
}
