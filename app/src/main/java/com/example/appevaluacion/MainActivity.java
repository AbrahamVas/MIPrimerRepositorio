package com.example.appevaluacion;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText txtRut, txtNombre, txtDireccion, txtEncargado;
    private ListView lista;
    private Spinner spEnfermedad;
    private FirebaseFirestore db;

    String[] tiposdeenfermedades = {"Fractura", "Accidente", "Resfriado comun", "Cita"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        txtDireccion = findViewById(R.id.txtDireccion);
        txtNombre = findViewById(R.id.txtNombre);
        txtEncargado = findViewById(R.id.txtEncargado);
        spEnfermedad = findViewById(R.id.spEnfermedad);
        lista = findViewById(R.id.lista);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposdeenfermedades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEnfermedad.setAdapter(adapter);
    }

    public void CargarLista() {
        CargarListaFirestore();
    }

    public void CargarListaFirestore() {
        db.collection("enfermedades")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        ArrayList<String> listadeenfermedades = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String linea = "||" +
                                    document.getString("codigo") + "||" +
                                    document.getString("nombre") + "||" +
                                    document.getString("encargado") + "||" +
                                    document.getString("direccion");
                            listadeenfermedades.add(linea);
                        }
                        // Actualizar el ListView con los datos obtenidos
                        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listadeenfermedades);
                        lista.setAdapter(listAdapter);
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }
}
