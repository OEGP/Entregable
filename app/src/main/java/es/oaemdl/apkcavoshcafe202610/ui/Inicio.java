package es.oaemdl.apkcavoshcafe202610.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.oaemdl.apkcavoshcafe202610.R;
import es.oaemdl.apkcavoshcafe202610.adapter.ProductAdapter;
import es.oaemdl.apkcavoshcafe202610.databinding.FragmentInicioBinding;
import es.oaemdl.apkcavoshcafe202610.model.Producto;

public class Inicio extends Fragment {
    FragmentInicioBinding binding;
    View view;
    Context context;
    NavController navController;

    RecyclerView rvNuevos;
    List<Producto> lista;
    ProductAdapter adapter;
    DatabaseReference ref;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_inicio, container, false);

        rvNuevos = view.findViewById(R.id.rvNuevos);


        rvNuevos.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        lista = new ArrayList<>();
        adapter = new ProductAdapter(lista);
        rvNuevos.setAdapter(adapter);

        ref = FirebaseDatabase.getInstance().getReference("productos");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Producto p = data.getValue(Producto.class);
                    if (p != null) lista.add(p);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error BD", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        navController = Navigation.findNavController( view );

    }

}