package game.nwn.readers;

import java.io.File;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class ErfReaderList {

	private static Logger log = LoggerFactory.getLogger(ErfReaderList.class);

	List<ErfReader> erfReaderList = Lists.newArrayList();
	
	public void add(File erfFile) {
		log.info("Loading " + erfFile);
		erfReaderList.add(new ErfReader(new BinaryFileReader(erfFile)));
	}
	
	public byte[] getResource(String key, ResourceType resType) {
		for(ErfReader reader: erfReaderList) {
			byte[] res = reader.getResource(key, resType);
			if ( res != null ) {
				return res;
			}
		}
		return null;
	}

	public void addAll(File root) {
		Stack<File> stack = new Stack<File>();
		stack.push(root);
		while(! stack.empty()) {
			root = stack.pop();
			if ( root.exists() ) {
				for(File file: root.listFiles()) {
					if ( file.isDirectory() ) {
						stack.push(file);
					}
					else if ( file.getName().endsWith(".erf") ) {
						add(file);
					}
				}
			}
		}
	}
	
	public List<ErfReader> getReaders() {
		return erfReaderList;
	}
}
