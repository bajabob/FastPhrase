package fastphrase.com.helpers;

import android.os.Environment;

import java.io.File;

import fastphrase.com.models.Recording;

/**
 * Created by bob on 4/6/16.
 */
public class RecordingFileSystem {

    private static final String FILENAME_PREFIX = "Recording_";
    private static final String FILENAME_EXTENSION = ".mp4";
    private static final String FOLDER = "FastPhrase";

    private Recording mRecording;

    public RecordingFileSystem(Recording recording){
        mRecording = recording;

        // ensure the folder structure is setup on disk
        ensurePath( getPath() );
    }

    /**
     * Ensure the specified path exists on disk, and create any required
     * folders if needed
     * @param path String
     */
    private void ensurePath(String path){
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdirs();
        }
    }

    /**
     * Returns the path to the storage directory
     * @return String
     */
    public String getPath(){
        return Environment.getExternalStorageDirectory() + File.separator + FOLDER + File.separator;
    }

    /**
     * Returns the path and filename to the recording
     * @return String
     */
    public String getFilenameAndPath(){
        return this.getPath() + this.getFilename();
    }

    /**
     * Get just the filename of the stored file
     * @return String
     */
    public String getFilename(){
        return FILENAME_PREFIX + Long.toString(mRecording.hash) + FILENAME_EXTENSION;
    }

    /**
     * Delete the current recording
     */
    public void deleteCurrent(){
        File file = new File(getFilenameAndPath());
        if(file.exists()){
            file.delete();
        }
    }
}
