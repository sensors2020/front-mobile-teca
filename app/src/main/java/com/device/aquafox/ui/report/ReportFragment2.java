package com.device.aquafox.ui.report;

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
import com.device.aquafox.data.FilterReport;
import com.device.aquafox.data.ReportLineRequest;
import com.device.aquafox.data.ReportPdfAquaRequest;
import com.device.aquafox.service.APIClient;
import com.device.aquafox.service.APIInterface;
import com.device.aquafox.service.SaveSharedPreference;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mikephil.charting.data.Entry;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReportFragment2 extends Fragment  {

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
    private String selectedFilter = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportViewModel =
                ViewModelProviders.of(this).get(ReportViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_report, container, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        spinner = (ProgressBar) root.findViewById(R.id.progressBarHome);
        spinner.setVisibility(View.GONE);

        //listServiciosFilter= (ListView) root.findViewById(R.id.listServicesFilter);
        //listServiciosFilter.setVisibility(View.GONE);
/*
        System.out.println("Permiso1::::");
        System.out.println(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
        System.out.println("Permiso2::::");
        System.out.println(PackageManager.PERMISSION_GRANTED);
*/

        templatePDF = new TemplatePDF(getActivity().getApplicationContext());
        templatePDF.openDocument();
        templatePDF.addMetaData("Clientes","Ventas","Marines");
        templatePDF.addTitle("Tienda Facil", "Cliente", "06/06/2020");
        templatePDF.addParagraph(shortText);
        templatePDF.addParagraph(longText);
        templatePDF.createTable(header,getClients());
        templatePDF.closeDocument();


        final Spinner dropdown = root.findViewById(R.id.spinner1);
        final Spinner periodo = root.findViewById(R.id.spinner2);
        periodo.setVisibility(View.GONE);

        lista.add(new FilterReport("e","Elije Filtro"));
        lista.add(new FilterReport("m","Mes"));
        lista.add(new FilterReport("a","Año"));
        FilterAquaAdapter adapter = new FilterAquaAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, lista);
        dropdown.setAdapter(adapter);


        final Tabla tabla = new Tabla(getActivity(), (TableLayout)root.findViewById(R.id.tabla));
        tabla.agregarCabecera(R.array.cabecera_tabla);

/*
        ArrayList<String> elementos = new ArrayList<String>();
        elementos.add("lince 2");
        elementos.add("30");
        elementos.add("240.00");
        tabla.agregarFilaTabla(elementos);

        elementos = new ArrayList<String>();
        elementos.add("Aquafox Rioso, Lince");
        elementos.add("50");
        elementos.add("320.00");
        tabla.agregarFilaTabla(elementos);

        elementos = new ArrayList<String>();
        elementos.add("Dispositivo 3A");
        elementos.add("10");
        elementos.add("150.00");
        tabla.agregarFilaTabla(elementos);
*/

        //String token = SaveSharedPreference.getToken(getActivity().getApplicationContext());
        final String token = SaveSharedPreference.getToken(getActivity().getApplicationContext());


        periodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                int index = arg0.getSelectedItemPosition();
                FilterReport filter = (FilterReport) periodo.getSelectedItem();
                System.out.println("ITEM::::::::::::::::::::::"+String.valueOf(filter.getCode()));
                selectedPeriodo = filter.getCode();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });




        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                int index = arg0.getSelectedItemPosition();
                FilterReport filter = (FilterReport) dropdown.getSelectedItem();
                System.out.println("ITEM::::::::::::::::::::::"+String.valueOf(filter.getCode()));
                if(!filter.getCode().equals("e")) {

                    selectedFilter = filter.getCode();

                    loadReport(token, root);
                    List<FilterReport> listPeriodo = new ArrayList<FilterReport>();
                    if(filter.getCode().equals("a")) {
                        listPeriodo.add(new FilterReport("2020","2020"));
                        selectedPeriodo = listPeriodo.get(0).getCode();
                    }
                    if(filter.getCode().equals("m")) {

                        Calendar calenadar = Calendar.getInstance();

                        SimpleDateFormat formatNowMonth = new SimpleDateFormat("MM");
                        SimpleDateFormat formatNowYear = new SimpleDateFormat("YYYY");
                        String currentMonth = formatNowMonth.format(calenadar.getTime());
                        String currentYear = formatNowYear.format(calenadar.getTime());
                        int w=0;
                        for (int z = 0; z < 12; z++) {
                            calenadar.add(Calendar.MONTH, -w);
                            w=1;
                            currentMonth = formatNowMonth.format(calenadar.getTime());
                            currentYear = formatNowYear.format(calenadar.getTime());
                            String mes = "";

                            if (currentMonth.equals("01"))
                                mes = "Enero " + currentYear;
                            else if (currentMonth.equals("02"))
                                mes = "Febrero " + currentYear;
                            else if (currentMonth.equals("03"))
                                mes = "Marzo " + currentYear;
                            else if (currentMonth.equals("04"))
                                mes = "Abril " + currentYear;
                            else if (currentMonth.equals("05"))
                                mes = "Mayo " + currentYear;
                            else if (currentMonth.equals("06"))
                                mes = "Junio " + currentYear;
                            else if (currentMonth.equals("07"))
                                mes = "Julio " + currentYear;
                            else if (currentMonth.equals("08"))
                                mes = "Agosto " + currentYear;
                            else if (currentMonth.equals("09"))
                                mes = "Setiembre " + currentYear;
                            else if (currentMonth.equals("10"))
                                mes = "Octubre " + currentYear;
                            else if (currentMonth.equals("11"))
                                mes = "Noviembre " + currentYear;
                            else if (currentMonth.equals("12"))
                                mes = "Diciembre " + currentYear;

                            listPeriodo.add(new FilterReport(currentYear+""+currentMonth,mes));
                            System.out.println("***************Mes:" + currentMonth + "=Año:" + currentYear);
                        }
                        selectedPeriodo = listPeriodo.get(0).getCode();

                    }
                    periodo.setVisibility(View.VISIBLE);
                    PeriodoAquaAdapter periodoAdapter = new PeriodoAquaAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, listPeriodo);
                    periodo.setAdapter(periodoAdapter);

                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        //final Button filterExporta = root.findViewById(R.id.btnExporta);
        final Button filterButton = root.findViewById(R.id.btnBuscar);
        //filterButton.setVisibility(View.GONE);
        final LinearLayout linea = root.findViewById(R.id.layoutTabla);
        linea.setVisibility(View.GONE);


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean execute = false;
                String token = SaveSharedPreference.getToken(getActivity().getApplicationContext());
                String codigo = SaveSharedPreference.getValue(getActivity().getApplicationContext(), "codigo");

                ReportPdfAquaRequest req = new ReportPdfAquaRequest();
                req.setPeriodo(selectedPeriodo);
                req.setToken(token);
                req.setCodigo(Integer.parseInt(codigo));
                req.setFilter(selectedFilter);

                if(!selectedFilter.equals("e") && !selectedFilter.equals("")) {
                    execute = true;
                }
                if(selectedPeriodo != null && !selectedPeriodo.equals("")) {
                    execute = true;
                }


                if(execute){
                    spinner.setVisibility(View.VISIBLE);

                    Call<ApiResponse> call = apiInterface.getReportFilter(new ApiRequest(req));
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                        System.out.println(response.body().getBody());
                        if (response.body().getBody().getError() == null) {
                            System.out.println("login OK::");
                            ObjectMapper oMapper = new ObjectMapper();
                            List<ConsumoReporte> lista = oMapper.convertValue(response.body().getBody().getBody(),
                                    new TypeReference<List<ConsumoReporte>>(){});
                            listadoConsumo = lista;
                            tabla.clean();

                            ArrayList<String> elementos = new ArrayList<String>();
                            for(ConsumoReporte bean: lista) {
                                System.out.println("SERIE::::::::::::::::::::::"+bean.getAlias());
                                elementos = new ArrayList<String>();
                                elementos.add(bean.getAlias());
                                elementos.add(String.valueOf(bean.getMetro3()));
                                elementos.add(String.valueOf(bean.getMonto()));


                                tabla.agregarFilaTabla(elementos);
                            }

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




                /*
                if(usernameEditText.getText() == null || usernameEditText.getText().toString().equals(""))  {
                    execute = false;
                    showMessage("Debe ingresar usuario.");
                }*/
                    filterButton.setVisibility(View.VISIBLE);
                    linea.setVisibility(View.VISIBLE);
                } else {
                    showMessage("Debe ingresar datos válidos.");
                }




            }
        });


/*
        filterExporta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean execute = false;

                String token = SaveSharedPreference.getToken(getActivity().getApplicationContext());
                String codigo = SaveSharedPreference.getValue(getActivity().getApplicationContext(), "codigo");

                ReportPdfAquaRequest req = new ReportPdfAquaRequest();
                req.setPeriodo(selectedPeriodo);
                req.setToken(token);
                req.setCodigo(Integer.valueOf(codigo));
                req.setFilter(selectedFilter);

                if(!selectedFilter.equals("e") && !selectedFilter.equals("")) {
                    execute = true;
                }
                if(selectedPeriodo != null && !selectedPeriodo.equals("")) {
                    execute = true;
                }


                if(execute){

                    Call<ApiResponse> call = apiInterface.getReportPdf(new ApiRequest(req));
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                        System.out.println(response.body().getBody());
                        if (response.body().getBody().getError() == null) {
                            System.out.println("::: OK::");
                            ObjectMapper oMapper = new ObjectMapper();
                            String ruta = oMapper.convertValue(response.body().getBody().getBody(),
                                    String.class);
                            exportar(root, ruta);


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
                } else {

                }

            }
        });

*/
        return root;
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
