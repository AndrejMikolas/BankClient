package sk.andrejmik.bankclient.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.andrejmik.bankclient.R;
import sk.andrejmik.bankclient.databinding.FragmentDialogNewCardBinding;
import sk.andrejmik.bankclient.objects.Card;
import sk.andrejmik.bankclient.utils.Globals;

/**
 * A simple {@link Fragment} subclass.
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
    
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//    {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_dialog_new_card, container, false);
//    }
}
