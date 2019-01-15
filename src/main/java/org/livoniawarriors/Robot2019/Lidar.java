package org.livoniawarriors.Robot2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Collectors;

public class Lidar {

    private Socket client;
    private Transform transform;

    public Lidar() {
        transform = new Transform();
        try {
            client = new Socket("169.254.25.245", 1234);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            InputStream stream = client.getInputStream();
            BufferedReader input = new BufferedReader(new InputStreamReader(stream));
            String line;
            while((line = input.readLine()) != null) {
                if(line.equals("A"))
                    updateTransform(input);
            }
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
            StringBuffer buffer = new StringBuffer();
            buffer.append(x);
            buffer.append(",");
            buffer.append(y);
            buffer.append(",");
            buffer.append(rotation);
            return buffer.toString();
        }
    }
}
