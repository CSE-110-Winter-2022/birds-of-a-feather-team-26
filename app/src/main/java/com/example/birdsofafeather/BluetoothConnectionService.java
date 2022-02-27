package com.example.birdsofafeather;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {
    private static final String TAG = "BluetoothConnectionService";

    private static final String appName = "birdsofafeather";

    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private final BluetoothAdapter myBluetoothAdapter;
    Context myContext;

    private AcceptThread myInsecureAcceptThread;

    private ConnectThread myConnectThread;
    private BluetoothDevice myDevice;
    private UUID deviceUUID;
    ProgressDialog myProgressDialog;

    private ConnectedThread myConnectedThread;

    public BluetoothConnectionService(Context context) {
        this.myContext = context;
        this.myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * ACCEPT THREAD CLASS
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled)
     */
    private class AcceptThread extends Thread {

        // The local server socket
        private final BluetoothServerSocket myServerSocket;

        /**
         * AcceptThread constructor
         */
        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try {
                tmp = myBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);

                Log.d(TAG, "AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
            } catch (IOException e) {
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }

            myServerSocket = tmp;
        }

        /**
         * Opens server socket for connection to another thread (device)
         */
        public void run() {
            Log.d(TAG, "run: AcceptThread Running.");

            BluetoothSocket socket = null;

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                Log.d(TAG, "run: RFCOM server socket start.....");

                socket = myServerSocket.accept();

                Log.d(TAG, "run: RFCOM server socket accepted connection.");

            } catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }

            if (socket != null) {
                connected(socket, myDevice);
            }

            Log.i(TAG, "End myAcceptThread");
        }

        /**
         * Cancel AcceptThread object
         */
        public void cancel() {
            Log.d(TAG, "cancel: Canceling AcceptThread.");
            try {
                myServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed. " + e.getMessage() );
            }
        }
    }

    /**
     * CONNECT THREAD CLASS
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket mySocket;

        /**
         * ConnectThread constructor
         * @param device
         * @param uuid
         */
        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started.");
            myDevice = device;
            deviceUUID = uuid;
        }

        /**
         * Creates a socket, connects socket, and connects socket to device
         */
        public void run() {
            BluetoothSocket tmp = null;
            Log.i(TAG, "RUN myConnectThread ");

            // Get a BluetoothSocket for a connection with the given BluetoothDevice
            try {
                Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID: " + MY_UUID_INSECURE);
                tmp = myDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + e.getMessage());
            }

            mySocket = tmp;

            // Always cancel discovery because it will slow down a connection
            myBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a successful connection of an exception
                mySocket.connect();

                Log.d(TAG, "run: ConnectThread connected.");
            } catch (IOException e) {
                // Close the socket
                try {
                    mySocket.close();
                    Log.d(TAG, "run: Closed Socket.");
                } catch (IOException e1) {
                    Log.e(TAG, "myConnectThread: run: Unable to close connection in socket " + e1.getMessage());
                }
                Log.d(TAG, "run: ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE);
            }

            connected(mySocket, myDevice);
        }

        /**
         * Cancel AcceptThread object
         */
        public void cancel() {
            Log.d(TAG, "cancel: Closing Client Socket.");
            try {
                mySocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of mySocket in ConnectThread failed. " + e.getMessage() );
            }
        }

        /**
         * Start the chat service. Specifically start AcceptThread to begin a
         * session in listening (server) mode. Called by the Activity onResume()
         */
        public synchronized void start() {
            Log.d(TAG, "start");

            // Cancel any thread attempting to make a connection
            if (myConnectThread != null) {
                myConnectThread.cancel();
                myConnectThread = null;
            }

            if (myInsecureAcceptThread == null) {
                myInsecureAcceptThread = new AcceptThread();
                myInsecureAcceptThread.start();
            }
        }

        /**
         * AcceptThread starts and sits waiting for a connection.
         * Then ConnectThread starts and attempts to make a connection with the other devices AcceptThread.
         * @param device
         * @param uuid
         */
        public void startClient(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "startClient: Started.");

            // init progress dialog
            myProgressDialog = ProgressDialog.show(myContext, "Connecting Bluetooth", "Please Wait...", true);

            myConnectThread = new ConnectThread(device, uuid);
            myConnectThread.start();
        }
    }

    /**
     * CONNECTED THREAD CLASS
     * Finally, ConnectedThread is responsible for maintaining the BTConnection, Sending the data,
     * and receiving incoming data through input/output streams respectively.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mySocket;
        private final InputStream myInStream;
        private final OutputStream myOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Starting.");

            mySocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // dismiss the progress dialog when connection is established
            myProgressDialog.dismiss();

            try {
                tmpIn = mySocket.getInputStream();
                tmpOut = mySocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            myInStream = tmpIn;
            myOutStream = tmpOut;
        }

        /**
         * Receives incoming data through myInStream byte-by-byte
         */
        public void run() {
            byte[] buffer = new byte[1024];     // buffer store for the instream

            int bytes;  // bytes returned from read()

            // Keep listening to the InputStream untl an exception occurs
            while (true) {
                // Read from the InputStream
                try {
                    bytes = myInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "InputStream: " + incomingMessage);
                } catch (IOException e) {
                    Log.e(TAG, "write: Error writing to input stream. " + e.getMessage());
                    break;
                }
            }
        }

        /**
         * Sends data to myOutStream byte-by-byte
         * @param bytes
         */
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to Output Stream: " + text);
            try {
                myOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing to output stream. " + e.getMessage());
            }
        }

        /**
         * Cancel ConnectedThread object
         * Call this from main activity to shutdown the connection
         */
        public void cancel() {
            Log.d(TAG, "cancel: Closing Bluetooth Connection.");
            try {
                mySocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of mySocket in ConnectedThread failed. " + e.getMessage() );
            }
        }
    }

    /**
     * Manage connection to perform OutputStream transmissions and grab InputStream transmissions
     * @param mySocket
     * @param myDevice
     */
    private void connected(BluetoothSocket mySocket, BluetoothDevice myDevice) {
        Log.d(TAG, "connected: Starting.");

        // Start the thread to manage the connection and perform transmissions
        myConnectedThread = new ConnectedThread(mySocket);
        myConnectedThread.start();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;

        // Synchronize a copy of the ConnectedThread
        Log.d(TAG, "write: Write Called.");
        myConnectedThread.write(out);
    }
}