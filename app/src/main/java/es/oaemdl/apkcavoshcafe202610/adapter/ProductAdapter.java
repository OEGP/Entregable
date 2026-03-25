package es.oaemdl.apkcavoshcafe202610.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.oaemdl.apkcavoshcafe202610.R;
import es.oaemdl.apkcavoshcafe202610.model.Producto;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    List<Producto> lista;

    public ProductAdapter(List<Producto> lista) {
        this.lista = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView nombre, precio;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgProducto);
            nombre = itemView.findViewById(R.id.tvNombre);
            precio = itemView.findViewById(R.id.tvPrecio);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Producto p = lista.get(position);

        holder.nombre.setText(p.nombre);
        holder.precio.setText("S/ " + p.precio);

        // 🔥 PICASSO
        Picasso.get().load(p.imagen).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
