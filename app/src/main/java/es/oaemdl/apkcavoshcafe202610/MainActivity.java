package es.oaemdl.apkcavoshcafe202610;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import es.oaemdl.apkcavoshcafe202610.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            int id = navDestination.getId();
            if ( id == R.id.navigation_splash || id == R.id.navigation_login ||
                 id == R.id.navigation_registrar )

                    //id == R.id.navigation_validar ||
                    //id == R.id.navigation_menuProducto || id == R.id.navigation_menuProductoPersonalizar )
                navView.setVisibility( View.INVISIBLE );
            else {

                navView.setVisibility(View.VISIBLE);
            }
        });
    }
}

