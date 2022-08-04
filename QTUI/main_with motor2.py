# region 라이브러리
import sys
from PyQt5.QtWidgets import *
from PyQt5 import uic
from PyQt5.QtGui import *
from PyQt5.QtWidgets import QApplication, QWidget, QPushButton, QVBoxLayout
from PyQt5.QtCore import *
import os
import mouse
import multiprocessing
import RPi.GPIO as GPIO
import time
import threading
from imutils.video import VideoStream
from pyzbar import pyzbar
import argparse
import datetime
import imutils
import time
import cv2
# endregion
# region 모터설정
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
motor1 = 20
motor2 = 21
GPIO.setup(motor1, GPIO.OUT, initial=GPIO.LOW)
GPIO.setup(motor2, GPIO.OUT, initial=GPIO.LOW)
p1 = GPIO.PWM(motor1, 10)
p2 = GPIO.PWM(motor2, 10)
# endregion
# region 무게센서 설정
EMULATE_HX711 = False
referenceUnit = 1
if not EMULATE_HX711:
    import RPi.GPIO as GPIO
    from hx711 import HX711
else:
    from emulated_hx711 import HX711
def cleanAndExit():
    print("Cleaning...")
    if not EMULATE_HX711:
        GPIO.cleanup()
    print("Bye!")
    sys.exit()

#무게 센서 세팅 및 초기 오차값
class Weightsensor(QThread):
    def run(self):
        self.hx_main = HX711(26, 19)
        self.hx_main.set_reading_format("MSB", "MSB")
        self.hx_main.set_reference_unit(398)
        self.hx_main.reset()
        self.hx_main.tare()
        while True:
            # 무게 측정
            self.val_cup = round(self.hx_main.get_weight(5))
            self.hx_main.power_down()
            self.hx_main.power_up()
            self.sleep(1)
            print("컵 센서 측정값: %f" % self.val_cup)
# endregion
# region QR코드 관련 변수 설정
global moctrl
global usernumber
global gram
global motime

# endregion
# region DC모터 제어식
def whichmotor(motorcontrol,motortime):
    if motorcontrol == 1:
        p1.start(0)
        p1.ChangeDutyCycle(50)
        time.sleep(motortime)
        p1.stop()
    elif motorcontrol == 2:
        p2.start(0)
        p2.ChangeDutyCycle(50)
        time.sleep(motortime)
        p2.stop()
# endregion
#region GUI 설정
form_class = uic.loadUiType("uis/mainpage.ui")[0]
global page_number
# 화면을 띄우는데 사용되는 Class 선언
class WindowClass(QMainWindow, form_class):
    def __init__(self):
        super().__init__()
        self.setupUi(self)
        self.stackedWidget: QStackedWidget  # class type 지정해주기
        self.stackedWidget.setCurrentIndex(0)
        self.page_number = 0
        self.yesbtn.clicked.connect(self.yesbuttonFunction)
        self.nobtn.clicked.connect(self.nobuttonFunction)
        self.weight = Weightsensor()
        self.weight.start()

        #region 이미지파일 설정
        self.qPixmapVar = QPixmap()
        self.qPixmapVar.load("images/test.svg")
        self.picture.setPixmap(self.qPixmapVar)
        self.qPixmapVar1 = QPixmap()
        self.qPixmapVar1.load("images/cup.svg")
        self.page_1_image.setPixmap(self.qPixmapVar1)
        self.qPixmapVar2 = QPixmap()
        self.qPixmapVar2.load("images/qr.svg")
        self.page_2_image.setPixmap(self.qPixmapVar2)
        self.qPixmapVar3 = QPixmap()
        self.qPixmapVar3.load("images/server.svg")
        self.page_3_image.setPixmap(self.qPixmapVar3)
        self.qPixmapVar4 = QPixmap()
        self.qPixmapVar4.load("images/protein.png")
        self.qPixmapVar4.scaledToWidth(400)
        self.page_4_image.setPixmap(self.qPixmapVar4)

        self.qPixmapVar5 = QPixmap()
        self.qPixmapVar5.load("images/scoop.svg")
        self.page_5_image.setPixmap(self.qPixmapVar5)
        self.qPixmapVar6 = QPixmap()
        self.qPixmapVar6.load("images/done.svg")
        self.page_6_image.setPixmap(self.qPixmapVar6)
        self.yesbtn.setStyleSheet('border-image:url("images/ybtn.png");border:0px;')
        self.nobtn.setStyleSheet('border-image:url("images/nbtn.png");border:0px;')
        #endregion
    def mousePressEvent(self, e):
        if self.page_number == 0:
            self.stackedWidget.setCurrentIndex(1)
            self.page_number += 1
        elif self.page_number == 1:
            self.stackedWidget.setCurrentIndex(2)
            self.page_number += 1
            global moctrl
            global usernumber
            global gram
            global motime
            self.ap = argparse.ArgumentParser()
            self.ap.add_argument("-o", "--output", type=str, default="barcodes.csv",
                            help="path to output CSV file containing barcodes")
            self.args = vars(self.ap.parse_args())
            print("[INFO] starting video stream...")
            self.vs = VideoStream(src=0).start()  # USB 웹캠 카메라 사용시
            time.sleep(2)
            self.csv = open(self.args["output"], "w")
            self.found = set()
            self.barcodeData = 0
            while True:
                self.frame = self.vs.read()
                self.frame = imutils.resize(self.frame, width=400)
                self.barcodes = pyzbar.decode(self.frame)
                for barcode in self.barcodes:
                    (x, y, w, h) = barcode.rect
                    self.cv2.rectangle(self.frame, (x, y), (x + w, y + h), (0, 0, 255), 2)
                    barcodeData = barcode.data.decode("utf-8")
                    self.barcodeType = barcode.type
                    text = "{} ({})".format(barcodeData, self.barcodeType)
                    cv2.putText(self.frame, text, (x, y - 10),
                                cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)
                    if barcodeData not in self.found:
                        self.csv.write("{},{}\n".format(datetime.datetime.now(),
                                                   barcodeData))
                        self.csv.flush()
                        self.found.add(barcodeData)
                self.cv2.imshow("Barcode Scanner", self.frame)
                key = self.cv2.waitKey(1) & 0xFF
                if barcodeData != 0:
                    self.sleep(1)
                    break

            print("[INFO] cleaning up...")
            print(barcodeData)
            usernumber = int(barcodeData[0:3])
            moctrl = int(barcodeData[3:5])
            gram = int(barcodeData[5:])
            motime = gram / 10
            self.csv.close()
            self.cv2.destroyAllWindows()
            self.vs.stop()
        elif self.page_number == 2:
            self.stackedWidget.setCurrentIndex(3)
            self.page_number += 1
        elif self.page_number == 3:
            self.stackedWidget.setCurrentIndex(4)
            self.page_number += 1
        elif self.page_number == 4:
            pass
        elif self.page_number == 5:
            whichmotor(1,1)
            self.stackedWidget.setCurrentIndex(6)
            self.page_number += 1
        elif self.page_number == 6:
            self.stackedWidget.setCurrentIndex(0)
            self.page_number = 0
    def yesbuttonFunction(self):
        self.stackedWidget.setCurrentIndex(5)
        self.page_number = 5

    def nobuttonFunction(self):
        self.stackedWidget.setCurrentIndex(3)
        self.page_number = 3
#endregion

if __name__ == "__main__":
    app = QApplication(sys.argv)
    myWindow = WindowClass()
    myWindow.show()
    app.exec_()

