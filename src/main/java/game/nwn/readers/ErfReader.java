package game.nwn.readers;

import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ErfReader {
	
	private static Logger log = LoggerFactory.getLogger(ErfReader.class);

	public static class ErfHeader {
		public long offset;
		
		public long fileType;
		public long version;
		public long languageCount;
		public long localizedStringSize;
		public long entryCount;
		public long offsetToLocalizedString;
		public long offsetToKeyList;
		public long offsetToResourceList;
		public long buildYear;
		public long buildDay;
		public long descriptionStringRef;
		public byte[] reserved;
	}
	
	public static class ErfKeyEntry {
		public String name;
		public long   resId;
		public int    resType;
		public int    reserved;
	}
	
	public static class ErfKeyList {
		public Multimap<String, ErfKeyEntry> entries = HashMultimap.create();

		public void put(ErfKeyEntry entry) {
			entries.put(entry.name, entry);
		}
	}
	
	public static class ErfResourceEntry {
		public long   offset;
		public long   size;
	}

	private BinaryFileReader inp;
	private ErfHeader hdr;
	private ErfKeyList keyList;
	
	public ErfReader(BinaryFileReader inp) {
		this.inp = inp;
		this.hdr = new ErfHeader();
		this.keyList = new ErfKeyList();
		readErfHeader(hdr, inp);
		readKeyList(hdr, inp, keyList);
	}
	
	void readErfHeader(ErfHeader hdr, BinaryFileReader inp) {
		hdr.offset = inp.pos();

		hdr.fileType = inp.readWord();
		hdr.version = inp.readWord();
		hdr.languageCount = inp.readWord();
		hdr.localizedStringSize = inp.readWord();
		hdr.entryCount = inp.readWord();
		hdr.offsetToLocalizedString = inp.readWord();

		hdr.offsetToKeyList = inp.readWord();
		hdr.offsetToResourceList = inp.readWord();
		hdr.buildYear = inp.readWord();
		hdr.buildDay = inp.readWord();
		hdr.descriptionStringRef = inp.readWord();
		
		hdr.reserved = inp.readBytes(116);
	}
	
	void readKeyList(ErfHeader hdr, BinaryFileReader inp, ErfKeyList keyList) {
		inp.seek(hdr.offset + hdr.offsetToKeyList);
		for(int i=0;i<hdr.entryCount;++i) {
			keyList.put(readErfKeyEntry(inp));
		}
	}

	private ErfKeyEntry readErfKeyEntry(BinaryFileReader inp) {
		ErfKeyEntry entry = new ErfKeyEntry();
		entry.name = inp.readNullString(16);
		entry.resId = inp.readWord();
		entry.resType = inp.readShort();
		entry.reserved = inp.readShort();
		return entry;
	}
	
	private byte[] getErfResource(ErfHeader hdr, BinaryFileReader inp, ErfKeyEntry entry) {
		inp.seek(hdr.offset + hdr.offsetToResourceList + 8 * entry.resId);
		long offset = inp.readWord();
		long length = inp.readWord();
		inp.seek(hdr.offset + offset);
		return inp.readBytes((int)length);
	}
	
	public byte[] getResource(String key, ResourceType resType) {
		for(ErfKeyEntry keyEntry: keyList.entries.get(key)) {
			log.info("Considering " + keyEntry.name + " " + keyEntry.resType + " against " + resType.id);
			if ( keyEntry.resType == resType.id) {
				return getErfResource(hdr, inp, keyEntry);
			}
		}
		return null;
	}

	public Set<String> getKeys() {
		return keyList.entries.keySet();
	}

	public Collection<ErfKeyEntry> getEntryList(String key) {
		return keyList.entries.get(key);
	}
}
