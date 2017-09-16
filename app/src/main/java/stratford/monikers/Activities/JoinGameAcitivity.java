package stratford.monikers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import stratford.monikers.R;

public class JoinGameAcitivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("game");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onClickJoinRed(View view){
        EditText editText = (EditText) findViewById(R.id.gameCode);
        String code = editText.getText().toString().toUpperCase();

        editText = (EditText) findViewById(R.id.usernameInput);
        String username = editText.getText().toString();

        Intent intent = new Intent(this, GameLobbyActivity.class);
        intent.putExtra("key",code);
        intent.putExtra("isCreator",false);
        intent.putExtra("name",username);
        intent.putExtra("team","red");
        startActivity(intent);
    }

    public void onClickJoinBlue(View view){
        EditText editText = (EditText) findViewById(R.id.gameCode);
        String code = editText.getText().toString().toUpperCase();

        editText = (EditText) findViewById(R.id.usernameInput);
        String username = editText.getText().toString();

        Intent intent = new Intent(this, GameLobbyActivity.class);
        intent.putExtra("key",code);
        intent.putExtra("isCreator",false);
        intent.putExtra("name",username);
        intent.putExtra("team","blue");
        startActivity(intent);
    }
}
