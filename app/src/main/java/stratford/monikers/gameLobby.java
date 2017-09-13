package stratford.monikers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class gameLobby extends AppCompatActivity {

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
                    Toast.makeText(gameLobby.this, "Game not found", Toast.LENGTH_SHORT).show();
                    thisGameRef.removeValue();
                    finish();
                }
                else if(redTeam.size()>4 && blueTeam.size()>4){
                    Toast.makeText(gameLobby.this, "Game is full", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(dataSnapshot.child("stage").getValue().equals("choosing")){
                    Intent intent = new Intent(gameLobby.this,chooseCardsActivity.class);
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
                    TextView slot9 = (TextView) findViewById(R.id.slot9);
                    slot9.setText("");
                    TextView slot8 = (TextView) findViewById(R.id.slot8);
                    slot8.setText("");
                    TextView slot7 = (TextView) findViewById(R.id.slot7);
                    slot7.setText("");
                    TextView slot6 = (TextView) findViewById(R.id.slot6);
                    slot6.setText("");
                    TextView slot5 = (TextView) findViewById(R.id.slot5);
                    slot5.setText("");
                    TextView slot4 = (TextView) findViewById(R.id.slot4);
                    slot4.setText("");
                    TextView slot3 = (TextView) findViewById(R.id.slot3);
                    slot3.setText("");
                    TextView slot2 = (TextView) findViewById(R.id.slot2);
                    slot2.setText("");
                    TextView slot1 = (TextView) findViewById(R.id.slot1);
                    slot1.setText("");
                    TextView slot0 = (TextView) findViewById(R.id.slot0);
                    slot0.setText("");

                    switch (redTeam.size()) {
                        case 5:
                            slot8.setText(redTeam.get(4));
                        case 4:
                            slot6.setText(redTeam.get(3));
                        case 3:
                            slot4.setText(redTeam.get(2));
                        case 2:
                            slot2.setText(redTeam.get(1));
                        case 1:
                            slot0.setText(redTeam.get(0));
                            break;
                        default:
                    }
                    switch (blueTeam.size()) {
                        case 5:
                            slot9.setText(blueTeam.get(4));
                        case 4:
                            slot7.setText(blueTeam.get(3));
                        case 3:
                            slot5.setText(blueTeam.get(2));
                        case 2:
                            slot3.setText(blueTeam.get(1));
                        case 1:
                            slot1.setText(blueTeam.get(0));
                            break;
                        default:
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
        if(myTeam.equals("blue")&&redTeam.size()<5) {
            playerRef.child(myUsername).child("team").setValue("red");
            myTeam="red";
        }
        else if(blueTeam.size()<5){
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
