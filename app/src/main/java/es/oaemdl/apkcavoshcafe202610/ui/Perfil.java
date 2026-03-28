package es.oaemdl.apkcavoshcafe202610.ui;

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

import com.google.android.gms.auth.api.signin.*;
import com.google.firebase.auth.*;

import es.oaemdl.apkcavoshcafe202610.R;
import es.oaemdl.apkcavoshcafe202610.databinding.FragmentPerfilBinding;

public class Perfil extends Fragment {

    FragmentPerfilBinding binding;
    NavController navController;

    GoogleSignInClient googleSignInClient;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPerfilBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            binding.txtNombre.setText(user.getDisplayName());
            binding.txtCorreo.setText(user.getEmail());
        }

        binding.btnCambiarContra.setOnClickListener(v -> {

            String nuevaPass = binding.NuevaContra.getEditText().getText().toString();

            if (nuevaPass.length() < 6) {
                Toast.makeText(getContext(), "Mínimo 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            if (user != null) {
                user.updatePassword(nuevaPass)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Debes volver a iniciar sesión", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        binding.btnCerrarSesion.setOnClickListener(v -> {
            mAuth.signOut();
            googleSignInClient.signOut().addOnCompleteListener(task -> {

                navController.popBackStack(); // limpia historial
                navController.navigate(R.id.navigation_login);

            });
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }
}
