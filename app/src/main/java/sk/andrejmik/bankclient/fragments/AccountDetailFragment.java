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

import sk.andrejmik.bankclient.R;
import sk.andrejmik.bankclient.databinding.AccountDetailFragmentBinding;
import sk.andrejmik.bankclient.list_adapters.CardsListAdapter;
import sk.andrejmik.bankclient.objects.Account;

public class AccountDetailFragment extends Fragment
{
    private AccountDetailViewModel mViewModel;
    private AccountDetailFragmentBinding mBinding;
    private String mAccountId;
    private CardsListAdapter mCardsListAdapter;
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
        // TODO: Use the ViewModel
    }
    
}
