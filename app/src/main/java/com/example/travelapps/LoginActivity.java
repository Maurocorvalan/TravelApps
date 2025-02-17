package com.example.travelapps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class LoginActivity extends AppCompatActivity {
    private EditText correoEditText, contrasenaEditText;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correoEditText = findViewById(R.id.correoEditText);
        contrasenaEditText = findViewById(R.id.contrasenaEditText);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.getToken().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String token) {
                if (token != null) {
                    // Guarda el token en SharedPreferences
                    saveToken(token);

                    // Navega a MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();  // Finaliza la actividad de login para evitar que el usuario regrese a ella con el botón de atrás
                }
            }
        });

        loginViewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.loginButton).setOnClickListener(v -> {
            String correo = correoEditText.getText().toString();
            String contrasena = contrasenaEditText.getText().toString();
            loginViewModel.login(correo, contrasena);
        });
    }

    private void saveToken(String token) {
        // Aquí puedes usar SharedPreferences para guardar el token
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jwt_token", token);
        editor.apply();
    }
}