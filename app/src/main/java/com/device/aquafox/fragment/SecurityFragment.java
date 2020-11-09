package com.device.aquafox.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.device.aquafox.R;
import com.device.aquafox.data.ApiRequest;
import com.device.aquafox.data.ApiResponse;
import com.device.aquafox.data.Clave;
import com.device.aquafox.data.Login;
import com.device.aquafox.data.Session;
import com.device.aquafox.service.APIClient;
import com.device.aquafox.service.APIInterface;
import com.device.aquafox.service.SaveSharedPreference;
import com.device.aquafox.ui.login.LoginActivity;
import com.device.aquafox.ui.login.MainActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecurityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecurityFragment extends Fragment {

    private EditText claveActual;
    private EditText claveNueva;
    private Button btnClave;
    private ProgressBar spinner;
    APIInterface apiInterface;

    public SecurityFragment() {
        // Required empty public constructor
    }

    public static SecurityFragment newInstance(String param1, String param2) {
        SecurityFragment fragment = new SecurityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_security, container, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        spinner = (ProgressBar) root.findViewById(R.id.progressBarClave);
        spinner.setVisibility(View.GONE);

        claveActual = root.findViewById(R.id.claveActual);;
        claveNueva = root.findViewById(R.id.claveNueva);;
        btnClave = root.findViewById(R.id.btnClave);;
        btnClave.setEnabled(true);

        btnClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int bandera = 0;
                if(claveActual.getText().toString() == null || claveActual.getText().toString().equals("")) {
                    bandera = 1;
                }
                if(claveNueva.getText().toString() == null || claveNueva.getText().toString().equals("")) {
                    bandera = 1;
                }

                if(bandera == 0) {
                    String token = SaveSharedPreference.getToken(getActivity().getApplicationContext());
                    Clave clave = new Clave(claveActual.getText().toString(), claveNueva.getText().toString(), token);

                    spinner.setVisibility(View.VISIBLE);

                    Call<ApiResponse> call = apiInterface.changePassword(new ApiRequest(clave));
                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                            if (response.body().getBody().getError() == null) {
                                System.out.println("changePassword OK::");
                                btnClave.setEnabled(false);
                                spinner.setVisibility(View.GONE);
                                showMessage("Operación realizada correctamente.");
                            } else {
                                System.out.println("changePassword ERROR::");
                                showMessage("Ocurrió un error, vuelva a itnentarlo.");
                                spinner.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            showMessage("Ocurrió un error, vuelva a itnentarlo.");
                            spinner.setVisibility(View.GONE);
                            call.cancel();
                        }
                    });
                } else {
                    showMessage("Debe ingresar un valor válido.");
                }

            }
        });


        return root;
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
