package com.example.practicafirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.practicafirebase.models.Coche;
import com.example.practicafirebase.models.Persona;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ValueEventListener, ChildEventListener, View.OnClickListener {

    private CochesAdapter cochesAdapter;
    private List<Coche> listaCoches;
    private RecyclerView recyclerView;
    private Button btnAdd;
    private Button btnConsulta;
    private Button btnModificar;
    private EditText nombre;
    private EditText apellido;
    private EditText telefono;
    private EditText marca;
    private EditText modelo;
    private AutoCompleteTextView matricula;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference dbCoche = FirebaseDatabase.getInstance().getReference("coche");
        dbCoche.addValueEventListener(this);
        dbCoche.addChildEventListener(this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        matricula = findViewById(R.id.matricula);

        listaCoches = new ArrayList<>();
        System.out.printf("listaCoches: %s\n", listaCoches);
        cochesAdapter = new CochesAdapter(this, dbCoche, listaCoches);
        recyclerView = findViewById(R.id.recyclerView);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnConsulta = findViewById(R.id.btnConsulta);
        btnConsulta.setOnClickListener(this);

        btnModificar = findViewById(R.id.btnModificar);
        btnModificar.setOnClickListener(this);

        nombre = findViewById(R.id.nom);
        apellido = findViewById(R.id.lastNom);
        telefono = findViewById(R.id.telefono);
        marca = findViewById(R.id.marca);
        modelo = findViewById(R.id.modelo);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(cochesAdapter);
    }

    public void buscarNombre(String nombre) {
        DatabaseReference dbCoche = FirebaseDatabase.getInstance().getReference("coche");
        Query query = dbCoche.orderByChild("persona/nombre").startAt(nombre).endAt(nombre + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCoches.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Coche coche = snapshot.getValue(Coche.class);
                    System.out.println(coche);
                    listaCoches.add(coche);
                }
                cochesAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        DatabaseReference dbCoche = null;
        Coche coche;
        switch (v.getId()) {
            case R.id.btnAdd:
                dbCoche = FirebaseDatabase.getInstance().getReference("coche");
                Query query = dbCoche.orderByChild("matricula").equalTo(matricula.getText().toString());
                DatabaseReference finalDbCoche = dbCoche;

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Coche coche = new Coche(
                                    matricula.getText().toString(),
                                    marca.getText().toString(),
                                    modelo.getText().toString(),
                                    new Persona(
                                            nombre.getText().toString(),
                                            apellido.getText().toString(),
                                            telefono.getText().toString()
                                    )
                            );
                            finalDbCoche.child(matricula.getText().toString()).setValue(coche);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
                case R.id.btnConsulta:
                    System.out.println("CONSULTA");
                    buscarNombre(nombre.getText().toString());
                    break;
        }
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // Eliminem tot el contingut per no afegir cada cop que hi ha un canvi
        listaCoches.removeAll(listaCoches);
        System.out.println("FDASEDRASDASDASDAS");

        // Recorrem tots els elements del DataSnapshot i els mostrem
        for (DataSnapshot element : dataSnapshot.getChildren()) {
            System.out.println(element);
            String name = element.child("persona").child("nombre").getValue().toString();
            String surname = element.child("persona").child("apellido").getValue().toString();
            String telephone = element.child("persona").child("telefono").getValue().toString();


            Persona person = new Persona(name, surname, telephone);
            Coche coche = new Coche(
                    element.child("matricula").getValue().toString(),
                    element.child("marca").getValue().toString(),
                    element.child("modelo").getValue().toString(),
                    person
            );
            System.out.printf("coche: %s\n", coche);
            listaCoches.add(coche);
        }
        // Per si hi ha canvis, que es refresqui l'adaptador
        cochesAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(listaCoches.size() - 1);
    }
    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}