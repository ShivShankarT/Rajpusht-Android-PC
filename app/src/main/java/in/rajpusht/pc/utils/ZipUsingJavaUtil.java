package in.rajpusht.pc.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUsingJavaUtil {
    private static final String LOG_TAG = "ZipUsingJavaUtil";

    public static void zipFiles(List<File> files, String zipFile) throws IOException {


        try {

            // create byte buffer
            byte[] buffer = new byte[1024];
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (int i = 0; i < files.size(); i++) {
                File srcFile = (files.get(i));
                FileInputStream fis = new FileInputStream(srcFile);
                // begin writing a new ZIP entry, positions the stream to the start of the entry data
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                // close the InputStream
                fis.close();
            }
            // close the ZipOutputStream
            zos.close();

        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }

    }

    public void unzip(final File file, final File destination) {
        long START_TIME = System.currentTimeMillis();
        long FINISH_TIME = 0;
        long ELAPSED_TIME = 0;
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(file));
            String workingDir = destination.getAbsolutePath() + "/";

            byte[] buffer = new byte[4096];
            int bytesRead;
            ZipEntry entry = null;
            while ((entry = zin.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    File dir = new File(workingDir, entry.getName());
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    Log.i(LOG_TAG, "[DIR] " + entry.getName());
                } else {
                    FileOutputStream fos = new FileOutputStream(workingDir + entry.getName());
                    while ((bytesRead = zin.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                    fos.close();
                    Log.i(LOG_TAG, "[FILE] " + entry.getName());
                }
            }
            zin.close();

            FINISH_TIME = System.currentTimeMillis();
            ELAPSED_TIME = FINISH_TIME - START_TIME;
            Log.i(LOG_TAG, "COMPLETED in " + (ELAPSED_TIME / 1000) + " seconds.");
        } catch (Exception e) {
            e.fillInStackTrace();
            Log.e(LOG_TAG, "FAILED", e);
        }
    }

    /*
     * Zip function zip all files and folders
     */
    @SuppressWarnings("finally")
    public boolean zipFiles(String srcFolder, String destZipFile) {
        boolean result = false;
        try {
            System.out.println("Program Start zipping the given files");
            /*
             * send to the zip procedure
             */
            zipFolder(srcFolder, destZipFile);
            result = true;
            System.out.println("Given files are successfully zipped");
        } catch (Exception e) {
            System.out.println("Some Errors happned during the zip process");
        } finally {
            return result;
        }
    }

    /*
     * zip the folders
     */
    private void zipFolder(String srcFolder, String destZipFile) throws Exception {
        ZipOutputStream zip = null;
        FileOutputStream fileWriter = null;
        /*
         * create the output stream to zip file result
         */
        fileWriter = new FileOutputStream(destZipFile);
        zip = new ZipOutputStream(fileWriter);
        /*
         * add the folder to the zip
         */
        addFolderToZip("", srcFolder, zip);
        /*
         * close the zip objects
         */
        zip.flush();
        zip.close();
    }

    /*
     * recursively add files to the zip files
     */
    private void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag) throws Exception {
        /*
         * create the file object for inputs
         */
        File folder = new File(srcFile);

        /*
         * if the folder is empty add empty folder to the Zip file
         */
        if (flag == true) {
            zip.putNextEntry(new ZipEntry(path + "/" + folder.getName() + "/"));
        } else { /*
         * if the current name is directory, recursively traverse it
         * to get the files
         */
            if (folder.isDirectory()) {
                /*
                 * if folder is not empty
                 */
                addFolderToZip(path, srcFile, zip);
            } else {
                /*
                 * write the file to the output
                 */
                byte[] buf = new byte[1024];
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
                while ((len = in.read(buf)) > 0) {
                    /*
                     * Write the Result
                     */
                    zip.write(buf, 0, len);
                }
            }
        }
    }

    /*
     * add folder to the zip file
     */
    private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
        File folder = new File(srcFolder);

        /*
         * check the empty folder
         */
        if (folder.list().length == 0) {
            System.out.println(folder.getName());
            addFileToZip(path, srcFolder, zip, true);
        } else {
            /*
             * list the files in the folder
             */
            for (String fileName : folder.list()) {
                if (path.equals("")) {
                    addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip, false);
                } else {
                    addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip, false);
                }
            }
        }
    }
}