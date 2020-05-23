package sk.andrejmik.bankclient.list_adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sk.andrejmik.bankclient.R;
import sk.andrejmik.bankclient.databinding.RowItemAccountsListBinding;
import sk.andrejmik.bankclient.objects.Account;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>
{
    private List<Account> mAccountList;
    
    public AccountsAdapter(List<Account> accountList)
    {
        mAccountList = accountList;
    }
    
    public List<Account> getAccountList()
    {
        return mAccountList;
    }
    
    @NonNull
    @Override
    public AccountsAdapter.AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RowItemAccountsListBinding itemAccountsListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                                                                     R.layout.row_item_accounts_list, parent, false);
        return new AccountsAdapter.AccountViewHolder(itemAccountsListBinding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AccountsAdapter.AccountViewHolder holder, int position)
    {
        Account account = mAccountList.get(position);
        if (account == null)
        {
            return;
        }
        holder.bind(account);
    }
    
    @Override
    public int getItemCount()
    {
        return mAccountList.size();
    }
    
    //region NESTED CLASSES
    static class AccountViewHolder extends RecyclerView.ViewHolder
    {
        private RowItemAccountsListBinding mBinding;
        
        AccountViewHolder(@NonNull RowItemAccountsListBinding binding)
        {
            super(binding.getRoot());
            this.mBinding = binding;
        }
        
        void bind(Account account)
        {
            mBinding.setAccount(account);
            mBinding.invalidateAll();
            mBinding.executePendingBindings();
        }
    }
    //endregion
}
