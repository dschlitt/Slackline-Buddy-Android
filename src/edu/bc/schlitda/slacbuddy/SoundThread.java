package edu.bc.schlitda.slacbuddy;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;



public class SoundThread extends Thread {
	private MainActivity parent;
	private MediaPlayer mediaPlayer=null;
	private boolean death;
	
	
	
	public SoundThread(MainActivity parent){
		this.parent = parent;
		death = false;
	}
	
	@Override
	public void run(){
		System.out.println("SoundThread Started");
		begin();
	}

	public void begin(){
		while(parent.isCalibrated()){
			if(death){
				return;
			}
			float accelerometerChange = calculateChange();
			if (accelerometerChange <= 0){
				continue;
			}
			if(mediaPlayer != null)
					continue;
			
			playSound(accelerometerChange); //should block on playing sound... 
		}
	}
	private void playSound(float accelerometerChange) {
		
		float ac = accelerometerChange;
		float s = parent.getSensitivity();
		
		float pause = 2 - .5f*ac;  //2 seconds is slowest beep and gets faster
		float duration = .2f - .199f*ac; //.2 is longest tone
		float freq = 440 + 1000*ac; //range of tones is 440 - 1440 ()
		int sampleRate = 8000; // why?
		
		
		float fnumSamples = duration * sampleRate + pause * duration;
		int numSamples = (int) fnumSamples;
		
		if (numSamples < 800){
			/*Prevents annoying sounds from high frequency beeps 
			and occasional crashing due to negative array size*/
			numSamples = 800;
		}
		
		
		byte snd[] = generateTone(sampleRate, freq, numSamples);;
	
		
		AudioTrack audioTrack = null;                 // Get audio track
        try {
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    sampleRate, AudioFormat.CHANNEL_OUT_DEFAULT,
                    AudioFormat.ENCODING_PCM_16BIT, (int)numSamples*2,
                    AudioTrack.MODE_STATIC);
            audioTrack.write(snd, 0, snd.length);     // Load the track
            audioTrack.play();                                          // Play the track
        }
        catch (Exception e){
          
        }

        int x =0;
        do{                                                     // Montior playback to find when done
             if (audioTrack != null) 
                 x = audioTrack.getPlaybackHeadPosition(); 
             else 
                 x = numSamples;            
        }while (x<numSamples);

        if (audioTrack != null) audioTrack.release();
		
		//needs to be tweaked

//		float step = s * 0.3f;
//		if (ac < s + step){
//			play(R.raw.two);
//			System.out.println("2");
//			return;
//		}
//		if (ac < s + step * 2){
//			play(R.raw.onepointfive);
//			System.out.println("1.5");
//			return;
//		}
//		if (ac < s + step * 3){
//			play(R.raw.onehigher);
//			System.out.println("1");
//			return;
//		}
//		if (ac < s + step * 4){
//			play(R.raw.pointfivehigher);
//			System.out.println(".5");
//			return;
//		}
//		
//			play(R.raw.pointthreehigher);  //requires 30% change to activate
//			System.out.println(".33");
//			return;
		
	}	
		
	private byte[] generateTone(int sampleRate, float freq, int numSamples) {
		float sample[] = new float[numSamples];
		byte snd[] = new byte [2* numSamples];
		
		for (int i = 0; i < numSamples; ++ i){
			sample[i] = (float) Math.sin((2 * Math.PI - .001) * i / (sampleRate/freq));
		}
		
		int idx = 0;
		int ramp = numSamples/20;
		
	    for (int i = 0; i < ramp; i++) {
	        // scale to maximum amplitude
	        final short val = (short) ((sample[i] * 32767) * i / ramp);
	        // in 16 bit wav PCM, first byte is the low order byte
	        snd[idx++] = (byte) (val & 0x00ff);
	        snd[idx++] = (byte) ((val & 0xff00) >>> 8);
	    }
	 
	    for (int i = ramp; i < numSamples - ramp; i++) {
	        // scale to maximum amplitude
	        final short val = (short) ((sample[i] * 32767));
	        // in 16 bit wav PCM, first byte is the low order byte
	        snd[idx++] = (byte) (val & 0x00ff);
	        snd[idx++] = (byte) ((val & 0xff00) >>> 8);
	    }
	 
	    for (int i = numSamples - ramp; i < numSamples; i++) {
	        // scale to maximum amplitude
	        final short val = (short) ((sample[i] * 32767) * (numSamples - i) / ramp);
	        // in 16 bit wav PCM, first byte is the low order byte
	        snd[idx++] = (byte) (val & 0x00ff);
	        snd[idx++] = (byte) ((val & 0xff00) >>> 8);
	    }
	    
		return snd;
	}		
	

//	
//	private void play(final int soundID) {
//				
//		mediaPlayer = MediaPlayer.create(parent, soundID);
//		mediaPlayer.setOnCompletionListener(this);
//		
//		parent.setMediaPlayer(mediaPlayer);
//		
//		System.out.println("Playing Sound!");
//		mediaPlayer.start();	
//		System.out.println("SoundFinished!");
//
//	}

	private float calculateChange() { //calculates wait time based on change in accelerometer
		
		float xDiff = Math.abs(parent.getSavedX() - parent.getX());
		float yDiff = Math.abs(parent.getSavedY() - parent.getY());
		float zDiff = Math.abs(parent.getSavedZ() - parent.getZ());
		if (zDiff < parent.getSensitivity())
			return -1;
		else
			return zDiff;
//		float temp;
//		float greatestDifference;
//		
//		if (xDiff > yDiff)
//			temp = xDiff;
//		else
//			temp = yDiff;
//		
//		if (temp > zDiff)
//			greatestDifference = temp;
//		else
//			greatestDifference = zDiff;
//		
//		if (greatestDifference < parent.getSensitivity())
//			return -1;
//		else
//			return greatestDifference;
	}

	public void flagForDeath() {
		death = true;
		
	}

//	@Override
//	public void onCompletion(MediaPlayer mp) {
//		mediaPlayer.setLooping(false);
//		mediaPlayer.stop();
//		mediaPlayer.release();
//		mediaPlayer = null;
//	}

}
