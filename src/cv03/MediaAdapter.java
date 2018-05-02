/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tul.ales.foldyna.mediaPlayer;

/**
 *
 * @author alesf_000
 */
public class MediaAdapter implements IMediaPlayer{
    
    private IAdvanceMediaPlayer advanceMediaPlayer;
    
    public MediaAdapter(String audioType){
        if(audioType.equalsIgnoreCase("mp4")){
            advanceMediaPlayer = new Mp4MediaPlayer();
        } else if (audioType.equalsIgnoreCase("vlc")){
            advanceMediaPlayer = new VlcMediaPlayer();
        }
    }
    
}
