package iso.piotrowski.marek.nyndro;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.IOException;

/**
 * Created by Marek on 20.08.2016.
 */
public class NyndroBackupAgent extends BackupAgentHelper {

    private static final String DATABASE_NAME = "practice";
    private static final String DATABASE_NYNDRO_FILE = "practice";
    private static final String FILE_BACKUP_KEY = "nyndro";

    @Override
    public void onCreate() {
        FileBackupHelper backupHelper = new FileBackupHelper(this, DATABASE_NYNDRO_FILE);
        addHelper(FILE_BACKUP_KEY, backupHelper);
    }

    @Override
    public File getFilesDir() {
        File path = getDatabasePath(DATABASE_NAME);
        return path.getParentFile();
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        super.onRestore(data, appVersionCode, newState);
    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
        super.onBackup(oldState, data, newState);
    }
}
