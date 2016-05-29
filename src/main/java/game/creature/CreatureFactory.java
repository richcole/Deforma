package game.creature;

import game.events.Clock;
import game.events.EventBus;
import game.math.Matrix;
import game.math.Vector;
import game.view.View;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreatureFactory {
	
	private static Logger log = LoggerFactory.getLogger(CreatureFactory.class);

	private View view;
	private EventBus eventBus;
	private Clock clock;

	private Function<String, CreatureModel> creatureModelFactory;

	public CreatureFactory(Clock clock, View view, EventBus eventBus, Function<String, CreatureModel> creatureModelFactory) {
		this.view = view;
		this.eventBus = eventBus;
		this.clock = clock;
		this.creatureModelFactory = creatureModelFactory; 
	}

	public Creature createCreature(String modelName, String animName, Vector pos, double theta) {
		CreatureModel creatureModel = creatureModelFactory.apply(modelName);
		Creature creature = new Creature(clock, eventBus, creatureModel, pos, theta, animName);
		view.add(creature);
		return creature;
	}
}
