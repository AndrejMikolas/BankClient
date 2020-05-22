package sk.andrejmik.bankclient.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.andrejmik.bankclient.R;

public class AccountsListFragment extends Fragment
{
    
    private AccountsListViewModel mViewModel;
    
    public static AccountsListFragment newInstance()
    {
        return new AccountsListFragment();
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.accounts_list_fragment, container, false);
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AccountsListViewModel.class);
        // TODO: Use the ViewModel
    }
    
}
