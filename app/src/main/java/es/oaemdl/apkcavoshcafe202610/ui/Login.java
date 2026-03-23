package es.oaemdl.apkcavoshcafe202610.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import es.oaemdl.apkcavoshcafe202610.R;
import es.oaemdl.apkcavoshcafe202610.databinding.FragmentLoginBinding;

public class Login extends Fragment {
    FragmentLoginBinding binding;
    View view;
    Context context;
    NavController navController;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        TextInputLayout tilCorreo = view.findViewById(R.id.tilCorreo);
        TextInputLayout tilPassword = view.findViewById(R.id.tilPasswordd);

        EditText email = tilCorreo.getEditText();
        EditText password = tilPassword.getEditText();

        Button btnLogin = view.findViewById(R.id.btnIniciar);

        btnLogin.setOnClickListener(v -> {

            String correo = email.getText().toString();
            String contra = password.getText().toString();

            mAuth.signInWithEmailAndPassword(correo, contra)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            NavController navController = Navigation.findNavController(v);
                            navController.navigate(R.id.navigation_menu);

                        } else {
                            Toast.makeText(getContext(),
                                    "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

        });


        return view;
    }
    }