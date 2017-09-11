package stratford.monikers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class joinGame extends AppCompatActivity {

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
        String code = editText.getText().toString();

        editText = (EditText) findViewById(R.id.usernameInput);
        String username = editText.getText().toString();

        Intent intent = new Intent(this, gameLobby.class);
        intent.putExtra("key",code);
        intent.putExtra("isCreator",false);
        intent.putExtra("name",username);
        intent.putExtra("team","red");
        startActivity(intent);
    }

    public void onClickJoinBlue(View view){
        EditText editText = (EditText) findViewById(R.id.gameCode);
        String code = editText.getText().toString();

        editText = (EditText) findViewById(R.id.usernameInput);
        String username = editText.getText().toString();

        Intent intent = new Intent(this, gameLobby.class);
        intent.putExtra("key",code);
        intent.putExtra("isCreator",false);
        intent.putExtra("name",username);
        intent.putExtra("team","blue");
        startActivity(intent);
    }
}
