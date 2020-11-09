package com.device.aquafox.service;

import com.device.aquafox.data.AliasRequest;
import com.device.aquafox.data.ApiRequest;
import com.device.aquafox.data.ApiResponse;
import com.device.aquafox.data.Clave;
import com.device.aquafox.data.Login;
import com.device.aquafox.data.ReportLineRequest;
import com.device.aquafox.data.ReportPdfAquaRequest;
import com.device.aquafox.data.TokenRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {

    String API_ROUTE_LOGIN = "client/session/login";
    String API_ROUTE_LOGOUT = "client/session/logout";
    String API_ROUTE_SERVICES = "client/services/aqua/list";
    String API_ROUTE_SERVICESRESUMEN = "client/services/aqua/list/resumen";
    String API_ROUTE_SERVICESUPDATEALIAS = "client/services/update/alias";
    String API_ROUTE_SERVICES_LISTADO= " client/services/aqua/listado";

    String API_ROUTE_SERVICES_RPTPDF = "client/reporte/aqua/pdf";
    String API_ROUTE_SERVICES_RPTFILTER = "client/reporte/aqua/filter";

    String API_ROUTE_CLAVE = "client/session/clave";
    String API_ROUTE_DASHBOARD = "client/reporte/dashboard";

    String API_ROUTE_REPORT_LINECHART = "client/reporte/aqua/linechart";
    String API_ROUTE_DASHBOARD_BYDEVICE = "client/reporte/aqua/dashboarddevice ";


    @POST(API_ROUTE_LOGIN)
    Call<ApiResponse> getLogin(@Body ApiRequest<Login> request);

    @POST(API_ROUTE_LOGOUT)
    Call<ApiResponse> getLogout(@Body ApiRequest<TokenRequest> request);


    @POST(API_ROUTE_SERVICESUPDATEALIAS)
    Call<ApiResponse> updateServiceALias(@Body ApiRequest<AliasRequest> request);

    @POST(API_ROUTE_SERVICES)
    Call<ApiResponse> listServices(@Body ApiRequest<Login> request);

    @POST(API_ROUTE_SERVICESRESUMEN)
    Call<ApiResponse> listServicesResumen(@Body ApiRequest<Login> request);

    @POST(API_ROUTE_SERVICES_LISTADO)
    Call<ApiResponse> listOnlyServices(@Body ApiRequest<TokenRequest> request);


    @POST(API_ROUTE_CLAVE)
    Call<ApiResponse> changePassword(@Body ApiRequest<Clave> request);

    @POST(API_ROUTE_REPORT_LINECHART)
    Call<ApiResponse> getReportLineChart(@Body ApiRequest<ReportLineRequest> request);


    @POST(API_ROUTE_DASHBOARD_BYDEVICE)
    Call<ApiResponse> getDashboardByDevice(@Body ApiRequest<ReportLineRequest> request);

    @POST(API_ROUTE_DASHBOARD)
    Call<ApiResponse> getDashboard(@Body ApiRequest<TokenRequest> request);

    @POST(API_ROUTE_SERVICES_RPTPDF)
    Call<ApiResponse> getReportPdf(@Body ApiRequest<ReportPdfAquaRequest> request);
    @POST(API_ROUTE_SERVICES_RPTFILTER)
    Call<ApiResponse> getReportFilter(@Body ApiRequest<ReportPdfAquaRequest> request);



}
