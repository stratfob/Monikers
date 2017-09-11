package stratford.monikers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickNew(View view) {
        Intent intent = new Intent(this, newGame.class);
        startActivity(intent);
    }
    public void onClickJoin(View view) {
        Intent intent = new Intent(this, joinGame.class);
        startActivity(intent);
    }
    public void onClickRules(View view) {
        Intent intent = new Intent(this, rules.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
