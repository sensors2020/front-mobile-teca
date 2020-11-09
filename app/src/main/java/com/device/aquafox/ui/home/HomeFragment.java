package com.device.aquafox.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.device.aquafox.R;

import com.device.aquafox.data.ApiRequest;
import com.device.aquafox.data.ApiResponse;
import com.device.aquafox.data.Consumo;
import com.device.aquafox.data.Dashboard;
import com.device.aquafox.data.FilterReport;
import com.device.aquafox.data.ReportLineRequest;
import com.device.aquafox.data.TokenRequest;
import com.device.aquafox.service.APIClient;
import com.device.aquafox.service.APIInterface;
import com.device.aquafox.service.SaveSharedPreference;
import com.device.aquafox.ui.login.MainActivity;
import com.device.aquafox.ui.report.FilterAquaAdapter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private PieChart pieChart;
    private HomeViewModel homeViewModel;
    APIInterface apiInterface;
    private ProgressBar spinner;
    List<Consumo> listado = new ArrayList<Consumo>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        spinner = (ProgressBar) root.findViewById(R.id.progressBarHome);
        spinner.setVisibility(View.GONE);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle((R.string.pie_chart));

        List<FilterReport> lista = new ArrayList<FilterReport>();

        final Spinner periodo = root.findViewById(R.id.periodo);
        lista.add(new FilterReport("d","Día"));
        lista.add(new FilterReport("m","Mes"));
        lista.add(new FilterReport("a","Año"));
        FilterAquaAdapter adapter = new FilterAquaAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, lista);
        periodo.setAdapter(adapter);

        final String token = SaveSharedPreference.getToken(getActivity().getApplicationContext());
        spinner.setVisibility(View.VISIBLE);
        final String device =  String.valueOf(getArguments().getString("serie"));
        final String tipoperiodo = "m";
        //loadReport(token, device, tipoperiodo, root);

        periodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                int index = arg0.getSelectedItemPosition();
                FilterReport filter = (FilterReport) periodo.getSelectedItem();
                System.out.println("ITEM::::::::::::::::::::::"+String.valueOf(filter.getCode()));
                String periodoSelect = filter.getCode();
                loadReport(token, device, periodoSelect, root);

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });



        return root;
    }


    public void loadReport(String token, String device,  String periodo, final View root) {
        ReportLineRequest req = new ReportLineRequest();
        req.setToken(token);
        req.setDevice(device);
        req.setPeriodo(periodo);

        Call<ApiResponse> call = apiInterface.getDashboardByDevice(new ApiRequest(req));
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.body().getBody().getError() == null) {
                    System.out.println("getDashboard OK::");
                    ObjectMapper oMapper = new ObjectMapper();
                    Dashboard dashboard = oMapper.convertValue(response.body().getBody().getBody(), Dashboard.class);
                    //new TypeReference<List<Dashboard>>(){});
                    listado = dashboard.getConsumos();
                    String anio = String.valueOf(dashboard.getAnio());
                    System.out.println("dashboard::::::::::::::::::::::"+anio);

                    drawLineChart(root, listado);
                    spinner.setVisibility(View.GONE);
                } else {
                    System.out.println("Dashboard ERROR:");
                    spinner.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                System.out.println("Dashboard Error General:");
                spinner.setVisibility(View.GONE);
                call.cancel();
            }
        });

    }


    private void drawLineChart(View root, final List<Consumo> listado) {

        //////////////////GRAFICO DE BARRAS
        BarChart chart = (BarChart) root.findViewById(R.id.barChart);

        ArrayList NoOfEmp = new ArrayList();

        int indice = 0;
        for(Consumo bean: listado) {
            NoOfEmp.add(new BarEntry(bean.getLitros(), indice));
            //NoOfEmp.add(new BarEntry(bean.getMetro3(), indice));
            indice++;
        }

        ArrayList year = new ArrayList();

        for(Consumo bean: listado) {
            //String periodo = bean.getMes()+" "+anio;
            String periodo = bean.getValues();
            year.add(periodo);
        }

        BarDataSet bardataset = new BarDataSet(NoOfEmp, "CONSUMO LITROS");
        chart.animateY(5000);
        BarData datas = new BarData(year, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(datas);



    }

}
