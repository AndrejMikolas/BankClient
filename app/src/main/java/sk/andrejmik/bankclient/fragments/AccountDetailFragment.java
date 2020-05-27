package sk.andrejmik.bankclient.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import sk.andrejmik.bankclient.R;
import sk.andrejmik.bankclient.databinding.AccountDetailFragmentBinding;
import sk.andrejmik.bankclient.list_adapters.CardsListAdapter;
import sk.andrejmik.bankclient.objects.Account;
import sk.andrejmik.bankclient.utils.Event;
import sk.andrejmik.bankclient.utils.LoadEvent;
import sk.andrejmik.bankclient.utils.ViewHelper;

public class AccountDetailFragment extends Fragment
{
    private Fragment mFragment;
    private AccountDetailViewModel mViewModel;
    private AccountDetailFragmentBinding mBinding;
    private String mAccountId;
    private CardsListAdapter mCardsListAdapter;
    private Snackbar mSnackUnknownError, mSnackNetworkError;
    private ProgressDialog mProgressDialog;
    private Observer<Account> mAccountObserver = new Observer<Account>()
    {
        @Override
        public void onChanged(Account account)
        {
            mBinding.setAccount(account);
            mCardsListAdapter = new CardsListAdapter(account.getCardsList(), account.getOwner());
            mBinding.recyclerviewCards.setLayoutManager(new LinearLayoutManager(getContext()));
            mBinding.recyclerviewCards.setAdapter(mCardsListAdapter);
        }
    };
    private Observer<Event<LoadEvent>> mLoadAccountEventObserver = new Observer<Event<LoadEvent>>()
    {
        @Override
        public void onChanged(Event<LoadEvent> loadEventEvent)
        {
            switch (loadEventEvent.peekContent())
            {
                case UNKNOWN_ERROR:
                    ViewHelper.controlSnack(mSnackUnknownError, true);
                    ViewHelper.controlSnack(mSnackNetworkError, false);
                    mBinding.swipeContainerAccountDetail.setEnabled(true);
                    mBinding.swipeContainerAccountDetail.setRefreshing(false);
                    break;
                case NETWORK_ERROR:
                    ViewHelper.controlSnack(mSnackUnknownError, false);
                    ViewHelper.controlSnack(mSnackNetworkError, true);
                    mBinding.swipeContainerAccountDetail.setEnabled(true);
                    mBinding.swipeContainerAccountDetail.setRefreshing(false);
                    break;
                case COMPLETE:
                    ViewHelper.controlSnack(mSnackUnknownError, false);
                    ViewHelper.controlSnack(mSnackNetworkError, false);
                    mBinding.swipeContainerAccountDetail.setEnabled(true);
                    mBinding.swipeContainerAccountDetail.setRefreshing(false);
                    Toast.makeText(getContext(), getResources().getString(R.string.updated), Toast.LENGTH_SHORT).show();
                    break;
                case STARTED:
                    ViewHelper.controlSnack(mSnackUnknownError, false);
                    ViewHelper.controlSnack(mSnackNetworkError, false);
                    mBinding.swipeContainerAccountDetail.setRefreshing(true);
                    break;
                case NO_MORE:
                    break;
                case NOT_FOUND:
                    ViewHelper.controlSnack(mSnackUnknownError, false);
                    ViewHelper.controlSnack(mSnackNetworkError, false);
                    mBinding.swipeContainerAccountDetail.setEnabled(true);
                    mBinding.swipeContainerAccountDetail.setRefreshing(false);
                    break;
            }
        }
    };
    private Observer<Event<LoadEvent>> mDeleteAccountEventObserver = new Observer<Event<LoadEvent>>()
    {
        @Override
        public void onChanged(Event<LoadEvent> loadEventEvent)
        {
            switch (loadEventEvent.peekContent())
            {
                case UNKNOWN_ERROR:
                    mProgressDialog.dismiss();
                    mSnackUnknownError.show();
                    break;
                case NETWORK_ERROR:
                    mProgressDialog.dismiss();
                    mSnackNetworkError.show();
                    break;
                case COMPLETE:
                    mProgressDialog.dismiss();
                    Toast.makeText(getContext(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(mFragment).navigate(R.id.action_accountDetailFragment_to_accountsListFragment);
                    break;
                case STARTED:
                    mProgressDialog = ProgressDialog.show(getContext(), "", getString(R.string.deleting_account_dialog_message), true);
                    break;
            }
        }
    };
    private View.OnClickListener clickRetryLoadListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            loadAccount();
        }
    };
    
    public AccountDetailFragment()
    {
        mFragment = this;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.account_detail_fragment, container, false);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mAccountId = bundle.getString("account_id");
        }
        return mBinding.getRoot();
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, new AccountDetailViewModel.AccountDetailViewModelFactory(mAccountId)).get(
                AccountDetailViewModel.class);
        mViewModel.getAccountLiveData().observe(getViewLifecycleOwner(), mAccountObserver);
        setupObservers();
        setupSnacks();
        setupListeners();
    }
    
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_detail_account, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_edit_account:
                Bundle bundle = new Bundle();
                bundle.putSerializable("account", mViewModel.getAccountLiveData().getValue());
                NavHostFragment.findNavController(mFragment).navigate(R.id.action_accountDetailFragment_to_newAccountFragment, bundle);
                break;
            case R.id.action_delete_account:
                mViewModel.deleteAccount();
                break;
        }
        return true;
    }
    
    private void setupListeners()
    {
        mBinding.swipeContainerAccountDetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                loadAccount();
            }
        });
    }
    
    /**
     * Setting observers
     */
    private void setupObservers()
    {
        mViewModel.getAccountLiveData().observe(getViewLifecycleOwner(), mAccountObserver);
        mViewModel.loadAccountEvent.observe(getViewLifecycleOwner(), mLoadAccountEventObserver);
        mViewModel.deleteAccountEvent.observe(getViewLifecycleOwner(), mDeleteAccountEventObserver);
    }
    
    /**
     * Creates and sets params of snackbars which will be used repeatedly
     */
    private void setupSnacks()
    {
        mSnackNetworkError = Snackbar.make(mBinding.swipeContainerAccountDetail, getResources().getString(R.string.network_error),
                                           Snackbar.LENGTH_INDEFINITE);
        mSnackNetworkError.setAction(getResources().getString(R.string.retry), clickRetryLoadListener);
        mSnackUnknownError = Snackbar.make(mBinding.swipeContainerAccountDetail, getResources().getString(R.string.unknown_error),
                                           Snackbar.LENGTH_INDEFINITE);
        mSnackUnknownError.setAction(getResources().getString(R.string.retry), clickRetryLoadListener);
    }
    
    private void loadAccount()
    {
        mViewModel.loadAccount();
    }
    
}
