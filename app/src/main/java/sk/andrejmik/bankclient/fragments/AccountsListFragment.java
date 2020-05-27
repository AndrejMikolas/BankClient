package sk.andrejmik.bankclient.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import sk.andrejmik.bankclient.R;
import sk.andrejmik.bankclient.databinding.AccountsListFragmentBinding;
import sk.andrejmik.bankclient.list_adapters.AccountsAdapter;
import sk.andrejmik.bankclient.objects.Account;
import sk.andrejmik.bankclient.utils.Event;
import sk.andrejmik.bankclient.utils.LoadEvent;
import sk.andrejmik.bankclient.utils.RecyclerTouchListener;
import sk.andrejmik.bankclient.utils.ViewHelper;

public class AccountsListFragment extends Fragment
{
    private AccountsListFragmentBinding mBinding;
    private Fragment mFragment;
    private AccountsListViewModel mViewModel;
    private AccountsAdapter mAccountsAdapter;
    private Observer<List<Account>> mAccountListUpdateObserver = new Observer<List<Account>>()
    {
        @Override
        public void onChanged(List<Account> accountsArrayList)
        {
            mAccountsAdapter = new AccountsAdapter(accountsArrayList);
            mBinding.recyclerviewAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
            mBinding.recyclerviewAccounts.setAdapter(mAccountsAdapter);
        }
    };
    private Snackbar mSnackUnknownError, mSnackNetworkError;
    private Observer<Event<LoadEvent>> mLoadAccountsEventObserver = new Observer<Event<LoadEvent>>()
    {
        @Override
        public void onChanged(Event<LoadEvent> loadEventEvent)
        {
            switch (loadEventEvent.peekContent())
            {
                case UNKNOWN_ERROR:
                    ViewHelper.controlSnack(mSnackUnknownError, true);
                    ViewHelper.controlSnack(mSnackNetworkError, false);
                    mBinding.swipeContainerAccountsList.setEnabled(true);
                    mBinding.swipeContainerAccountsList.setRefreshing(false);
                    break;
                case NETWORK_ERROR:
                    ViewHelper.controlSnack(mSnackUnknownError, false);
                    ViewHelper.controlSnack(mSnackNetworkError, true);
                    mBinding.swipeContainerAccountsList.setEnabled(true);
                    mBinding.swipeContainerAccountsList.setRefreshing(false);
                    break;
                case COMPLETE:
                    ViewHelper.controlSnack(mSnackUnknownError, false);
                    ViewHelper.controlSnack(mSnackNetworkError, false);
                    mBinding.swipeContainerAccountsList.setEnabled(true);
                    mBinding.swipeContainerAccountsList.setRefreshing(false);
                    Toast.makeText(getContext(), getResources().getString(R.string.updated), Toast.LENGTH_SHORT).show();
                    break;
                case STARTED:
                    ViewHelper.controlSnack(mSnackUnknownError, false);
                    ViewHelper.controlSnack(mSnackNetworkError, false);
                    mBinding.swipeContainerAccountsList.setRefreshing(true);
                    break;
                case NO_MORE:
                    break;
                case NOT_FOUND:
                    ViewHelper.controlSnack(mSnackUnknownError, false);
                    ViewHelper.controlSnack(mSnackNetworkError, false);
                    mBinding.swipeContainerAccountsList.setEnabled(true);
                    mBinding.swipeContainerAccountsList.setRefreshing(false);
                    break;
            }
        }
    };
    private View.OnClickListener clickRetryLoadListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            loadAccounts();
        }
    };
    
    public AccountsListFragment()
    {
        mFragment = this;
    }
    
    private void loadAccounts()
    {
        mViewModel.loadAccounts();
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.accounts_list_fragment, container, false);
        return mBinding.getRoot();
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AccountsListViewModel.class);
        setupSnacks();
        setupObservers();
        setupListeners();
    }
    
    /**
     * Setting observers
     */
    private void setupObservers()
    {
        mViewModel.getAccountsLiveData().observe(getViewLifecycleOwner(), mAccountListUpdateObserver);
        mViewModel.onEvent.observe(getViewLifecycleOwner(), mLoadAccountsEventObserver);
    }
    
    /**
     * Creates and sets params of snackbars which will be used repeatedly
     */
    private void setupSnacks()
    {
        mSnackNetworkError = Snackbar.make(mBinding.swipeContainerAccountsList, getResources().getString(R.string.network_error),
                                           Snackbar.LENGTH_INDEFINITE);
        mSnackNetworkError.setAction(getResources().getString(R.string.retry), clickRetryLoadListener);
        mSnackUnknownError = Snackbar.make(mBinding.swipeContainerAccountsList, getResources().getString(R.string.unknown_error),
                                           Snackbar.LENGTH_INDEFINITE);
        mSnackUnknownError.setAction(getResources().getString(R.string.retry), clickRetryLoadListener);
    }
    
    private void setupListeners()
    {
        mBinding.recyclerviewAccounts.addOnItemTouchListener(
                new RecyclerTouchListener(getContext(), mBinding.recyclerviewAccounts, new RecyclerTouchListener.ClickListener()
                {
                    @Override
                    public void onClick(View view, int position)
                    {
                        Account account = mAccountsAdapter.getAccountList().get(position);
                        Bundle bundle = new Bundle();
                        bundle.putString("account_id", account.getId());
                        NavHostFragment.findNavController(mFragment).navigate(R.id.action_accountsListFragment_to_accountDetailFragment, bundle);
                    }
                }));
        mBinding.floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(mFragment).navigate(R.id.action_accountsListFragment_to_newAccountFragment);
            }
        });
        mBinding.swipeContainerAccountsList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                loadAccounts();
            }
        });
    }
}
