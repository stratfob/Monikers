package stratford.monikers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import stratford.monikers.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickNew(View view) {
        Intent intent = new Intent(this, NewGameActivity.class);
        startActivity(intent);
    }
    public void onClickJoin(View view) {
        Intent intent = new Intent(this, JoinGameActivity.class);
        startActivity(intent);
    }
    public void onClickRules(View view) {
        Intent intent = new Intent(this, RulesActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
