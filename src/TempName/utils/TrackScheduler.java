package TempName.utils;

import java.util.Queue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class TrackScheduler extends AudioEventAdapter {
	private Queue<AudioTrack> queue;
	private AudioPlayer player;
	
	
	public TrackScheduler(AudioPlayer player){
		this.player = player;
		
	}
	public void queue(AudioTrack track){
		if (!player.startTrack(track, true)) {
		      queue.offer(track);
		    }
	}
	public void nextTrack(){
		if(queue.peek() == null){
			return;
		}
		else player.playTrack(queue.poll());
	}
	
	public void onPlayerPause(AudioPlayer player) {
	    // Player was paused
		  
		  
	}

	  
	public void onPlayerResume(AudioPlayer player) {
	    // Player was resumed
	}

	  
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
	    // A track started playing
	}

	  
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			nextTrack();
			
	      // Start next track
		}

	    // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
	    // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
	    // endReason == STOPPED: The player was stopped.
	    // endReason == REPLACED: Another track started playing while this had not finished
	    // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
	    //                       clone of this back to your queue
	}

	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
	    // An already playing track threw an exception (track end event will still be received separately)
	}

	  
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
	    // Audio track has been unable to provide us any audio, might want to just start a new track
	}
}