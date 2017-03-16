/*
 * Copyright 2011 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ahri.chat.util.ocr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.ahri.chat.base.BaseChatFragment;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Installs the language data required for OCR, and initializes the OCR engine using a background 
 * thread.
 */
public final class OcrInitAsyncTask extends AsyncTask<String, String, Boolean> {
  private static final String TAG = OcrInitAsyncTask.class.getSimpleName();

  /** Suffixes of required data files for Cube. */
  private static final String[] CUBE_DATA_FILES = {
    ".cube.bigrams",
    ".cube.fold", 
    ".cube.lm", 
    ".cube.nn", 
    ".cube.params", 
    //".cube.size", // This file is not available for Hindi
    ".cube.word-freq", 
    ".tesseract_cube.nn", 
    ".traineddata"
  };
  private BaseChatFragment baseChatFragment;
  private Context context;
  private TessBaseAPI baseApi;
  private ProgressDialog dialog;
  private final String languageCode;
  private String languageName;
  private int ocrEngineMode;

  /**
   * AsyncTask to asynchronously download data and initialize Tesseract.
   * 
   * @param activity
   *          The calling activity
   * @param baseApi
   *          API to the OCR engine
   * @param dialog
   *          Dialog box with thermometer progress indicator
   * @param indeterminateDialog
   *          Dialog box with indeterminate progress indicator
   * @param languageCode
   *          ISO 639-2 OCR language code
   * @param languageName
   *          Name of the OCR language, for example, "English"
   * @param ocrEngineMode
   *          Whether to use Tesseract, Cube, or both
   */
  public OcrInitAsyncTask(BaseChatFragment baseChatFragment, Activity activity, TessBaseAPI baseApi, ProgressDialog dialog,
                          ProgressDialog indeterminateDialog, String languageCode, String languageName,
                          int ocrEngineMode) {
   this.baseChatFragment = baseChatFragment;
//    this.activity = activity;
    this.context = activity.getBaseContext();
    this.baseApi = baseApi;
    this.dialog = dialog;
    this.languageCode = languageCode;
    this.languageName = languageName;
    this.ocrEngineMode = ocrEngineMode;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    dialog.setTitle("Please wait");
    dialog.setMessage("Checking for data installation...");
    dialog.setIndeterminate(false);
    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    dialog.setCancelable(false);
    dialog.show();
  }

  /**
   * In background thread, perform required setup, and request initialization of
   * the OCR engine.
   * 
   * @param params
   *          [0] Pathname for the directory for storing language data files to the SD card
   */
  protected Boolean doInBackground(String... params) {
    String destinationFilenameBase = languageCode + ".traineddata";
    String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    String datapath = absolutePath + "/tesseract/";
    File tessSubDir = new File(datapath, "tessdata");
    tessSubDir.mkdirs();
    if (!tessSubDir.exists()) {
      tessSubDir.mkdirs();
    }
    if (!tessSubDir.exists() && !tessSubDir.mkdirs()) {
      Log.e(TAG, "Couldn't make directory " + tessSubDir);
      return false;
    }

    // Create a reference to the file to save the download in
//    File downloadFile = new File(destinationDirBase, destinationFilenameBase);

    // Check if an incomplete download is present. If a *.download file is there, delete it and
    // any (possibly half-unzipped) Tesseract and Cube data files that may be there.
    File incomplete = new File(tessSubDir, destinationFilenameBase + ".download");
    File tesseractTestFile = new File(tessSubDir, languageCode + ".traineddata");
    if (incomplete.exists()) {
      incomplete.delete();
      if (tesseractTestFile.exists()) {
        tesseractTestFile.delete();
      }
      deleteCubeDataFiles(tessSubDir);
    }


    // If language data files are not present, install them
    boolean installSuccess = false;
    if (!tesseractTestFile.exists()) {
      Log.d(TAG, "Language data for " + languageCode + " not found in " + tessSubDir.toString());
      deleteCubeDataFiles(tessSubDir);

      // Check assets for language data to install. If not present, download from Internet
      try {
        Log.d(TAG, "Checking for language data (" + destinationFilenameBase
            + ".zip) in application assets...");
        // Check for a file like "eng.traineddata.zip" or "tesseract-ocr-3.01.eng.tar.zip"
        installSuccess = installFromAssets(destinationFilenameBase + ".zip", tessSubDir,
                tessSubDir);
      } catch (IOException e) {
        Log.e(TAG, "IOException", e);
      } catch (Exception e) {
        Log.e(TAG, "Got exception", e);
      }


    } else {
      Log.d(TAG, "Language data for " + languageCode + " already installed in " 
          + tessSubDir.toString());
      installSuccess = true;
    }


    // Dismiss the progress dialog box, revealing the indeterminate dialog box behind it
    try {
      dialog.dismiss();
    } catch (IllegalArgumentException e) {
      // Catch "View not attached to window manager" error, and continue
    }

    // Initialize the OCR engine
    if (baseApi.init(datapath , languageCode, ocrEngineMode)) {
      return installSuccess ;
    }
    return false;
  }

  /**
   * Delete any existing data files for Cube that are present in the given directory. Files may be 
   * partially uncompressed files left over from a failed install, or pre-v3.01 traineddata files.
   * 
   * @param tessdataDir
   *          Directory to delete the files from
   */
  private void deleteCubeDataFiles(File tessdataDir) {
    File badFile;
    for (String s : CUBE_DATA_FILES) {
      badFile = new File(tessdataDir.toString() + File.separator + languageCode + s);
      if (badFile.exists()) {
        Log.d(TAG, "Deleting existing file " + badFile.toString());
        badFile.delete();
      }
      badFile = new File(tessdataDir.toString() + File.separator + "tesseract-ocr-3.01." 
          + languageCode + ".tar");
      if (badFile.exists()) {
        Log.d(TAG, "Deleting existing file " + badFile.toString());
        badFile.delete();
      }
    }
  }



  /**
   * Unzips the given Gzipped file to the given destination, and deletes the
   * gzipped file.
   * 
   * @param zippedFile
   *          The gzipped file to be uncompressed
   * @param outFilePath
   *          File to unzip to, including path
   * @throws FileNotFoundException
   * @throws IOException
   */
  private void gunzip(File zippedFile, File outFilePath)
      throws FileNotFoundException, IOException {
    int uncompressedFileSize = getGzipSizeUncompressed(zippedFile);
    Integer percentComplete;
    int percentCompleteLast = 0;
    int unzippedBytes = 0;
    final Integer progressMin = 0;
    int progressMax = 100 - progressMin;
    publishProgress("Uncompressing data for " + languageName + "...",
        progressMin.toString());

    // If the file is a tar file, just show progress to 50%
    String extension = zippedFile.toString().substring(
        zippedFile.toString().length() - 16);
    if (extension.equals(".tar.gz.download")) {
      progressMax = 50;
    }
    GZIPInputStream gzipInputStream = new GZIPInputStream(
        new BufferedInputStream(new FileInputStream(zippedFile)));
    OutputStream outputStream = new FileOutputStream(outFilePath);
    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
        outputStream);

    final int BUFFER = 8192;
    byte[] data = new byte[BUFFER];
    int len;
    while ((len = gzipInputStream.read(data, 0, BUFFER)) > 0) {
      bufferedOutputStream.write(data, 0, len);
      unzippedBytes += len;
      percentComplete = (int) ((unzippedBytes / (float) uncompressedFileSize) * progressMax)
          + progressMin;

      if (percentComplete > percentCompleteLast) {
        publishProgress("Uncompressing data for " + languageName
            + "...", percentComplete.toString());
        percentCompleteLast = percentComplete;
      }
    }
    gzipInputStream.close();
    bufferedOutputStream.flush();
    bufferedOutputStream.close();

    if (zippedFile.exists()) {
      zippedFile.delete();
    }
  }

  /**
   * Returns the uncompressed size for a Gzipped file.
   * 
   * @param
   *
   * @return Size when uncompressed, in bytes
   * @throws IOException
   */
  private int getGzipSizeUncompressed(File zipFile) throws IOException {
    RandomAccessFile raf = new RandomAccessFile(zipFile, "r");
    raf.seek(raf.length() - 4);
    int b4 = raf.read();
    int b3 = raf.read();
    int b2 = raf.read();
    int b1 = raf.read();
    raf.close();
    return (b1 << 24) | (b2 << 16) + (b3 << 8) + b4;
  }


  /**
   * Install a file from application assets to device external storage.
   * 
   * @param sourceFilename
   *          File in assets to install
   * @param modelRoot
   *          Directory on SD card to install the file to
   * @param destinationFile
   *          File name for destination, excluding path
   * @return True if installZipFromAssets returns true
   * @throws IOException
   */
  private boolean installFromAssets(String sourceFilename, File modelRoot,
      File destinationFile) throws IOException {
    String extension = sourceFilename.substring(sourceFilename.lastIndexOf('.'), 
        sourceFilename.length());
    try {
      if (extension.equals(".zip")) {
        return installZipFromAssets(sourceFilename, modelRoot, destinationFile);
      } else {
        throw new IllegalArgumentException("Extension " + extension
            + " is unsupported.");
      }
    } catch (FileNotFoundException e) {
      Log.d(TAG, "Language not packaged in application assets.");
    }
    return false;
  }

  /**
   * Unzip the given Zip file, located in application assets, into the given
   * destination file.
   * 
   * @param sourceFilename
   *          Name of the file in assets
   * @param destinationDir
   *          Directory to save the destination file in
   * @param destinationFile
   *          File to unzip into, excluding path
   * @return
   * @throws IOException
   * @throws FileNotFoundException
   */
  private boolean installZipFromAssets(String sourceFilename,
      File destinationDir, File destinationFile) throws IOException,
      FileNotFoundException {
    // Attempt to open the zip archive
    publishProgress("Uncompressing data for " + languageName + "...", "0");
    ZipInputStream inputStream = new ZipInputStream(context.getAssets().open(sourceFilename));

    // Loop through all the files and folders in the zip archive (but there should just be one)
    for (ZipEntry entry = inputStream.getNextEntry(); entry != null; entry = inputStream
        .getNextEntry()) {
      destinationFile = new File(destinationDir, entry.getName());

      if (entry.isDirectory()) {
        destinationFile.mkdirs();
      } else {
        // Note getSize() returns -1 when the zipfile does not have the size set
        long zippedFileSize = entry.getSize();

        // Create a file output stream
        FileOutputStream outputStream = new FileOutputStream(destinationFile);
        final int BUFFER = 8192;

        // Buffer the output to the file
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, BUFFER);
        int unzippedSize = 0;

        // Write the contents
        int count = 0;
        Integer percentComplete = 0;
        Integer percentCompleteLast = 0;
        byte[] data = new byte[BUFFER];
        while ((count = inputStream.read(data, 0, BUFFER)) != -1) {
          bufferedOutputStream.write(data, 0, count);
          unzippedSize += count;
          percentComplete = (int) ((unzippedSize / (long) zippedFileSize) * 100);
          if (percentComplete > percentCompleteLast) {
            publishProgress("Uncompressing data for " + languageName + "...", 
                percentComplete.toString(), "0");
            percentCompleteLast = percentComplete;
          }
        }
        bufferedOutputStream.close();
      }
      inputStream.closeEntry();
    }
    inputStream.close();
    return true;
  }

  /**
   * Update the dialog box with the latest incremental progress.
   * 
   * @param message
   *          [0] Text to be displayed
   * @param message
   *          [1] Numeric value for the progress
   */
  @Override
  protected void onProgressUpdate(String... message) {
    super.onProgressUpdate(message);
    int percentComplete = 0;

    percentComplete = Integer.parseInt(message[1]);
    dialog.setMessage(message[0]);
    dialog.setProgress(percentComplete);
    dialog.show();
  }

  @Override
  protected void onPostExecute(Boolean result) {
    super.onPostExecute(result);
    
    try {
      Toast.makeText(context, "OCR init success", Toast.LENGTH_SHORT).show();
      dialog.dismiss();
      baseChatFragment.setInitSuccess(true);
    } catch (IllegalArgumentException e) {
    }

  }
}