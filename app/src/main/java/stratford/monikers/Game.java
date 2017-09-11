package stratford.monikers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 20/05/2017.
 */

public class Game {
    public List<Integer> cardsInPlay;
    public List<Player> players;

    public Game(int[] cards){
        cardsInPlay = new ArrayList<>();
        players = new ArrayList<>();

//        players.add(player);
        for (int i=0; i<cards.length; i++){
            cardsInPlay.add(cards[i]);
        }
    }
}
