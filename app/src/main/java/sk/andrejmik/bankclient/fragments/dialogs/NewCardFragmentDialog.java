package sk.andrejmik.bankclient.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import sk.andrejmik.bankclient.R;
import sk.andrejmik.bankclient.databinding.FragmentDialogNewCardBinding;
import sk.andrejmik.bankclient.objects.Card;
import sk.andrejmik.bankclient.utils.Globals;

/**
 * Dialog fragment for adding new card to account
 */
public class NewCardFragmentDialog extends DialogFragment
{
    private FragmentDialogNewCardBinding mBinding;
    private Card newCard;
    
    public NewCardFragmentDialog()
    {
        // Required empty public constructor
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_dialog_new_card, null, false);
        View rootView = mBinding.getRoot();
        newCard = new Card();
        mBinding.setCard(newCard);
        return new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.add_card))
                                                     .setView(rootView)
                                                     .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                                                     {
                                                         @Override
                                                         public void onClick(DialogInterface dialogInterface, int i)
                                                         {
                                                             /* Confirm new card - send new card in intent to calling fragment */
                                                             mBinding.invalidateAll();
                                                             newCard.setCardNo(Card.generateCardNo());
                                                             Intent intent = new Intent();
                                                             intent.putExtra("new_card", Globals.GSON.toJson(newCard));
                                                             getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                                                             dismiss();
                                                         }
                                                     })
                                                     .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                                                     {
                                                         @Override
                                                         public void onClick(DialogInterface dialogInterface, int i)
                                                         {
                                                            dismiss();
                                                         }
                                                     })
                                                     .create();
    }
}
