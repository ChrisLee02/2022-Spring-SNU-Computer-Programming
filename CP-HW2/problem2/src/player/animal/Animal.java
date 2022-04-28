package relay.player.animal;

import relay.player.Player;
import relay.map.Map;
import relay.simulator.Message;

public abstract class Animal extends Player {

    Animal(double position, double velocity, Map map) {
        super(position, velocity,  map, "Animal");
    }

	@Override
    public void hear(Message message) {
        //Animals do nothing when they hear the announcement
	}

}
