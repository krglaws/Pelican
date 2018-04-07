package pelican.pelican;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kyleg on 4/6/2018.
 */

public class VideoUploadTask extends AsyncTask<String, Void, Integer> {

    private static final String TAG = "VideoUploadTask";

    @Override
    protected Integer doInBackground(String... strings) {

        String uploadServerUri = "http://entropy7.nas.eckerd.edu/pelican/upload.php";
        String sourceFileUri = strings[0];
        int bytesRead, bytesAvailable, bufferSize, serverResponseCode = -1;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        HttpURLConnection connection = null;
        DataOutputStream dos = null;
        URL url = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(sourceFileUri));
            url = new URL(uploadServerUri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true); // Allow Inputs
            connection.setDoOutput(true); // Allow Outputs
            connection.setUseCaches(false); // Don't use a Cached Copy
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            connection.setRequestProperty("uploaded_file", sourceFileUri);
            dos = new DataOutputStream(connection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes(
                    "Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + sourceFileUri + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available(); // create a buffer of maximum size
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();
            Log.d(TAG,"Connecting to server, HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
            // close streams
            fileInputStream.close();
            dos.flush();
            dos.close();
        }catch(FileNotFoundException e){
            Log.d(TAG, e.toString());
            return null;
        }catch(MalformedURLException e){
            Log.d(TAG, e.toString());
            return null;
        }catch(IOException e){
            Log.d(TAG, e.toString());
            return null;
        }

        // this block will give the response of upload link
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                Log.d(TAG, line);
            }
            rd.close();
        } catch (IOException ioex) {
            Log.d(TAG, ioex.getMessage());
        }
        return serverResponseCode; // like 200 (Ok)
    }
}

