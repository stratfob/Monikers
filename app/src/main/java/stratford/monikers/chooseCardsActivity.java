package stratford.monikers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    DatabaseReference playerRef;
    DatabaseReference cardRef;
    String passedKey;
    String myUsername;
    int[] cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cards);

        Intent intent = getIntent();
        passedKey = intent.getStringExtra("key");
        myUsername = intent.getStringExtra("username");
        gameRef = database.getReference("game").child(passedKey);
        playerRef = gameRef.child("players");
        cardRef = gameRef.child("cardsInPlay");
        cards = new int[485];

        cardRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int j = 0;
                for(DataSnapshot card : dataSnapshot.getChildren()){
                    cards[j] = Integer.parseInt(card.getValue()+"");
                    j++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Read from the database
        playerRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                TextView text = (TextView) findViewById(R.id.textView);
                text.setText("");
                String dealtCards = "";
                for (DataSnapshot playerSnap : dataSnapshot.getChildren()) {
                    if(playerSnap.getKey().equals(myUsername)){
                        for(int j=0;j<8;j++){
                            dealtCards+=cards[i*8 + j] + " ";
                        }
                        text.setText(dealtCards);
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
