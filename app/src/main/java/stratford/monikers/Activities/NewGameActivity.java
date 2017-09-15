package stratford.monikers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import stratford.monikers.Game;
import stratford.monikers.R;


public class NewGameActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("game");

    TextView codeView;
    String key;

    public static int[] generateDeck() {
        int size = 485;
        int[] deck = new int[size];
        for (int i = 0; i < size; i++) {
            deck[i] = i;
        }
        for (int i = 0; i < size; i++) {
            int temp = deck[i];
            int rand = (int) Math.floor(Math.random() * size);
            deck[i] = deck[rand];
            deck[rand] = temp;
        }
        return deck;
    }

    public static String newKey() {
        String key = "";
        for (int i = 0; i < 4; i++) {
            int rand = (int) Math.floor(Math.random() * 26);
            char letter = (char) ('A' + rand);
            key += letter;
        }
        return key;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        Button red = (Button)findViewById(R.id.joinRed);
        Button blue = (Button)findViewById(R.id.joinBlue);
        Log.i("tag","h: "+red.getHeight());
        Log.i("tag","w: "+red.getWidth());
        red.setHeight(red.getWidth());
        blue.setHeight(blue.getWidth());
        codeView = (TextView) findViewById(R.id.generatedCodeTextView);
        key = newKey();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                while(dataSnapshot.child(key).exists()){
                    key = newKey();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        codeView.setText(key);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onClickCreateRed(View view){
        EditText editText = (EditText) findViewById(R.id.usernameInput);
        String username = editText.getText().toString();

        int[] deck = generateDeck();
        Game game = new Game(deck);

        myRef.child(key).setValue(game);

        Intent intent = new Intent(this, GameLobbyActivity.class);
        intent.putExtra("key",key);
        intent.putExtra("isCreator",true);
        intent.putExtra("name",username);
        intent.putExtra("team","red");
        startActivity(intent);
    }

    public void onClickCreateBlue(View view){
        EditText editText = (EditText) findViewById(R.id.usernameInput);
        String username = editText.getText().toString();

        int[] deck = generateDeck();
        Game game = new Game(deck);

        myRef.child(key).setValue(game);

        Intent intent = new Intent(this, GameLobbyActivity.class);
        intent.putExtra("key",key);
        intent.putExtra("isCreator",true);
        intent.putExtra("name",username);
        intent.putExtra("team","blue");
        startActivity(intent);
    }
}
