package com.device.aquafox.ui.login;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.device.aquafox.R;
import com.device.aquafox.data.ApiRequest;
import com.device.aquafox.data.ApiResponse;
import com.device.aquafox.data.Login;
import com.device.aquafox.data.Session;

import com.device.aquafox.service.APIClient;
import com.device.aquafox.service.APIInterface;
import com.device.aquafox.service.SaveSharedPreference;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    APIInterface apiInterface;
    private ProgressBar spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        //final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        spinner = (ProgressBar)findViewById(R.id.progressBarLogin);
        spinner.setVisibility(View.GONE);

        //VISIBLE: muestra   GONE: oculta
        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            System.out.println("Login Load automatico:::"+SaveSharedPreference.getLoggedStatus(getApplicationContext()));
            System.out.println(getApplicationContext().toString());

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        /*
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });


        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                //loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    //lanzar(this);
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });
*/

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean execute = true;
                if(usernameEditText.getText() == null || usernameEditText.getText().toString().equals(""))  {
                    execute = false;
                    showMessage("Debe ingresar usuario.");
                }
                if(passwordEditText.getText() == null || passwordEditText.getText().toString().equals(""))  {
                    execute = false;
                    showMessage("Debe ingresar su contraseña..");
                }

                if(execute) {
                    Login login = new Login();
                    login.setEmail(usernameEditText.getText().toString());
                    login.setPassword(passwordEditText.getText().toString());
                    spinner.setVisibility(View.VISIBLE);

                    Call<ApiResponse> call = apiInterface.getLogin(new ApiRequest(login));
                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                            System.out.println(response.body().getBody());
                            if (response.body().getBody().getError() == null) {
                                System.out.println("login OK::");
                                ObjectMapper oMapper = new ObjectMapper();
                                Session session = oMapper.convertValue(response.body().getBody().getBody(), Session.class);
                                System.out.println("Session.token::" + session.getToken());

                            /*
                            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.token), session.getToken());
                            editor.commit();
                            */
                                SaveSharedPreference.setLoggedIn(getApplicationContext(), true, session);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                spinner.setVisibility(View.GONE);
                            } else {
                                System.out.println("login ERROR::");
                                showMessage("Usuario y /o Contraseña incorrectos.");
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

                }


            }
        });


    }


    public void lanzar(View view) {
        Intent i = new Intent(this, MainActivity.class );
        startActivity(i);
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
