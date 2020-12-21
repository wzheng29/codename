package code;

import javax.sound.sampled.*;

public class AudioPlayer {

	private Clip clip;
	
	/**
	 * The name of the Audio file in the Media folder is passed in as a string into the constructor.
	 * It gets the resource and decodes it so that it can be used with a clip object.
	 * Clip grabs the resource and then it can be used with the stop start and close methods.
	 * 
	 * @author Eoghan McCarroll
	 * @param String s: The name of the Audio file in the Media folder
	 * **/
	
	public AudioPlayer(String s){
		
		
		try{
			
			AudioInputStream ais=AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/"+s));
			AudioFormat baseFormat=ais.getFormat();
			AudioFormat decodeFormat= new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(),
					16,
					baseFormat.getChannels(),
					baseFormat.getChannels()*2,
					baseFormat.getSampleRate(),
					false
					);
			AudioInputStream dais=
					AudioSystem.getAudioInputStream(
							decodeFormat,ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
			
			
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
		
		
	}
	/**
	 * If the clip is not null, it stops the clip from playing, and restarts/starts the clip.
	 * 
	 * **/
	public void play(){
		if(clip!=null) {
		stop();
		clip.setFramePosition(0);
		clip.start();
		}
		
	}
	/**
	 * If the clip is running, stop the clip
	 * 
	 * **/
	public void stop(){
		if(clip.isRunning()){
			clip.stop();
		}
	}
	/**
	 * Stops the clip and frees up any used system resources by the clip.
	 * 
	 * **/
	public void close(){
		stop();
		clip.close();
	}
	
	
	
}
