package es.oaemdl.apkcavoshcafe202610.ui;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import es.oaemdl.apkcavoshcafe202610.R;
import es.oaemdl.apkcavoshcafe202610.databinding.FragmentLoginBinding;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;


public class Login extends Fragment {
    FragmentLoginBinding binding;
    View view;
    Context context;
    NavController navController;

    GoogleSignInClient googleSignInClient;
    FirebaseAuth mAuth;

    int RC_SIGN_IN = 100;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);


        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        TextInputLayout tilCorreo = view.findViewById(R.id.tilCorreo);
        TextInputLayout tilPassword = view.findViewById(R.id.tilPasswordd);

        EditText email = tilCorreo.getEditText();
        EditText password = tilPassword.getEditText();

        Button btnLogin = view.findViewById(R.id.btnIniciar);
        Button btnRegistrar = view.findViewById(R.id.tvRegistrar01);

        btnLogin.setOnClickListener(v -> {

            String correo = email.getText().toString();
            String contra = password.getText().toString();

            mAuth.signInWithEmailAndPassword(correo, contra)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            NavController navController = Navigation.findNavController(v);
                            navController.navigate(R.id.navigation_inicio);

                        } else {
                            Toast.makeText(getContext(),
                                    "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

        });

        btnRegistrar.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.navigation_registrar);

        });

        ImageButton btnGoogle = view.findViewById(R.id.ivGooglePlus);

        btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });


        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (Exception e) {
                Toast.makeText(getContext(), "Error Google", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {

                        Toast.makeText(getContext(), "Login con Google OK", Toast.LENGTH_SHORT).show();

                        NavController navController = Navigation.findNavController(requireView());
                        navController.navigate(R.id.navigation_inicio);

                    } else {
                        Toast.makeText(getContext(),
                                "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }


}