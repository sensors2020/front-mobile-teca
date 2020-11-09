package com.device.aquafox.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.device.aquafox.R;
import com.device.aquafox.data.ApiRequest;
import com.device.aquafox.data.ApiResponse;
import com.device.aquafox.data.Session;
import com.device.aquafox.data.TokenRequest;
import com.device.aquafox.service.APIClient;
import com.device.aquafox.service.APIInterface;
import com.device.aquafox.service.SaveSharedPreference;
import com.device.aquafox.ui.login.LoginActivity;
import com.device.aquafox.ui.login.MainActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private ProgressBar spinner;
    APIInterface apiInterface;

    ListView simpleList;
    String countryList[] = {"Perfil", "Cambio de Clave", "Salir"};
    int flags[] = {R.drawable.profile, R.drawable.seguridad, R.drawable.logout};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        spinner = (ProgressBar) root.findViewById(R.id.progressBarNotification);
        spinner.setVisibility(View.GONE);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle((R.string.profiles));

        /*
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */
        //getActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Fragment fragmentProfile = new ProfileFragment();
        //FragmentTransaction transaction =  getActivity().getSupportFragmentManager().beginTransaction();
        //FragmentTransaction transaction =  getChildFragmentManager().beginTransaction();
        //FragmentTransaction transaction = getFragmentManager().beginTransaction();
       /*
        OpcionesFragment fragment = new OpcionesFragment();
        System.out.println("::::::FRAGMENT NOTIFICATION:::::");
        //getFragmentManager().popBackStack();
        FragmentTransaction transaction =  getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        //transaction.addToBackStack(null);
        transaction.disallowAddToBackStack();
        transaction.commit();
*/

        simpleList = (ListView) root.findViewById(R.id.simpleListView);
        //CustomAdapter customAdapter = new CustomAdapter(root.getApplicationContext(), countryList, flags);
        CustomAdapter customAdapter = new CustomAdapter(getActivity().getApplicationContext(), countryList, flags);
        simpleList.setAdapter(customAdapter);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("POSITION::"+position);
                System.out.println(countryList[position]);
                if(countryList[position].equals("Salir")) {

                    /*
                    SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                    String token = sharedPref.getString(getString(R.string.preference_file_key), null);
                    System.out.println("Fragment token::" + token);
                    */

                    String token = SaveSharedPreference.getToken(getActivity().getApplicationContext());
                    System.out.println("Fragment token::" + token);
                    //Clean Session
                    SaveSharedPreference.setLoggedIn(getActivity().getApplicationContext(),false, null);

                    System.out.println("Fragment Load automatico:::"+SaveSharedPreference.getLoggedStatus(getActivity().getApplicationContext()));

                    System.out.println(getActivity().getApplicationContext().toString());

                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);

                    TokenRequest req = new TokenRequest(token);

                    spinner.setVisibility(View.VISIBLE);
                    Call<ApiResponse> call = apiInterface.getLogout(new ApiRequest(req));
                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                            System.out.println(response.body().getBody());
                            if (response.body().getBody().getError() == null) {
                                System.out.println("logout OK::");
                                spinner.setVisibility(View.GONE);
                            } else {
                                System.out.println("login ERROR::");
                                spinner.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            spinner.setVisibility(View.GONE);
                            call.cancel();
                        }
                    });




                }
                if(countryList[position].equals("Perfil")) {
                    System.out.println("Profile");
                    Navigation.findNavController(view).navigate(R.id.action_navigation_notifications_to_profileFragment);
               }
                if( position == 1) {
                    System.out.println("Cambio de clave");
                    Navigation.findNavController(view).navigate(R.id.action_navigation_notifications_to_securityFragment);
                }


            }
        });


        return root;
    }



}
