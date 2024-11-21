package com.example.eventito;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Map<String, Object> user = new HashMap<>();
        user.put("nome_usuario", "teste lalala");
        user.put("email_usuario", "teste@exemplo.com");
        user.put("xp_usuario", 10010101);

        db.collection("Usuarios")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firebase", "Usuário adicionado com ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w("Firebase", "Erro ao adicionar usuário", e);
                });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}