package game.audio;

import java.io.InputStream;
import java.nio.FloatBuffer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALCcontext;
import org.lwjgl.openal.ALCdevice;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.WaveData;

import com.google.common.io.Resources;

public class AudioTest {

	public static void main(String[] args) {
		try {
			AL.create();
			ALCcontext context = AL.getContext();
			ALCdevice device = AL.getDevice();
			if (!device.isValid()) {
				throw new RuntimeException("Invalid device");
			}
			if (!context.isValid()) {
				throw new RuntimeException("Invalid device");
			}
			checkError();

			int bufferId = AL10.alGenBuffers();
			checkError();

			WaveData waveFile = WaveData.create(Resources.getResource("test.wav"));
			
			
			checkError();
			AL10.alBufferData(bufferId, waveFile.format, waveFile.data, waveFile.samplerate);
			checkError();

			FloatBuffer Z = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
			Z.flip();

			FloatBuffer F = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 1.0f });
			F.flip();

			int sourceId = AL10.alGenSources();
			AL10.alSourcei(sourceId, AL10.AL_BUFFER, bufferId);
			AL10.alSourcef(sourceId, AL10.AL_PITCH, 1.0f);
			AL10.alSourcef(sourceId, AL10.AL_GAIN, 10.0f);
			AL10.alSource(sourceId, AL10.AL_POSITION, Z);
			AL10.alSource(sourceId, AL10.AL_VELOCITY, Z);
			checkError();

			AL10.alListener(AL10.AL_POSITION, Z);
			AL10.alListener(AL10.AL_VELOCITY, Z);
			AL10.alListener(AL10.AL_ORIENTATION, F);
			checkError();

			AL10.alSourcePlay(sourceId);

			checkError();
			Thread.sleep(3000);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			AL.destroy();
		}
	}

	private static void checkError() {
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			throw new RuntimeException("An error occured: " + String.format("%x", error));
		}
	}
}
