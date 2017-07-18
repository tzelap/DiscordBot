package TempName.utils;


import java.util.function.Consumer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.events.message.priv.GenericPrivateMessageEvent;

public class MessageResponder extends ListenerAdapter{
	public void onMessageReceived(MessageReceivedEvent event){
		String message = event.getMessage().getContent();
		
		User author = event.getAuthor();
		String name = author.getName();
		Member mem = event.getMember();
		
		Guild guild = event.getGuild();
		
		
		
		
		
		
		
		
		
		if(author.isBot()){
			return;
		}
		
		if (message.startsWith("$help")) {
			
			if(!event.isFromType(ChannelType.PRIVATE)){
				//event.getTextChannel().sendMessage(author.getAsMention() + " So, I see that you're a bitch " + name + " and you need help, check your DMs... bitch").queue();
				sendGeneralHelp(event);
			}
			
			sendPrivateMessage(author, "Sup my niiiiyaargh " + name);
			sendPrivateMessage(author, Constants.responses);
			
			return;
			
		}
		
		AudioManager manager = guild.getAudioManager();
		AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
		playerManager.registerSourceManager(new LocalAudioSourceManager());
		//AudioSourceManagers.registerLocalSource(playerManager);
		AudioPlayer player = playerManager.createPlayer();
		TrackScheduler t = new TrackScheduler(player);
		AudioPlayerSendHandler sendHandler = new AudioPlayerSendHandler(player);
		if (message.startsWith("$chyea")){
			joinUsersVoice(event, mem, manager);
			manager.setSendingHandler(sendHandler);
			playSound("C:\\Users\\Edmund\\Desktop\\Chyaaaa.mp3", playerManager, player, t);
		}
		if (message.startsWith("$hungry")){
			joinUsersVoice(event, mem, manager);
			manager.setSendingHandler(sendHandler);
			playSound("C:\\Users\\Edmund\\Desktop\\hungry.mp3", playerManager, player, t);
		}
		if (message.startsWith("$blake")){
			joinUsersVoice(event, mem, manager);
			manager.setSendingHandler(sendHandler);
			playSound("C:\\Users\\Edmund\\Desktop\\fb1.mp3", playerManager, player, t);
		}
		if (message.startsWith("$stop")){
			guild.getAudioManager().setSendingHandler(null);
            guild.getAudioManager().closeAudioConnection();
       
		}
		
		
		
		
		
	}
	private void sendGeneralHelp (MessageReceivedEvent event){
		event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " So, I see that you're a bitch " + event.getAuthor().getName() + " and you need help, check your DMs... bitch").queue();
		return;
	}
	private void sendPrivateMessage(User user, String content){
		user.openPrivateChannel().queue(privateChannel -> {
			privateChannel.sendMessage(content).queue();
		});
		return;
		
	}
	
	
	
	
	
	
	/*
	private void sendPrivateMessage(User user, String content)
	{
	    // openPrivateChannel provides a RestAction<PrivateChannel> 
	    // which means it supplies you with the resulting channel
	    user.openPrivateChannel().queue((channel) ->
	    {
	        // value is a parameter for the `accept(T channel)` method of our callback.
	        // here we implement the body of that method, which will be called later by JDA automatically.
	        sendAndLog(channel, content);
	        // here we access the enclosing scope variable -content-
	        // which was provided to sendPrivateMessage(User, String) as a parameter
	    });
	}
	public void sendAndLog(MessageChannel channel, String message)
	{
	    // Here we use a lambda expressions which names the callback parameter -response- and uses that as a reference
	    // in the callback body -System.out.printf("Sent Message %s\n", response)-
	    Consumer<Message> callback = (response) -> System.out.printf("Sent Message %s\n", response);
	    channel.sendMessage(message).queue(callback); // ^ calls that
	}
	*/
	private void joinUsersVoice (MessageReceivedEvent event , Member member, AudioManager manager){
		if (!member.getVoiceState().inVoiceChannel()){
			sendPrivateMessage(member.getUser(), "Join a voice channel bitchass "+event.getAuthor());
			return;
		}
		else{
			VoiceChannel vCh = getUsersVoiceCh(event);
			
			manager.openAudioConnection(vCh);
			
			
		}
		
	}
	
	
	private void playSound (String sound, AudioPlayerManager manager, AudioPlayer player, TrackScheduler t ){
		
		//AudioLoadResultHandler handler = new AudioLoadResultHandler();
		
		
		manager.loadItem(sound, new AudioLoadResultHandler() {
			  @Override
			  public void trackLoaded(AudioTrack track) {
			    t.queue(track);
			  }
			  

			  @Override
			  public void playlistLoaded(AudioPlaylist playlist) {
			    for (AudioTrack track : playlist.getTracks()) {
			      t.queue(track);
			    }
			  }

			  @Override
			  public void noMatches() {
			    // Notify the user that we've got nothing
			  }

			  @Override
			  public void loadFailed(FriendlyException throwable) {
			    // Notify the user that everything exploded
			  }
			});
		
			
	
		
	}
	private VoiceChannel getUsersVoiceCh(MessageReceivedEvent event){
		
		Guild guild = event.getGuild();
		for(VoiceChannel channel: guild.getVoiceChannels()){
			for(Member member : channel.getMembers()){
				if(event.getMember() == member){
					return channel;
				}
			}
		}
		
		return null;
		
	}
	
	
}
