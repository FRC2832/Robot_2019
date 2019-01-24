package org.livoniawarriors.Robot2019;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Collectors;

public class Lidar {

    private Socket client;
    private Transform transform;
    private BufferedReader input;
    private BufferedWriter output;

    private static final int MAX_BUFFER_READS = 100;

    public Lidar() {
        transform = new Transform();
        try {
            client = new Socket("169.254.25.245", 1234); // try http://frcvision.local instead of static
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            String line;
            int reads = 0;
            while((line = input.readLine()) != null) {
                if(line.equals("transform"))
                    updateTransform(input);
                else if(line.startsWith("error"))
                    System.err.println(line);
                reads++;
                if(reads >= MAX_BUFFER_READS)
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        try {
            output.write(message);
            output.newLine();
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTransform(BufferedReader input) throws IOException {
        transform.x = Integer.parseInt(input.readLine());
        transform.y = Integer.parseInt(input.readLine());
        transform.rotation = Integer.parseInt(input.readLine());
    }

    public Transform getTransform() {
        return transform;
    }

    public class Transform {
        private float x, y, rotation;
        private Transform(float x, float y, float rotation) {
            this.x = x; this.y = y; this.rotation = rotation;
        }

        private Transform() {
            this(0, 0, 0);
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }


        public float getRotation() {
            return rotation;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ", " + rotation + ")"; // StringBuilder usage is boilerplate code in this instance
        }
    }

    public void dispose() throws IOException {
        if(client != null)
            client.close();
    }
}
