package sk.andrejmik.bankclient.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import sk.andrejmik.bankclient.R;
import sk.andrejmik.bankclient.databinding.FragmentSettingsBinding;
import sk.andrejmik.bankclient.utils.PreferencesManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment
{
    private FragmentSettingsBinding mBinding;
    
    public SettingsFragment()
    {
        // Required empty public constructor
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        setupFragment();
        return mBinding.getRoot();
    }
    
    private void setupFragment()
    {
        mBinding.edittextServerAddress.setText(PreferencesManager.getString(PreferencesManager.PREFERENCES_KEY_SERVER_ADDRESS));
        mBinding.edittextServerPort.setText(PreferencesManager.getString(PreferencesManager.PREFERENCES_KEY_SERVER_PORT));
        setupListeners();
    }
    
    private void setupListeners()
    {
        mBinding.buttonSettingsSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PreferencesManager.putString(PreferencesManager.PREFERENCES_KEY_SERVER_ADDRESS, mBinding.edittextServerAddress.getText().toString());
                PreferencesManager.putString(PreferencesManager.PREFERENCES_KEY_SERVER_PORT, mBinding.edittextServerPort.getText().toString());
                Toast.makeText(getContext(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
