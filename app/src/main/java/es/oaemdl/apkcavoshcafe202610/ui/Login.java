package es.oaemdl.apkcavoshcafe202610.ui;

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
import android.widget.Toast;

import com.google.firebase.auth.*;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import es.oaemdl.apkcavoshcafe202610.R;
import es.oaemdl.apkcavoshcafe202610.databinding.FragmentLoginBinding;

public class Login extends Fragment {

    FragmentLoginBinding binding;
    NavController navController;

    GoogleSignInClient googleSignInClient;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authListener;

    int RC_SIGN_IN = 100;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        mAuth = FirebaseAuth.getInstance();

        authListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                navController.popBackStack();
                navController.navigate(R.id.navigation_inicio);
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);


        binding.btnIniciar.setOnClickListener(v -> {

            String correo = binding.tilCorreo.getEditText().getText().toString();
            String contra = binding.tilPasswordd.getEditText().getText().toString();

            mAuth.signInWithEmailAndPassword(correo, contra)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Login correcto", Toast.LENGTH_SHORT).show();
                        }else {
                            if (isAdded()) {
                            Toast.makeText(requireContext(),
                                    "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                         }
                        }
                    });
        });

        binding.tvRegistrar01.setOnClickListener(v -> {
            navController.navigate(R.id.navigation_registrar);
        });

        binding.ivGooglePlus.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            mAuth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        if (isAdded()) {
                            Toast.makeText(requireContext(), "Login con Google OK", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (isAdded()) {
                            Toast.makeText(requireContext(),
                                    "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
