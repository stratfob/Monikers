package stratford.monikers.Activities;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_players);

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
                    TextView text = (TextView) findViewById(R.id.readyStatus);
                    text.setText(R.string.allPlayersReady);
                    Button button = (Button) findViewById(R.id.startButton);
                    button.setVisibility(View.VISIBLE);
                    playerRef.removeEventListener(this);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void onClickStart(View view){
        if(getIntent().getBooleanExtra("isCreator",false)) {
            Toast.makeText(this, "Heyo", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Only the game creator can start the game", Toast.LENGTH_SHORT).show();
        }
    }
}
