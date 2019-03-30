package yokohama.osm.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static java.nio.file.Files.readAllBytes;

public class FileUtil {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] convertFile(File file) throws IOException {
        return readAllBytes(file.toPath());
    }

}
