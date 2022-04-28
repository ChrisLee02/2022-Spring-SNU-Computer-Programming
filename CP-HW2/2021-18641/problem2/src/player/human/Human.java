package relay.player.human;

import relay.player.Player;
import relay.map.Map;

public abstract class Human extends Player{

    Human(double position, double velocity, Map map) {
        super(position, velocity, map,"Human");
    }

}
