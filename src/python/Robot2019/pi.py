#!/usr/bin/env python3
#----------------------------------------------------------------------------
# Copyright (c) 2018 FIRST. All Rights Reserved.
# Open Source Software - may be modified and shared by FRC teams. The code
# must be accompanied by the FIRST BSD license file in the root directory of
# the project.
#----------------------------------------------------------------------------

import json
import time
import sys
from breezyslam.algorithms import RMHC_SLAM
from breezyslam.sensors import RPLidarA1 as LaserModel
from rplidar import RPLidar as Lidar
import socket

LIDAR_DEVICE = '/dev/ttyUSB0'
MIN_SAMPLES = 200 # The number of samples that are processed of the RPLidar's aprox. 250
MAP_SIZE_PIXELS = 500
MAP_SIZE_METERS = 10

configFile = "/boot/frc.json"

if __name__ == "__main__":
    # Connect to Lidar
    lidar = Lidar(LIDAR_DEVICE)

    # Create an RMHC SLAM object with a laser model and optional robot model
    slam = RMHC_SLAM(LaserModel(), MAP_SIZE_PIXELS, MAP_SIZE_METERS)

    # Initialize an empty trajectory
    trajectory = []

    # Initialize empty map
    mapbytes = bytearray(MAP_SIZE_PIXELS * MAP_SIZE_PIXELS)

    # Create an iterator to collect scan data from the RPLidar
    iterator = lidar.iter_scans()

    # We will use these to store previous scan in case current scan is inadequate
    previous_distances = None
    previous_angles = None

    # First scan is crap, so ignore it
    next(iterator)

    port = 1234
    s = socket.socket()
    s.bind(('', port))
    s.listen(1)

    while True:
        conn, address = s.accept()
        with conn:
            while True:
                data = conn.recv(1024)
                if not data:
                    break
                # Shut down the lidar connection
                lidar.stop()
                lidar.disconnect()
                # Extract (quality, angle, distance) triples from current scan
                items = [item for item in next(iterator)]

                # Extract distances and angles from triples
                distances = [item[2] for item in items]
                angles = [item[1] for item in items]

                # Update SLAM with current Lidar scan and scan angles if adequate
                if len(distances) > MIN_SAMPLES:
                    slam.update(distances, scan_angles_degrees=angles)
                    previous_distances = distances.copy()
                    previous_angles = angles.copy()
                # If not adequate, use previous
                elif previous_distances is not None:
                    slam.update(previous_distances, scan_angles_degrees=previous_angles)

                # Get current robot position
                x, y, theta = slam.getpos()

                # Get current map bytes as grayscale
                slam.getmap(mapbytes)

                data = conn.recv(1024).decode()

                conn.send('map\n')
                conn.send(mapbytes)
                conn.send('\n')
                conn.send('transform\n' + x + '\n' + y + '\n' + theta + '\n')
