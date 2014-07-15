/* [LGPL] Copyright 2010, 2011 Gima

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
package fi.conf.ae.image.netpbm;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Supports writing Netpbm images; Only PPM(rgb) and PGM(greyscale) are supported.
 * TODO: Add PBM(bitmap) support
 * 
 * @see https://secure.wikimedia.org/wikipedia/en/wiki/Netpbm_format
 */
public class PPMWriter {
	
	/** Static methods only, no instantiation */
	private PPMWriter() {
	}
	
	private static final int BUFFER_SIZE = 4096;
	
	/**
	 * Reads data from inputStream and writes it to outputStream in PPM/PGM image format (format decided by useColor).
	 * Maximum pixel component value is set to 255.
	 * <p>
	 * Pixels are written top-down, one row at a time from left to right.
	 * Data is read from input stream and interpreted as follows:
	 * <ul>
	 * <li>PPM: one red byte, one green byte and one blue byte is read from the input stream.</li>
	 * <li>PGM: one grayscale byte is read from the input stream.</li>
	 * </ul>
	 * After successfully writing the whole image, input stream position will be incremented by:
	 * <ul>
	 * <li>PPM: imageWidth * imageHeight * 3 bytes</li>
	 * <li>PGM: imageWidth * imageHeight * 1 bytes</li>
	 * </ul>
	 * In case of exceptions, the amount of bytes read or written to the streams is unspecified.
	 * 
	 * @param useColor - True for PPM format (color), false for PGM (grayscale)
	 * @param inputStream - Where the to-be-image data is read from.
	 * @param imageWidth - Width of the created image.
	 * @param imageHeight - Height of the created image.
	 * @param outputStream - Where the image data will be written to.
	 * @throws PPMWriterException Thrown in case of any I/O errors.
	 */
	public static void write(
			boolean useColor,
			InputStream inputStream,
			int imageWidth,
			int imageHeight,
			OutputStream outputStream
	) throws PPMWriterException {
		
		final DataInputStream dataInputStream = new DataInputStream(inputStream);
		
		final byte buffer[] = new byte[BUFFER_SIZE];

		final int dataLength = (imageWidth * imageHeight * (useColor ? 3 : 1));
		int totalNumBytesRead = 0;
		int readNumBytes;
		
		// write header
		
		final String formatSpecifier = useColor ? "P6" : "P5";
		
		try {
			outputStream.write(String.format(
					formatSpecifier + "\n%d %d\n255\n",
					imageWidth, imageHeight
					).getBytes()
					);
		}
		catch (IOException e) {
			throw new PPMWriterException("could not write header");
		}
		
		// read data to buffer and write it out until requested amount of data is read or error is encountered
		
		do {
			
			readNumBytes = dataLength - totalNumBytesRead;
			
			if (readNumBytes > buffer.length) {
				readNumBytes = buffer.length;
			}
			
			try {
				dataInputStream.readFully(buffer, 0, readNumBytes);
				totalNumBytesRead += readNumBytes;
			}
			catch (EOFException e) {
				// end of stream but not all requested data was read
				throw new PPMWriterException("stream end encountered before data could be completely read");
			}
			catch (IOException e) {
				throw new PPMWriterException("error while reading from input stream");
			}
			
			// there is still data to be read, continue looping
			
			try {
				outputStream.write(buffer, 0, readNumBytes);
			}
			catch (IOException e) {
				throw new PPMWriterException("error writing to output stream");
			}
			
						
		} // do
		while (totalNumBytesRead < dataLength);
	}
	
	/**
	 * Calls {@link #write(boolean, InputStream, int, int, OutputStream) the generic Netpbm write method} with <i>useColor</i> parameter set to <b>true</b>
	 */
	public static void writePPM(
			InputStream inputStream,
			int imageWidth,
			int imageHeight,
			OutputStream outputStream
	) throws PPMWriterException {
		write(true, inputStream, imageWidth, imageHeight, outputStream); 
	}
	
	/**
	 * Calls {@link #write(boolean, InputStream, int, int, OutputStream) the generic Netpbm write method} with <i>useColor</i> parameter set to <b>false</b>
	 */
	public static void writePGM(
			InputStream inputStream,
			int imageWidth,
			int imageHeight,
			OutputStream outputStream
	) throws PPMWriterException {
		write(false, inputStream, imageWidth, imageHeight, outputStream); 
	}
		
}
