import libjevois as jevois
import cv2
import numpy as np

## Test
#
# Add some description of your module here.
#
# @author Test
# 
# @videomapping YUYV 640 390 30 YUYV 320 240 30 Test JevoisTesting
# @email Test
# @address 123 first street, Los Angeles CA 90012, USA
# @copyright Copyright (C) 2018 by Test
# @mainurl TestTest
# @supporturl TestTest
# @otherurl TestTest
# @license Test
# @distribution Unrestricted
# @restrictions None
# @ingroup modules
class JevoisTestig:
    # ###################################################################################################
    ## Constructor
    def __init__(self):
        # Instantiate a JeVois Timer to measure our processing framerate:
        self.timer = jevois.Timer("processing timer", 100, jevois.LOG_INFO)
        
    # ###################################################################################################
    ## Process function with USB output
    def process(self, inframe, outframe):
        # Get the next camera image (may block until it is captured) and here convert it to OpenCV BGR. If you need a
        # grayscale image, just use getCvGRAY() instead of getCvBGR(). Also supported are getCvRGB() and getCvRGBA():
        inimg = inframe.getCvBGR()

        #blur image with radius of 8 pixels
        ksize = int(2 * round(8) + 1)
        blur_img= cv2.blur(inimg, (ksize, ksize))
        #hsv threshold
        temp1 = cv2.cvtColor(blur_img, cv2.COLOR_BGR2HSV)
        hsvout = cv2.inRange(temp1, (84, 0, 100),  (101, 87, 255))
        #finds contours
        contours, hierarchy =cv2.findContours(hsvout, mode=cv2.RETR_LIST, method=cv2.CHAIN_APPROX_SIMPLE)
        #filter contours
        output = []
        dist = 0
        ang = 0
        for contour in contours:
           #x,y is top left corner, w,h is width/height
           x,y,w,h = cv2.boundingRect(contour)
           if (w < 100 or w > 1000):
                continue
           if (h < 90 or h > 1000):
                continue
           #draw bounds
           cv2.rectangle(inimg, (x,y), (x+w,y+h), (0,255,0),2)
           #contour matches, report distance and angle
           dist = w
           ang = x
        jevois.sendSerial("{{{},{},{},{},{},{},{},{}}}\n".format(60, "T", 60, 20, 22, 2000, 20000000, 10))
        cv2.putText(inimg, str(dist) + " " + str(ang), (20,20), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255,255,255))
        outframe.sendCv(inimg)