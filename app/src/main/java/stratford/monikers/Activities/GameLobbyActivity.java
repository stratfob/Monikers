package stratford.monikers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stratford.monikers.R;

public class GameLobbyActivity extends AppCompatActivity {

    private final int MAX_TEAM_SIZE = 5;
    private final List<Integer> RED_TEAM_IDS =
            Arrays.asList(R.id.red0, R.id.red1, R.id.red2, R.id.red3, R.id.red4);
    private final List<Integer> BLUE_TEAM_IDS =
            Arrays.asList(R.id.blue0, R.id.blue1, R.id.blue2, R.id.blue3, R.id.blue4);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference gameRef = database.getReference("game");
    DatabaseReference playerRef;
    DatabaseReference thisGameRef;
    String passedKey;
    String myUsername;
    String myTeam;
    ArrayList<String> redTeam;
    ArrayList<String> blueTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        Intent intent = getIntent();
        passedKey = intent.getStringExtra("key");

        thisGameRef = gameRef.child(passedKey);

        myUsername = intent.getStringExtra("name");
        myTeam = intent.getStringExtra("team");
        playerRef = thisGameRef.child("players");

        if(getIntent().getBooleanExtra("isCreator",false)){
            thisGameRef.child("stage").setValue("lobby");
        }

        playerRef.child(myUsername).child("team").setValue(getIntent().getStringExtra("team"));

        redTeam = new ArrayList<>();
        blueTeam = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Read from the database
        thisGameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("stage").getValue()==null && !getIntent().getBooleanExtra("isCreator",false)){
                    Toast.makeText(GameLobbyActivity.this, "Game not found", Toast.LENGTH_SHORT).show();
                    thisGameRef.removeValue();
                    finish();
                }
                else if(redTeam.size()>4 && blueTeam.size()>4){
                    Toast.makeText(GameLobbyActivity.this, "Game is full", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(dataSnapshot.child("stage").getValue().equals("choosing")){
                    Intent intent = new Intent(GameLobbyActivity.this, ChooseCardsAcitivity.class);
                    intent.putExtra("key",passedKey);
                    intent.putExtra("username",myUsername);
                    startActivity(intent);
                }
                else {
                    redTeam = new ArrayList<>();
                    blueTeam = new ArrayList<>();
                    DataSnapshot playersSnapshot = dataSnapshot.child("players");
                    for (DataSnapshot playerSnap : playersSnapshot.getChildren()) {
                        if (playerSnap.child("team").getValue().equals("red")) {
                            redTeam.add(playerSnap.getKey());
                        } else {
                            blueTeam.add(playerSnap.getKey());
                        }
                    }
                    for (int index = 0; index < MAX_TEAM_SIZE; index++) {
                        TextView textView = (TextView) findViewById(RED_TEAM_IDS.get(index));
                        if (index >= redTeam.size()) {
                            textView.setText("");
                        } else {
                            textView.setText(redTeam.get(index));
                        }
                    }
                    for (int index = 0; index < MAX_TEAM_SIZE; index++) {
                        TextView textView = (TextView) findViewById(BLUE_TEAM_IDS.get(index));
                        if (index >= blueTeam.size()) {
                            textView.setText("");
                        } else {
                            textView.setText(blueTeam.get(index));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    @Override
    protected void onStop() {
        //TODO
        super.onStop();
//        if(getIntent().getBooleanExtra("isCreator",false)){
//            gameRef.child(passedKey).removeValue();
//        }
    }

    public void onClickChangeTeams(View view){
        if (myTeam.equals("blue") && redTeam.size() < MAX_TEAM_SIZE) {
            playerRef.child(myUsername).child("team").setValue("red");
            myTeam="red";
        } else if (blueTeam.size() < MAX_TEAM_SIZE) {
            playerRef.child(myUsername).child("team").setValue("blue");
            myTeam="blue";
        }
    }

    public void onClickStart(View view){
        //if it is the creator of the game (false is the default)
        if (getIntent().getBooleanExtra("isCreator",false)){
            gameRef.child(passedKey).child("stage").setValue("choosing");
        }
        else{
            Toast.makeText(this, "Only the game creator can start the game.", Toast.LENGTH_SHORT).show();
        }
    }
}
