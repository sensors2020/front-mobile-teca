package com.device.aquafox.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.device.aquafox.R;
import com.device.aquafox.data.ApiRequest;
import com.device.aquafox.data.ApiResponse;
import com.device.aquafox.data.Services;
import com.device.aquafox.data.Session;
import com.device.aquafox.data.TokenRequest;
import com.device.aquafox.service.APIClient;
import com.device.aquafox.service.APIInterface;
import com.device.aquafox.service.SaveSharedPreference;
import com.device.aquafox.ui.login.LoginActivity;
import com.device.aquafox.ui.login.MainActivity;
import com.device.aquafox.ui.notifications.CustomAdapter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    ListView listServicios;
    String countryList[] = {"Medidor de Agua", "Medidor de Agua"};
    //int flags[] = {R.drawable.usrusr, R.drawable.pswd, R.drawable.usrusr, R.drawable.pswd};
    int flags[] = {R.drawable.medidor, R.drawable.medidor};

    List<Services> listado = new ArrayList<Services>();
    APIInterface apiInterface;
    private ProgressBar spinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        spinner = (ProgressBar) root.findViewById(R.id.progressBarServices);
        //TextView  text_dashboard = (TextView) root.findViewById(R.id.text_dashboard);
        //text_dashboard.setText("Mis Dispositivos");
        listServicios = (ListView) root.findViewById(R.id.listServices);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle((R.string.devices));

        spinner.setVisibility(View.VISIBLE);
        String token = SaveSharedPreference.getToken(getActivity().getApplicationContext());
        System.out.println("Fragment token::" + token);
        TokenRequest req = new TokenRequest(token);

        Call<ApiResponse> call = apiInterface.listServicesResumen(new ApiRequest(req));
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                System.out.println(response.body().getBody());
                if (response.body().getBody().getError() == null) {
                    System.out.println("login OK::");
                    ObjectMapper oMapper = new ObjectMapper();
                    List<Services> lista = oMapper.convertValue(response.body().getBody().getBody(),
                            new TypeReference<List<Services>>(){});
                    listado = lista;
                    /*
                    for(Services bean: lista) {
                        System.out.println("SERIE::::::::::::::::::::::"+bean.getSerie());
                    }*/

                    ServiceAdapter servicesAdapter = new ServiceAdapter(getActivity().getApplicationContext(), lista);
                    listServicios.setAdapter(servicesAdapter);
                    spinner.setVisibility(View.GONE);


                } else {
                    System.out.println("List ERROR::");
                    spinner.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                spinner.setVisibility(View.GONE);
                call.cancel();
            }
        });




        listServicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("POSITION::"+position);
                Bundle bundle = new Bundle();
                bundle.putString("serie", listado.get(position).getSerie());
                Navigation.findNavController(view).navigate(R.id.action_navigation_dashboard_to_homeFragment, bundle);
            }
        });


        return root;
    }
}
