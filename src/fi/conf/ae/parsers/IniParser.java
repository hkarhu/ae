/* [LGPL] Copyright 2010, 2011 Gima, Irah

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package fi.conf.ae.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fi.conf.ae.routines.S;

/**
 * Handler for configuration data in the following format:
 * 
 * <pre>
 * entry0 = somevalue
 * 
 * [Section1]
 * entry1 = value1
 * entry2 = value2
 * entry2 = value3
 * 
 * [Section2]
 * entry1 = value with spaces and "="-characters.
 * </pre>
 * 
 * Default entry (section name "") will contain "entry0". "entry2" under "Section1" will contain "value3".
 * 
 * @author Gima
*/
public class IniParser {

	private LinkedHashMap<String, LinkedHashMap<String, String>> data;
	private BufferedReader reader;

	public IniParser() {
		data = new LinkedHashMap<String, LinkedHashMap<String,String>>();
	}
	
	public IniParser(BufferedReader reader) throws IOException {
		this();
		loadConfig(reader);
	}
	
	public IniParser(Path file) throws IOException {
		this(new BufferedReader(
				new InputStreamReader(
						Files.newInputStream(file, StandardOpenOption.READ)
						)
				)
		);
	}
	
	public IniParser(String file) throws IOException {
		this(new BufferedReader(
				new InputStreamReader(
						Files.newInputStream(Paths.get(file), StandardOpenOption.READ)
						)
				)
		);
	}
	
	/**
	 * Load configuration from given stream
	 * @param reader - Parse config from this reader. Null if wishing to create empty {@link IniParser}
	 * @throws IOException - Errors during loading
	 */
	public void loadConfig(BufferedReader reader) throws IOException {
		this.reader = reader;
		if (reader != null) parse();
	}

	public void loadConfig(Path file) throws IOException {
		loadConfig(Files.newBufferedReader(file, Charset.defaultCharset()));
	}

	public void loadConfig(String file) throws IOException {
		
		Path configPath = Paths.get(file);
		
		if(Files.exists(configPath)){
			loadConfig(Files.newBufferedReader(configPath, Charset.defaultCharset()));
		} else {
			S.eprintf("Cannot parse file: Path does not exist, " + configPath.toString());
		}
		
	}

	/**
	 * Close the reader. Discards Exceptions silently.
	 */
	public void closeStreams() {
		try {
			reader.close();
		} catch (IOException e) {}
	}
	
	/**
	 * Parse configuration data from InputStream and store it to memory.
	 * 
	 * @throws IOException - Error during loading
	 */
	private void parse() throws IOException {
		
		String readLine;
		String entryName;
		String entryValue;
		String lastSectionName = "";

		Pattern sectionPattern = Pattern.compile( "^\\[([^\\]]*)\\]$" );
		Pattern sectionEntryPattern = Pattern.compile( "^([^=]*?) ?= ?(.*)$" );
//		Pattern commentPattern = Pattern.compile( "^([^#]*?) *#[^#]*$" );
		
		Matcher matcher;
		
		while (reader.ready()) {
			readLine = reader.readLine();
			//S.debug("Read: %s", readLine);

			int commentStartIdx = readLine.indexOf('#');
			if (commentStartIdx >= 0) {
				readLine = readLine.substring(0, commentStartIdx);
//				S.debug("Comment morph: %s", readLine);
			}
			
			if (readLine.length() == 0) continue;
			
			matcher = sectionPattern.matcher(readLine);
			if (matcher.matches()) { // section begins
//				S.debug("Section: %s", matcher.group(1));
				lastSectionName = matcher.group(1).toLowerCase();
				continue;
			}
			
			matcher = sectionEntryPattern.matcher(readLine);
			if (matcher.matches()) { // section entry begins
				entryName = matcher.group(1).toLowerCase();
				entryValue = matcher.group(2);
				//S.debug("Entry: '%s', Value: '%s'", entryName, entryValue);

				if (data.get(lastSectionName) == null ) {
					data.put(lastSectionName, new LinkedHashMap<String, String>());
				}
				
				data.get(lastSectionName).put(entryName, entryValue);
				continue;
			}

			//S.debug("Unknown line '%s'", readLine);
			
		} // while

	}
	

	/**
	 * Write config data to stream
	 * @throws IOException - Error during writing
	 */
	public void save(Writer writer) throws IOException {
		
		for (Entry<String, LinkedHashMap<String, String>> sectionKeyValue : data.entrySet()) {
			// write section header line
//			S.debug("Section: %s", sectionKeyValue.getKey());
			if (!sectionKeyValue.getKey().equals("")) {
				writer.write(String.format("[%s]%n", sectionKeyValue.getKey()));
			}

			for (Entry<String, String> entryKeyValue : sectionKeyValue.getValue().entrySet()) {
				// write section entry
//				S.debug("Entry: " + entryKeyValue.getKey() + ", value: " + entryKeyValue.getValue());
				writer.write(String.format(
						"%s = %s%n",
						entryKeyValue.getKey(),
						entryKeyValue.getValue()
				));
			}
			

			// write empty line between sections
			writer.write(String.format( "%n" ));
		} // for
		
	}
	
	/**
	 * Get value from memory return null if it didn't exist.
	 */
	public String getValue(String sectionName, String entryName) {
		return getValue(sectionName, entryName, null);
	}
	
	/**
	 * Get value from memory. Returns "defaultValue" if it didn't exist.
	 */
	public String getValue(String sectionName, String entryName, String defaultValue) {

		sectionName = sectionName.toLowerCase();
		entryName = entryName.toLowerCase();
		
		String returnValue = null;
		
		if (data.get(sectionName) != null) {
			// section exists, get entry value
			returnValue = data.get( sectionName ).get( entryName );
		}
		
		if (returnValue == null) returnValue = defaultValue;
		
		return returnValue;
	}
	
	/**
	 * Write value to memory.
	 */
	public void setValue(String sectionName, String entryName, String entryValue) {

		sectionName = sectionName.toLowerCase();
		entryName = entryName.toLowerCase();
		
		if (data.get(sectionName) == null) {
			data.put(sectionName, new LinkedHashMap<String, String>());
		}

		data.get(sectionName).put(entryName, entryValue);
	}
	
	/**
	 * Delete value from memory.
	 */
	public void deleteValue(String sectionName, String entryName) {

		sectionName = sectionName.toLowerCase();
		entryName = entryName.toLowerCase();
		
		if (data.get(sectionName) == null) return;

		data.get(sectionName).remove( entryName );
		
		if (data.get(sectionName).size() == 0) {
			data.remove(sectionName);
		}
		
	}
	
	/**
	 * Get sections from memory.
	 */
	public ArrayList<String> configGetSections() {
		return new ArrayList<String>(data.keySet());
	}

	/**
	 * Get entries of a section from memory.
	 */
	public ArrayList<String> configGetEntries(String sectionName) {
		sectionName.toLowerCase();
		if (data.get(sectionName) == null) return null;
		return new ArrayList<String>(data.get(sectionName).keySet());
	}
}
