package stratford.monikers.Activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import stratford.monikers.R;

public class GameActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference gameRef;
    DatabaseReference playerRef;
    DatabaseReference cardRef;
    String passedKey;
    String myUsername;
    ArrayList<Integer> chosenCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        passedKey = intent.getStringExtra("key");
        myUsername = intent.getStringExtra("username");
        chosenCards = intent.getIntegerArrayListExtra("chosenCards");
        gameRef = database.getReference("game").child(passedKey);
        playerRef = gameRef.child("players");
        cardRef = gameRef.child("cardsInPlay");
        for(int i = 0; i<chosenCards.size();i++) {
            playerRef.child(myUsername).child("chosenCards").child(i+"").setValue(chosenCards.get(i));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Read from the database
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean allReady = true;
                for (DataSnapshot playerSnap : dataSnapshot.getChildren()) {
                    if(!playerSnap.child("chosenCards").exists()){
                        allReady = false;
                        break;
                    }
                }
                if(allReady){
                    Toast.makeText(GameActivity.this, "All Players ready!", Toast.LENGTH_SHORT).show();
                    playerRef.removeEventListener(this);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
