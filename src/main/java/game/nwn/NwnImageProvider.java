package game.nwn;

import java.util.function.Function;

import game.image.Image;
import game.nwn.readers.BinaryFileReader;
import game.nwn.readers.KeyReader;
import game.nwn.readers.Resource;
import game.nwn.readers.ResourceType;
import game.nwn.readers.TgaLoader;

public class NwnImageProvider implements Function<String, Image> {
	
	private KeyReader reader;

	public NwnImageProvider(KeyReader reader) {
		this.reader = reader;
	}

	@Override
	public Image apply(String name) {
		Resource res = reader.getResource(name, ResourceType.TGA);
		BinaryFileReader inp = res.getReader().getInp();
		inp.seek(res.getOffset());
		try {
			return new TgaLoader().readImage(inp);
		}
		catch(Exception e) {
			throw new RuntimeException("Unable to load resource " + name, e);
		}
	}

}
