package stratford.monikers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import stratford.monikers.R;

public class ChooseCardsAcitivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference gameRef;
    DatabaseReference playerRef;
    DatabaseReference cardRef;
    String passedKey;
    String myUsername;
    int[] cards;
    int[] myCards;
    String[] cardDetails;
    ArrayList chosenCards;
    int numberOfCardsChosen;

    int cardCurrentlyViewing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cards);

        Intent intent = getIntent();
        passedKey = intent.getStringExtra("key");
        myUsername = intent.getStringExtra("username");
        gameRef = database.getReference("game").child(passedKey);
        playerRef = gameRef.child("players");
        cardRef = gameRef.child("cardsInPlay");
        cards = new int[476];
        myCards = new int[8];
        cardDetails = new String[4];
        cardCurrentlyViewing = 0;
        chosenCards = new ArrayList();
        numberOfCardsChosen = 0;

        cardRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int j = 0;
                for(DataSnapshot card : dataSnapshot.getChildren()){
                    cards[j] = Integer.parseInt(card.getValue()+"");
                    j++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Read from the database
        playerRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot playerSnap : dataSnapshot.getChildren()) {
                    if(playerSnap.getKey().equals(myUsername)){
//                        for(int j=0;j<8;j++) {
//                            myCards[j] = cards[i * 8 + j];
//                        }
                        System.arraycopy(cards,i*8,myCards,0,8);
                        setCardDetails();
//                        Toast.makeText(ChooseCardsAcitivity.this, "Card number " + myCards[i], Toast.LENGTH_SHORT).show();
                        break;
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void onClickNext(View view){
        cardCurrentlyViewing = (cardCurrentlyViewing+1)%8;
        Button button = (Button) findViewById(R.id.selectButton);
        if(chosenCards.contains(myCards[cardCurrentlyViewing])){
            button.setText("Deselect");
        }else{
            button.setText("Select");
        }
        setCardDetails();
    }

    public void onClickPrevious(View view){
        cardCurrentlyViewing--;
        if(cardCurrentlyViewing<0){
            cardCurrentlyViewing=7;
        }
        Button button = (Button) findViewById(R.id.selectButton);
        if(chosenCards.contains(myCards[cardCurrentlyViewing])){
            button.setText("Deselect");
        }else{
            button.setText("Select");
        }
        setCardDetails();
    }

    public void onClickSelect(View view){
        Button button = (Button) findViewById(R.id.selectButton);
        if(button.getText().equals("Select")){
            if(chosenCards.size()==5){
                Toast.makeText(ChooseCardsAcitivity.this, "5 cards already selected", Toast.LENGTH_SHORT).show();
            }else{
                button.setText("Deselect");
                chosenCards.add(myCards[cardCurrentlyViewing]);
                numberOfCardsChosen++;
            }
        }
        else{
            button.setText("Select");
            chosenCards.remove(chosenCards.indexOf(myCards[cardCurrentlyViewing]));
            numberOfCardsChosen--;
        }

        setCardDetails();
    }

    public void setCardDetails(){
        TextView numberChosen = (TextView) findViewById(R.id.numberChosen);
        numberChosen.setText(numberOfCardsChosen + " out of 5 chosen");

        TextView name = (TextView) findViewById(R.id.cardName);
        TextView description = (TextView) findViewById(R.id.cardDescription);
        TextView category = (TextView) findViewById(R.id.cardCategory);
        TextView points = (TextView) findViewById(R.id.cardPoints);

        int cardId = ChooseCardsAcitivity.this.getResources().getIdentifier("card" + myCards[cardCurrentlyViewing],"array",ChooseCardsAcitivity.this.getPackageName());
        cardDetails = getResources().getStringArray(cardId);
        name.setText(cardDetails[0]);
        description.setText(cardDetails[1]);
        category.setText(cardDetails[2]);
        points.setText(cardDetails[3]);
    }
}
