package com.device.aquafox.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.device.aquafox.R;
import com.device.aquafox.service.PreferencesUtility;
import com.device.aquafox.service.SaveSharedPreference;
import com.device.aquafox.ui.notifications.NotificationsViewModel;


public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView profileNombre;
    private TextView profileEmail;
    private TextView profileTelefono;
    private TextView profileDocumento;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_profile, container, false);

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profileNombre = (TextView) root.findViewById(R.id.profileNombre);
        profileEmail = (TextView) root.findViewById(R.id.profileEmail);
        profileTelefono = (TextView) root.findViewById(R.id.profileTelefono);
        profileDocumento = (TextView) root.findViewById(R.id.profileDocumento);

        String nombre = SaveSharedPreference.getValue(getActivity().getApplicationContext(), PreferencesUtility.DATA_NOMBRE)
                + " "+ SaveSharedPreference.getValue(getActivity().getApplicationContext(), PreferencesUtility.DATA_APELLIDO);

        String email = SaveSharedPreference.getValue(getActivity().getApplicationContext(), PreferencesUtility.DATA_EMAIL);
        String telefono = SaveSharedPreference.getValue(getActivity().getApplicationContext(), PreferencesUtility.DATA_TELEFONO);
        String documento = SaveSharedPreference.getValue(getActivity().getApplicationContext(), PreferencesUtility.DATA_TIPODOC)
                + " "+ SaveSharedPreference.getValue(getActivity().getApplicationContext(), PreferencesUtility.DATA_NUMDOC);

        profileNombre.setText(nombre);
        profileTelefono.setText(telefono);
        profileEmail.setText(email);
        profileDocumento.setText(documento);

        return root;
    }
}
