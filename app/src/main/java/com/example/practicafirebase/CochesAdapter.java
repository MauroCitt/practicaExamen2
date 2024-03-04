package com.example.practicafirebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicafirebase.models.Persona;
import com.google.firebase.database.DatabaseReference;
import com.example.practicafirebase.models.Coche;

import java.util.List;

public class CochesAdapter extends RecyclerView.Adapter<CochesAdapter.ViewHolder>{

    private View mView;
    public List<Coche> listaCoche;
    public Persona persona;
    private Context context;
    private DatabaseReference databaseReference;

    public CochesAdapter(Context context, DatabaseReference databaseReference, List<Coche> listaCoche) {
        this.context = context;
        this.databaseReference = databaseReference;
        this.listaCoche = listaCoche;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView matricula;

        @SuppressLint("WrongViewCast")
        public ViewHolder(View itemView) {
            super(itemView);
            matricula = itemView.findViewById(R.id.matricula);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mostraPopupMenu(v, position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Coche coche = listaCoche.get(position);
        String matricula = coche.getMatricula() != null ? coche.getMatricula() : "";
        System.out.println("matri: " + matricula);
        String nombre = coche.getPersona() != null && coche.getPersona().getNombre() != null ? coche.getPersona().getNombre() : "";
        System.out.println("nombre: " + nombre);
        holder.matricula.setText(matricula);
    }

    @Override
    public int getItemCount() {
        return listaCoche != null ? listaCoche.size() : 0;
    }

    private void mostraPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(this.context, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new Menu(position));
        popupMenu.show();
    }

    public class Menu implements PopupMenu.OnMenuItemClickListener {
        Integer pos;

        public Menu(int pos) {
            this.pos = pos;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Coche coche;
            switch (item.getItemId()) {
                case R.id.borrar:
                    databaseReference.child(listaCoche.get(pos).getMatricula()).removeValue();
                default:
            }
            return false;
        }
    }
}
