package stratford.monikers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class newGame extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("game");

    TextView codeView;
    String key;

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
        key = myRef.push().getKey();
        codeView.setText(key);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onClickCreateRed(View view){
        EditText editText = (EditText) findViewById(R.id.usernameInput);
        String username = editText.getText().toString();

        //Player player = new Player(username, "red");
        int[] deck = generateDeck();
        Game game = new Game(deck);

        myRef.child(key).setValue(game);

        Intent intent = new Intent(this, gameLobby.class);
        intent.putExtra("key",key);
        intent.putExtra("isCreator",true);
        intent.putExtra("name",username);
        intent.putExtra("team","red");
        startActivity(intent);
    }

    public void onClickCreateBlue(View view){
        EditText editText = (EditText) findViewById(R.id.usernameInput);
        String username = editText.getText().toString();

        //Player player = new Player(username, "blue");
        int[] deck = generateDeck();
        Game game = new Game(deck);

        myRef.child(key).setValue(game);

        Intent intent = new Intent(this, gameLobby.class);
        intent.putExtra("key",key);
        intent.putExtra("isCreator",true);
        intent.putExtra("name",username);
        intent.putExtra("team","blue");
        startActivity(intent);
    }

    public static int[] generateDeck(){
        int size = 485;
        int[] deck = new int[size];
        for (int i=0; i<size; i++){
            deck[i]=i;
        }
        for (int i=0; i<size; i++){
            int temp = deck[i];
            int rand = (int)Math.floor(Math.random()*size);
            deck[i] = deck[rand];
            deck[rand] = temp;
        }
        return deck;
    }
}
