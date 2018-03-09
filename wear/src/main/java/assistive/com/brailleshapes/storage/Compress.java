package assistive.com.brailleshapes.storage;

/**
 * Created by kyle montague on 11/05/15.
 */


import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Compress {
    private static final int BUFFER = 2048;

    private String[] _files;
    private String _zipFile;

    public Compress(String[] files, String zipFile) {
        _files = files;
        _zipFile = zipFile;
    }

    public Compress(String parent, String zipFile){
        _zipFile = zipFile;
        List<File> listOfFiles = getListFiles(new File(parent));
        _files = new String[listOfFiles.size()];
        for(int x=0;x<listOfFiles.size();x++){
            _files[x] = listOfFiles.get(x).getAbsolutePath();
        }
    }

    public boolean zip() {
        if(_files.length == 0)
            return false;
        try  {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(_zipFile);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];

            for(int i=0; i < _files.length; i++) {
                Log.v("Compress", "Adding: " + _files[i]);
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        if(files == null || files.length == 0) {
            Log.d("COMPRESS","NO FILES IN: "+parentDir.getPath());
            return inFiles;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if(file.getName().endsWith(".csv")){
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }


    public static void DeleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();

    }


    public static void Delete(String[] files) {
        if(files != null && files.length > 0)
            for(String filename: files) {
                File f = new File(filename);
                if(f.isFile())
                    f.delete();
            }
    }

    public static String ZipRecursive(File Directory) {
        String path="";
        ArrayList<String> filesToZip = new ArrayList<String>();
        if(Directory != null && Directory.listFiles().length > 0)
            for (File child : Directory.listFiles()) {
                if(child.isDirectory())
                    filesToZip.add(Compress.ZipRecursive(child));
                else
                    filesToZip.add(child.getAbsolutePath());
            }
        if(filesToZip.size() > 0) {
            String zip = Directory.getName()+".zip";
            String[] files = (String[])filesToZip.toArray();
            Compress c = new Compress(files,zip);
            if(c.zip()) {
                path = zip;
                Compress.Delete(files);
            }
        }
        return path;
    }

}