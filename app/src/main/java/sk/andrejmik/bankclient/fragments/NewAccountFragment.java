package sk.andrejmik.bankclient.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import sk.andrejmik.bankclient.R;
import sk.andrejmik.bankclient.databinding.NewAccountFragmentBinding;
import sk.andrejmik.bankclient.fragments.dialogs.NewCardFragmentDialog;
import sk.andrejmik.bankclient.objects.Account;
import sk.andrejmik.bankclient.objects.Card;
import sk.andrejmik.bankclient.utils.Event;
import sk.andrejmik.bankclient.utils.Globals;
import sk.andrejmik.bankclient.utils.LoadEvent;

/**
 * Fragment for creating new account or edit existing
 */
public class NewAccountFragment extends Fragment
{
    private static final int ADD_CARD_FRAGMENT_DIALOG_RESULT_CODE = 1;
    private Fragment mFragment;
    private NewAccountFragmentBinding mBinding;
    private NewAccountViewModel mViewModel;
    private ArrayAdapter<Card> mCardArrayAdapter;
    private ProgressDialog mProgressDialog;
    private Snackbar mSnackNetworkError, mSnackUnknownError;
    /* Observing changes in account in viewmodel */
    private Observer<Account> mAccountObserver = new Observer<Account>()
    {
        @Override
        public void onChanged(Account account)
        {
            mCardArrayAdapter = new ArrayAdapter<Card>(getContext(), android.R.layout.simple_list_item_1,
                                                       mViewModel.account.getValue().getCardsList());
            mBinding.listviewNewCards.setAdapter(mCardArrayAdapter);
            mBinding.listviewNewCards.invalidate();
        }
    };
    /* Observing changes in saving account event in viewmodel */
    private Observer<Event<LoadEvent>> mSaveAccountEventObserver = new Observer<Event<LoadEvent>>()
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
                    NavHostFragment.findNavController(mFragment).navigate(R.id.action_newAccountFragment_to_accountsListFragment);
                    break;
                case STARTED:
                    mProgressDialog = ProgressDialog.show(getContext(), "", getString(R.string.saving_dialog_message), true);
                    break;
            }
        }
    };
    
    public NewAccountFragment()
    {
        mFragment = this;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        switch (requestCode)
        {
            /* On close dialog with new card created, add new card to account in viewmodel */
            case ADD_CARD_FRAGMENT_DIALOG_RESULT_CODE:
                if (resultCode == Activity.RESULT_OK)
                {
                    Bundle bundle = data.getExtras();
                    if (bundle == null)
                    {
                        return;
                    }
                    Card card = Globals.GSON.fromJson(bundle.getString("new_card"), Card.class);
                    if (card == null)
                    {
                        return;
                    }
                    mViewModel.addCard(card);
                }
        }
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
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("account"))
        {
            Account editAccount = (Account) bundle.getSerializable("account");
            mViewModel.setAccountLiveData(editAccount);
        }
        setupObservers();
        setupSnacks();
        setupListeners();
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
    
    private void setupListeners()
    {
        mBinding.buttonNewAccountAddCard.setOnClickListener(new View.OnClickListener()
        {
            /**
             * Open new dialog for adding new card
             */
            @Override
            public void onClick(View view)
            {
                FragmentTransaction ft = requireFragmentManager().beginTransaction();
                Fragment previous = getFragmentManager().findFragmentByTag("dialog");
                if (previous != null)
                {
                    ft.remove(previous);
                }
                ft.addToBackStack(null);
                DialogFragment newDeviceFragmentDialog = new NewCardFragmentDialog();
                newDeviceFragmentDialog.setTargetFragment(mFragment, ADD_CARD_FRAGMENT_DIALOG_RESULT_CODE);
                newDeviceFragmentDialog.show(ft, "dialog");
            }
        });
        mBinding.listviewNewCards.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            /**
             * On long click on cards list card will be deleted
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                mViewModel.removeCard(i);
                return false;
            }
        });
    }
    
    /**
     * Creates and sets params of snackbars which will be used repeatedly
     */
    private void setupSnacks()
    {
        mSnackNetworkError = Snackbar.make(mBinding.rootNewAccountFragment, getResources().getString(R.string.network_error), Snackbar.LENGTH_SHORT);
        mSnackUnknownError = Snackbar.make(mBinding.rootNewAccountFragment, getResources().getString(R.string.unknown_error), Snackbar.LENGTH_SHORT);
    }
    
    /**
     * Setting observers
     */
    private void setupObservers()
    {
        mViewModel.getAccountLiveData().observe(getViewLifecycleOwner(), mAccountObserver);
        mViewModel.onEvent.observe(getViewLifecycleOwner(), mSaveAccountEventObserver);
    }
    
}
