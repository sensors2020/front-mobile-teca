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
import com.device.aquafox.data.AliasRequest;
import com.device.aquafox.data.ApiRequest;
import com.device.aquafox.data.ApiResponse;
import com.device.aquafox.data.Login;
import com.device.aquafox.data.Services;
import com.device.aquafox.data.Session;
import com.device.aquafox.data.TokenRequest;
import com.device.aquafox.service.APIClient;
import com.device.aquafox.service.APIInterface;
import com.device.aquafox.service.SaveSharedPreference;
import com.device.aquafox.ui.dashboard.ServiceAdapter;
import com.device.aquafox.ui.login.LoginActivity;
import com.device.aquafox.ui.login.MainActivity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetalleServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetalleServiceFragment extends Fragment {


    private EditText serviceNombre;
    //private TextView serviceNombre;
    private TextView serviceSerie;
    private TextView serviceDireccion;
    private TextView serviceConsumo;
    private TextView serviceFecha;

    private Button update;
    private Button cancel;

    APIInterface apiInterface;
    private ProgressBar spinner;

    public DetalleServiceFragment() {
        // Required empty public constructor
    }

    public static DetalleServiceFragment newInstance(String param1, String param2) {
        DetalleServiceFragment fragment = new DetalleServiceFragment();
        /*
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        */
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("DESCRIPCION SERVICIO:::::::::"+
        getArguments().getString("descripcion_servicio")
        );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detalle_service, container, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        spinner = (ProgressBar) root.findViewById(R.id.progressBarServices);
        spinner.setVisibility(View.GONE);
        //serviceNombre = (TextView) root.findViewById(R.id.serviceNombre);
        serviceNombre = (EditText) root.findViewById(R.id.serviceNombre);

        serviceSerie = (TextView) root.findViewById(R.id.serviceSerie);
        serviceDireccion = (TextView) root.findViewById(R.id.serviceDireccion);
        serviceConsumo = (TextView) root.findViewById(R.id.serviceConsumo);
        serviceFecha = (TextView) root.findViewById(R.id.serviceFecha);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle((R.string.pie_chart));

        serviceNombre.setText(getArguments().getString("alias"));
        serviceSerie.setText(getArguments().getString("serie"));
        serviceDireccion.setText(getArguments().getString("direccion"));
        serviceConsumo.setText(getArguments().getString("consumo"));
        String fecha = getArguments().getString("inscripcion")+" - "+getArguments().getString("finalizacion");
        serviceFecha.setText(fecha);
        //serviceFechaFinsetText(getArguments().getString("fecha_finalizacion"));

        update = (Button) root.findViewById(R.id.update);
        //cancel = (Button) root.findViewById(R.id.cancel);



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean execute = true;
                if(serviceNombre.getText() == null || serviceNombre.getText().toString().equals(""))  {
                    execute = false;
                    showMessage("Debe ingresar Alias.");
                }

                if(execute) {
                    spinner.setVisibility(View.VISIBLE);
                    String token = SaveSharedPreference.getToken(getActivity().getApplicationContext());
                    System.out.println("Fragment token::" + token);
                    AliasRequest requestAlias = new AliasRequest();
                    requestAlias.setToken(token);
                    requestAlias.setDevice(serviceSerie.getText().toString());
                    requestAlias.setAlias(serviceNombre.getText().toString());
                    Call<ApiResponse> call = apiInterface.updateServiceALias(new ApiRequest(requestAlias));
                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                            System.out.println(response.body().getBody());
                            if (response.body().getBody().getError() == null) {
                                System.out.println("Respomse OK::");
                                spinner.setVisibility(View.GONE);
                                showMessage("Operación realizada correctamente");
                            } else {
                                System.out.println("List ERROR::");
                                spinner.setVisibility(View.GONE);
                                showMessage("Ocurrió un problema, volver a intentralo.");
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            showMessage("Ocurrió un error, vuelva a itnentarlo.");
                            spinner.setVisibility(View.GONE);
                            call.cancel();
                        }
                    });

                }




            }
        });

        return root;
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
