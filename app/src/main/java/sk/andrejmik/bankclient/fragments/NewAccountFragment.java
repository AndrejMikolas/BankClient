package sk.andrejmik.bankclient.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import sk.andrejmik.bankclient.R;
import sk.andrejmik.bankclient.databinding.NewAccountFragmentBinding;
import sk.andrejmik.bankclient.objects.Account;
import sk.andrejmik.bankclient.objects.Card;

public class NewAccountFragment extends Fragment
{
    private NewAccountFragmentBinding mBinding;
    private NewAccountViewModel mViewModel;
    private ArrayAdapter<Card> mCardArrayAdapter;
    private Observer<Account> mAccountObserver = new Observer<Account>()
    {
        @Override
        public void onChanged(Account account)
        {
            //mBinding.
            //mBinding.listviewNewCards.setLayoutManager(new LinearLayoutManager(getContext()));
            mCardArrayAdapter = new ArrayAdapter<Card>(getContext(), android.R.layout.simple_list_item_1,
                                                       mViewModel.account.getValue().getCardsList());
            mBinding.listviewNewCards.setAdapter(mCardArrayAdapter);
            mBinding.listviewNewCards.invalidate();
        }
    };
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.new_account_fragment, container, false);
        mBinding.setLifecycleOwner(this.getViewLifecycleOwner());
        return mBinding.getRoot();
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NewAccountViewModel.class);
        mBinding.setAccount(mViewModel.account);
        // TODO: Use the ViewModel
        
        mViewModel.getAccountLiveData().observe(getViewLifecycleOwner(), mAccountObserver);
    }
    
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_new_account, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_save_account:
                mViewModel.saveAccount();
                break;
        }
        return true;
    }
    
}
