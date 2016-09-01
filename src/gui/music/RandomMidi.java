package gui.music;

import java.util.Random;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

public class RandomMidi {
	public static final int	MIDDLE_NOTE_C	= 60;

	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException,
			InterruptedException {
		Synthesizer synthesizer = MidiSystem.getSynthesizer();
		synthesizer.open();

		Sequencer sequencer = MidiSystem.getSequencer();
		Sequence sequence = new Sequence(Sequence.PPQ, 10);

		Soundbank sb = synthesizer.getDefaultSoundbank();

		Instrument[] instruments = sb.getInstruments();
		System.out.println(instruments.length);
		synthesizer.loadInstrument(instruments[0]);

		MidiChannel midiChannels[] = synthesizer.getChannels();
		System.out.println(midiChannels.length);
		MidiChannel channel = midiChannels[0];
		int velocity, pressure, bend, reverb;

		velocity = pressure = bend = reverb = 256;
//		for (int i = 0; i < 8; i++) {
//			channel.noteOn(MIDDLE_NOTE_C + i, velocity);
//			Thread.sleep(velocity);
//			channel.noteOff(MIDDLE_NOTE_C + i, velocity);
//		}
//		Thread.sleep(velocity);
//		for (int i = 7; i >= 0; i--) {
//			channel.noteOn(MIDDLE_NOTE_C + i, velocity);
//			Thread.sleep(velocity);
//			channel.noteOff(MIDDLE_NOTE_C + i, velocity);
//		}
		
		Thread.sleep(velocity);
		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i <= 20; i++) {
			int note = rand.nextInt(128);
			channel.noteOn(note, velocity);
			Thread.sleep(velocity);
			channel.noteOff(note, velocity);
		}
		
		Thread.sleep(velocity);
		Random rand2 = new Random(System.currentTimeMillis());
		for (int i = 0; i <= 20; i++) {
			int note = rand2.nextInt(60);
			channel.noteOn(note, velocity);
			Thread.sleep(velocity);
			channel.noteOff(note, velocity);
		}
		
		Thread.sleep(velocity);
		Random rand3 = new Random(System.currentTimeMillis());
		for (int i = 0; i <= 20; i++) {
			int note = 60 + rand3.nextInt(60);
			channel.noteOn(note, velocity);
			Thread.sleep(velocity);
			channel.noteOff(note, velocity);
		}

		if (synthesizer != null) {
			synthesizer.close();
		}
		if (sequencer != null) {
			sequencer.close();
		}
		sequencer = null;
		synthesizer = null;
		instruments = null;
	}
}
