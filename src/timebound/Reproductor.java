package timebound;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Reproductor implements Runnable{
	
	private FileInputStream fin;
        private AudioInputStream ais;
	private Clip clip;
        
	private boolean isSoundStored; //Java lo hace false por default
	private boolean isPlaying;
        private final static String AUDIOROUTE = "./src/resources/audio/";
        private File audioFILE;
	
	public void loopSound(String archivo)
	{
		setSound(archivo);
		
		if(isSoundStored) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			isPlaying = true;
		} else {
			isPlaying = false;
		}
		
	}
	
	public void loopSound()
	{
		if(isPlaying) {
		} else if(isSoundStored){
			isPlaying = true;
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
			System.out.println("No hay un archivo de audio en el reproductor.");
		}
	}

	public final void setSound(String archivo) {
		
            if(isSoundStored) {
                
                if(isPlaying) {
                    stopSound();
                }
                closeSoundFile();	
            }
            
            audioFILE = new File(AUDIOROUTE + archivo + ".wav");

            try {
                fin = new FileInputStream(audioFILE);
                clip = AudioSystem.getClip();
                ais = AudioSystem.getAudioInputStream(audioFILE);
                clip.open(ais);
                isSoundStored = true;
            } catch(NullPointerException | LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                System.out.println("No se encontro o ha elegido un archivo de audio invalido.");
                e.printStackTrace(System.out);
                isSoundStored = false;
            }
	}
	
	public void playSound()
	{
		if(isPlaying) {
		} else if(isSoundStored){
			isPlaying = true;
			clip.start();
		} else {
			System.out.println("No hay un archivo de audio en el reproductor.");
		}
	}
	
	public synchronized void playSound(String archivo)
	{
		setSound(archivo);
		
		if(isSoundStored) {
			clip.start();
			isPlaying = true;
		} else {
			isPlaying = false;
		}
	}

	public synchronized void stopSound() {
            if(isPlaying) {
                isPlaying = false;
                clip.stop();
            }
	}
	
	private synchronized void closeSoundFile() {
		
            if(isSoundStored) {
                isSoundStored = false;	

                try {
                    fin.close();
                    ais.close();
                    clip.close();
                    audioFILE = null;
                } catch(IOException e) {
                    System.out.println("dum dum");
                }
            }
	}


	public void pauseSound()
	{
		if(isPlaying) {
                    clip.stop();
                    isPlaying = false;
		}
	}
        
        public void sleepThenPause(long millisToSleep) {
            try {
                Thread.sleep(millisToSleep);
            } catch (InterruptedException ex) {
                Logger.getLogger(TimeBound.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
            
            pauseSound();
        }
	
	public Reproductor(String archivo) {          
            setSound(archivo);
	}
	
	public Reproductor() {
            //Placeholder
	}

        @Override
        public void run() {
            playSound();
        }
}
