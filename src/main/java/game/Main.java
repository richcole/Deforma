package game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
	
	final static Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		log.info("Starting");
		try {
			Context context = new Context();
			context.init();
			try {
				context.run();
			} catch (RuntimeException e) {
				log.error("Exception raised while running", e);
			} finally {
				context.dispose();
			}
		} catch (RuntimeException e) {
			log.error("Exception raised in main", e);
		}
		log.info("Finished");
	}

}
