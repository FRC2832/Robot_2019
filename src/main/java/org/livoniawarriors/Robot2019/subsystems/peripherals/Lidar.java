package org.livoniawarriors.Robot2019.subsystems.peripherals;

import edu.wpi.first.wpilibj.Notifier;
import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.Robot;

import java.io.*;
import java.net.Socket;

public class Lidar {

    private static final int MAX_READ = 100;
    private static final float CONNECT_ATTEMPT_DELAY = 1;
    private static final String PI_ADDRESS = "http://frcvision.local"; // Fix MEEEE

    private Socket client;
    private Transform transform;
    private BufferedReader input;
    private BufferedWriter output;
    private Notifier connectNotifier, updateNotifier;

    public Lidar() {
        transform = new Transform();
        connectNotifier = new Notifier(this::tryConnect);
        connectNotifier.startPeriodic(CONNECT_ATTEMPT_DELAY);
        updateNotifier = new Notifier(this::update);
        updateNotifier.startPeriodic(0.05);
    }

    private void tryConnect() {
        try {
            client = new Socket(PI_ADDRESS, 1234); // try http://frcvision.local instead of static
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (IOException e) {
            //Robot.logger.error("Couldn't connect to LIDAR");
        }
    }

    void csv(ICsvLogger logger) {
        logger.log("Lidar On", client != null);
    }

    private void update() {
        if(client != null) {
            try {
                String line;
                int read = 0;
                while ((line = input.readLine()) != null) {
                    if (line.equals("transform"))
                        updateTransform(input);
                    else if (line.startsWith("error"))
                        System.err.println(line);
                    else if (line.equals("map"))
                        System.out.println(input.readLine());
                    read++;
                    if (read >= MAX_READ)
                        break;
                }
            } catch (IOException e) {
                Robot.logger.error("Lidar", e);
            }
        }
    }

    private void sendMessage(String message) {
        if(client != null) {
            try {
                output.write(message);
                output.newLine();
                output.flush();
            } catch (IOException e) {
                Robot.logger.error("Lidar", e);
            }
        }
    }

    private void updateTransform(BufferedReader input) throws IOException {
        transform.x = Float.parseFloat(input.readLine());
        transform.y = Float.parseFloat(input.readLine());
        transform.rotation = Float.parseFloat(input.readLine());
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
            return "(" + x + ", " + y + ", " + rotation + ")"; // StringBuilder usage is boilerplate code in this instance, at least in java 8
        }
    }

    public void dispose() throws IOException {
        if(client != null)
            client.close();
        connectNotifier.close();
    }
}
