package stratford.monikers.Activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import stratford.monikers.R;

public class WaitForPlayersActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference gameRef;
    DatabaseReference playerRef;
    DatabaseReference cardRef;
    String passedKey;
    String myUsername;
    ArrayList<Integer> chosenCards;
    ArrayList<Integer> newDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_players);

        Intent intent = getIntent();
        passedKey = intent.getStringExtra("key");
        myUsername = intent.getStringExtra("username");
        chosenCards = intent.getIntegerArrayListExtra("chosenCards");
        newDeck = new ArrayList<>();

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
                    if(!playerSnap.child("chosenCards").exists() ||
                            playerSnap.child("chosenCards").getChildrenCount()<5){
                        allReady = false;
                        break;
                    }
                }
                if(allReady){
                    for(DataSnapshot playerSnap:dataSnapshot.getChildren()){
                        for(DataSnapshot cardSnap:playerSnap.child("chosenCards").getChildren()){
                            newDeck.add(Integer.parseInt(cardSnap.getValue() + ""));
                        }
                    }
                    TextView text = (TextView) findViewById(R.id.readyStatus);
                    text.setText(R.string.allPlayersReady);
                    playerRef.removeEventListener(this);

                    newDeck = shuffle(newDeck);
                    gameRef.child("cardsInPlay").removeValue();
                    gameRef.child("cardsInPlay").setValue(newDeck);
                    Intent intent = new Intent(WaitForPlayersActivity.this, GameActivity.class);
                    intent.putExtra("username", myUsername);
                    intent.putExtra("key",passedKey);
                    intent.putExtra("isCreator", getIntent().getBooleanExtra("isCreator",false));
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public ArrayList<Integer> shuffle (ArrayList<Integer> deck){
        for (int i = 0; i < deck.size(); i++) {
            int temp = deck.get(i);
            int rand = (int) Math.floor(Math.random() * deck.size());
            deck.set(i, deck.get(rand));
            deck.set(rand, temp);
        }
        return deck;
    }
}
