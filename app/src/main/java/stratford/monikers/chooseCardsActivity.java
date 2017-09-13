package stratford.monikers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chooseCardsActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference gameRef;
    String passedKey;
    String myUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cards);

        Intent intent = getIntent();
        passedKey = intent.getStringExtra("key");
        myUsername = intent.getStringExtra("username");
        gameRef = database.getReference("game").child(passedKey);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Read from the database
        gameRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot playersSnapshot = dataSnapshot.child("players");
                int i = 0;
                for (DataSnapshot playerSnap : playersSnapshot.getChildren()) {
                    if(playerSnap.getKey().equals(myUsername)){
                        Toast.makeText(chooseCardsActivity.this, "You are player "+i, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
