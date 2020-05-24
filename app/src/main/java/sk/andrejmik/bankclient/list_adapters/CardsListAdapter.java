package sk.andrejmik.bankclient.list_adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sk.andrejmik.bankclient.R;
import sk.andrejmik.bankclient.databinding.RowItemCardsListBinding;
import sk.andrejmik.bankclient.objects.Card;
import sk.andrejmik.bankclient.objects.Person;

public class CardsListAdapter extends RecyclerView.Adapter<CardsListAdapter.CardViewHolder>
{
    private List<Card> mCardList;
    private Person mOwner;
    
    public CardsListAdapter(List<Card> cardList, Person owner)
    {
        mCardList = cardList;
        mOwner = owner;
    }
    
    public List<Card> getCardList()
    {
        return mCardList;
    }
    
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RowItemCardsListBinding itemCardsListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_item_cards_list,
                                                                               parent, false);
        return new CardsListAdapter.CardViewHolder(itemCardsListBinding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position)
    {
        Card card = mCardList.get(position);
        if (card == null)
        {
            return;
        }
        holder.bind(card, mOwner);
    }
    
    @Override
    public int getItemCount()
    {
        return mCardList.size();
    }
    
    //region NESTED CLASSES
    static class CardViewHolder extends RecyclerView.ViewHolder
    {
        private RowItemCardsListBinding mBinding;
        
        CardViewHolder(@NonNull RowItemCardsListBinding binding)
        {
            super(binding.getRoot());
            this.mBinding = binding;
        }
        
        void bind(Card card, Person person)
        {
            mBinding.setCard(card);
            mBinding.setOwner(person);
            mBinding.invalidateAll();
            mBinding.executePendingBindings();
        }
    }
    //endregion
}
