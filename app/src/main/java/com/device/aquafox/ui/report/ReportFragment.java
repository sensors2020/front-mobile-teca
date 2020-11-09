package com.device.aquafox.ui.report;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.device.aquafox.R;
import com.device.aquafox.data.ApiRequest;
import com.device.aquafox.data.ApiResponse;
import com.device.aquafox.data.Consumo;
import com.device.aquafox.data.ConsumoReporte;
import com.device.aquafox.data.Dashboard;
import com.device.aquafox.data.Devices;
import com.device.aquafox.data.FilterReport;
import com.device.aquafox.data.Login;
import com.device.aquafox.data.ReportLineRequest;
import com.device.aquafox.data.ReportPdfAquaRequest;
import com.device.aquafox.data.Services;
import com.device.aquafox.data.Session;
import com.device.aquafox.data.TokenRequest;
import com.device.aquafox.service.APIClient;
import com.device.aquafox.service.APIInterface;
import com.device.aquafox.service.SaveSharedPreference;
import com.device.aquafox.ui.dashboard.ServiceAdapter;
import com.device.aquafox.ui.home.ServiceAquaAdapter;
import com.device.aquafox.ui.login.LoginActivity;
import com.device.aquafox.ui.login.MainActivity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReportFragment extends Fragment  {

    private ReportViewModel reportViewModel;
    APIInterface apiInterface;
    private ProgressBar spinner;

    List<ConsumoReporte> listadoConsumo = new ArrayList<ConsumoReporte>();
    List<Consumo> listado = new ArrayList<Consumo>();
    List<FilterReport> lista = new ArrayList<FilterReport>();

    ListView listServiciosFilter;

    private Typeface mTf;
    private String selected = "";



    private String[]header={"ID","Nombre","Apellido"};
    private String shortText="Hola";
    private String longText="Nunca consideres el estudio como una oblicación.";
    private TemplatePDF templatePDF;

    private String selectedPeriodo = "";
    private String selectedDevice = "";
    private Devices currentDevices = new Devices();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportViewModel =
                ViewModelProviders.of(this).get(ReportViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_report, container, false);


        apiInterface = APIClient.getClient().create(APIInterface.class);
        spinner = (ProgressBar) root.findViewById(R.id.progressBarHome);
        spinner.setVisibility(View.VISIBLE);




        final Spinner periodo = root.findViewById(R.id.spinner1);
        final Spinner servicios = root.findViewById(R.id.spinner2);
        //periodo.setVisibility(View.GONE);

        //lista.add(new FilterReport("e","Elije Filtro"));
        lista.add(new FilterReport("d","Semana"));
        lista.add(new FilterReport("m","Mes"));
        lista.add(new FilterReport("a","Año"));
        lista.add(new FilterReport("h","Dia"));
        FilterAquaAdapter adapter = new FilterAquaAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, lista);
        periodo.setAdapter(adapter);



        //String token = SaveSharedPreference.getToken(getActivity().getApplicationContext());
        final String token = SaveSharedPreference.getToken(getActivity().getApplicationContext());
        TokenRequest req = new TokenRequest(token);

        Call<ApiResponse> call = apiInterface.listOnlyServices(new ApiRequest(req));
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                System.out.println(response.body().getBody());
                if (response.body().getBody().getError() == null) {
                    ObjectMapper oMapper = new ObjectMapper();
                    List<Devices> lista = oMapper.convertValue(response.body().getBody().getBody(),
                            new TypeReference<List<Devices>>(){});

                    DevicesAdapter servicesAdapter = new DevicesAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, lista);
                    servicios.setAdapter(servicesAdapter);
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




        servicios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                int index = arg0.getSelectedItemPosition();
                Devices filter = (Devices) servicios.getSelectedItem();
                selectedDevice = filter.getSerie();
                currentDevices = filter;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        periodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                int index = arg0.getSelectedItemPosition();
                FilterReport filter = (FilterReport) periodo.getSelectedItem();
                selectedPeriodo = filter.getCode();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        final Tabla tabla = new Tabla(getActivity(), (TableLayout)root.findViewById(R.id.tabla));


        //final Button filterExporta = root.findViewById(R.id.btnExporta);
        final Button filterButton = root.findViewById(R.id.btnBuscar);
        //filterButton.setVisibility(View.GONE);
        final LinearLayout linea = root.findViewById(R.id.layoutTabla);
        linea.setVisibility(View.GONE);

        final TextView textServicio = root.findViewById(R.id.textServicio);
        final TextView textSerie = root.findViewById(R.id.textSerie);
        final TextView textAlias = root.findViewById(R.id.textAlias);
        final TextView textPeriodo = root.findViewById(R.id.textPeriodo);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle((R.string.reportes));
        //getActivity().getActionBar().setTitle(R.string.reportes);

        filterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String token = SaveSharedPreference.getToken(getActivity().getApplicationContext());
                spinner.setVisibility(View.VISIBLE);
                String device =  selectedDevice;
                String periodoSelect =selectedPeriodo;
                ///loadReport(token, device, periodoSelect, v, tabla);

                tabla.clean();


                ReportLineRequest req = new ReportLineRequest();
                req.setToken(token);
                req.setDevice(device);
                req.setPeriodo(periodoSelect);
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

                            linea.setVisibility(View.VISIBLE);
                            tabla.agregarCabecera(R.array.cabecera_tabla);

                            //
                            textServicio.setText("Servicio: "+currentDevices.getDescripcion_servicio());
                            textSerie.setText("ID: "+currentDevices.getSerie());
                            textAlias.setText("Alias: "+currentDevices.getAlias());
                            textPeriodo.setText("Periodo: "+currentDevices.getInscripcion()+" - "+currentDevices.getFinalizacion());

                            ArrayList<String> elementos = new ArrayList<String>();
                            float monto = 0;
                            float consumosmetro3 = 0;
                            for(Consumo bean: listado) {
                                System.out.println("SERIE::::::::::::::::::::::"+bean.getValues());
                                elementos = new ArrayList<String>();
                                elementos.add(bean.getValues());
                                elementos.add(String.valueOf(bean.getMetro3()));
                                elementos.add(String.valueOf(bean.getMonto()));

                                monto = monto + Float.parseFloat(String.valueOf(bean.getMonto()));
                                consumosmetro3 = consumosmetro3 + Float.parseFloat(String.valueOf(bean.getMetro3()));
                                tabla.agregarFilaTabla(elementos);
                            }

                            DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
                            separadoresPersonalizados.setDecimalSeparator('.');
                            DecimalFormat formato1 = new DecimalFormat("#.000", separadoresPersonalizados);
                            DecimalFormat formato2 = new DecimalFormat("#.00", separadoresPersonalizados);

                            elementos = new ArrayList<String>();
                            elementos.add("Total");
                            //elementos.add(String.valueOf(formato1.format(consumosmetro3)));
                            elementos.add(String.valueOf(consumosmetro3));
                            elementos.add(String.valueOf(formato2.format(monto)));
                            tabla.agregarFilaTabla(elementos);



                            //drawLineChart(root, listado);
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






                spinner.setVisibility(View.GONE);

            }

        });



        return root;
    }


    public void loadReport(String token, String device, String periodo, final View root, final Tabla tabla ) {
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

                    ArrayList<String> elementos = new ArrayList<String>();
                    for(Consumo bean: listado) {
                        System.out.println("SERIE::::::::::::::::::::::"+bean.getValues());
                        elementos = new ArrayList<String>();
                        elementos.add(bean.getValues());
                        elementos.add(String.valueOf(bean.getMetro3()));
                        elementos.add(String.valueOf(bean.getMonto()));

                        tabla.agregarFilaTabla(elementos);
                    }




                    //drawLineChart(root, listado);
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




    public void exportar(View view, String path)  {
        try {
            //templatePDF.appViewPDF(getActivity());
            templatePDF.exporta(getActivity(), path);
        }catch(UnsupportedEncodingException e) {
            System.out.println("Error:::"+e.getMessage());
        }

    }

    public ArrayList<String[]> getClients() {
        ArrayList<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"1","Pedro","Lopez"});
        rows.add(new String[]{"2","Mario","Lopez"});
        rows.add(new String[]{"3","Juan","Lopez"});
        return rows;
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void loadReport(String token, final View root) {
        ReportLineRequest req = new ReportLineRequest();
        req.setToken(token);
        req.setPeriodo("m");

        Call<ApiResponse> call = apiInterface.getReportLineChart(new ApiRequest(req));
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
                    //////////////////GRAFICO DE BARRAS
                    //data = getData(listado);
                    //setupChart(mCharts[0], data, mColors[0]);
                    //drawLineChart(root, listado);
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



    private List<Entry> getDataSet(List<Consumo> listado)  {
        List<Entry> lineEntries = new ArrayList<Entry>();
        int x=0;
        for(Consumo c: listado) {
            lineEntries.add(new Entry(x, c.getLitros()));
            x++;
        }
 /*
        lineEntries.add(new Entry(0, 1));
        lineEntries.add(new Entry(1, 2));
        lineEntries.add(new Entry(2, 3));
        lineEntries.add(new Entry(3, 4));
        lineEntries.add(new Entry(4, 2));
        lineEntries.add(new Entry(5, 3));
        lineEntries.add(new Entry(6, 1));
        lineEntries.add(new Entry(7, 5));
        lineEntries.add(new Entry(8, 7));
        lineEntries.add(new Entry(9, 6));
        lineEntries.add(new Entry(10, 4));
        lineEntries.add(new Entry(11, 5));
*/
        return lineEntries;
    }





}
