# region 라이브러리 설정
import tkinter
import tkinter as tk
from tkinter import *
import sys
import multiprocessing
import RPi.GPIO as GPIO
import time
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
motor1 = 20
motor2 = 21
motor3 = 12
motor4 = 16
motor5 = 7
motor6 = 8
GPIO.setwarnings(False)
GPIO.setup(motor1, GPIO.OUT, initial=GPIO.LOW)
GPIO.setup(motor2, GPIO.OUT, initial=GPIO.LOW)
GPIO.setup(motor3, GPIO.OUT, initial=GPIO.LOW)
GPIO.setup(motor4, GPIO.OUT, initial=GPIO.LOW)
GPIO.setup(motor5, GPIO.OUT, initial=GPIO.LOW)
GPIO.setup(motor6, GPIO.OUT, initial=GPIO.LOW)
p1 = GPIO.PWM(motor1, 10)
p2 = GPIO.PWM(motor2, 10)
p3 = GPIO.PWM(motor3, 10)
p4 = GPIO.PWM(motor4, 10)
p5 = GPIO.PWM(motor5, 10)
p6 = GPIO.PWM(motor6, 10)
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


# region 센서 세팅 및 초기 오차값
# 텀블러, 원료통1~6세팅
hx_main = HX711(26, 19)
hx_1 = HX711(13, 6)
hx_2 = HX711(5, 11)
hx_3 = HX711(9, 10)
hx_4 = HX711(22, 27)
hx_5 = HX711(17, 4)
hx_6 = HX711(3, 2)
hx_main.set_reading_format("MSB", "MSB")
hx_main.set_reference_unit(399)
hx_1.set_reading_format("MSB", "MSB")
hx_1.set_reference_unit(403)
hx_2.set_reading_format("MSB", "MSB")
hx_2.set_reference_unit(398)
hx_3.set_reading_format("MSB", "MSB")
hx_3.set_reference_unit(423)
hx_4.set_reading_format("MSB", "MSB")
hx_4.set_reference_unit(412)
hx_5.set_reading_format("MSB", "MSB")
hx_5.set_reference_unit(406)
hx_6.set_reading_format("MSB", "MSB")
hx_6.set_reference_unit(413)


# endregion

class Weightsensor:
    def __init__(self):
        self.count = 0

    def weight_cup(self):
        hx_main.reset()
        hx_main.tare()
        while TRUE:
            # 무게 측정
            val_cup = round(hx_main.get_weight(5))
            hx_main.power_down()
            hx_main.power_up()
            time.sleep(0.5)
            print("컵 센서 측정값: %f" % val_cup)

    def weight_powder(self):
        # region reset&tare
        hx_1.reset()
        hx_2.reset()
        hx_3.reset()
        hx_4.reset()
        hx_5.reset()
        hx_6.reset()
        hx_1.tare()
        hx_2.tare()
        hx_3.tare()
        hx_4.tare()
        hx_5.tare()
        hx_6.tare()
        # endregion
        while TRUE:
            # region getweight
            val_1 = round(hx_1.get_weight(5), 1)
            val_2 = round(hx_2.get_weight(5), 1)
            val_3 = round(hx_3.get_weight(5), 1)
            val_4 = round(hx_4.get_weight(5), 1)
            val_5 = round(hx_5.get_weight(5), 1)
            val_6 = round(hx_6.get_weight(5), 1)
            # endregion
            # region power_down_up
            hx_1.power_down()
            hx_2.power_down()
            hx_3.power_down()
            hx_4.power_down()
            hx_5.power_down()
            hx_6.power_down()

            hx_1.power_up()
            hx_2.power_up()
            hx_3.power_up()
            hx_4.power_up()
            hx_5.power_up()
            hx_6.power_up()
            # endregion
            time.sleep(0.1)
            # region print_value
            print("원료통1 측정값: %f" % val_1)
            print("원료통2 측정값: %f" % val_2)
            print("원료통3 측정값: %f" % val_3)
            print("원료통4 측정값: %f" % val_4)
            print("원료통5 측정값: %f" % val_5)
            print("원료통6 측정값: %f" % val_6)
            # endregion


# endregion
# region 창설정
win = tk.Tk()
win.title("Hy-personal Booster Vender")
win.geometry("1280x800")
win.resizable(True, True)


# endregion

# region 화면전환함수
def change1to2():
    label_subtext.configure(text='인식시켰다면 화면을 클릭해 주세요')
    label_maintext.configure(text='카메라에 qr코드를 인식시켜 주세요')
    label_image.configure(image=image2, command=change2to3)


global moctrl


def change2to3():
    global moctrl
    ap = argparse.ArgumentParser()
    ap.add_argument("-o", "--output", type=str, default="barcodes.csv",
                    help="path to output CSV file containing barcodes")
    args = vars(ap.parse_args())
    print("[INFO] starting video stream...")
    vs = VideoStream(src=0).start()  # USB 웹캠 카메라 사용시
    time.sleep(2.0)
    csv = open(args["output"], "w")
    found = set()

    barcodeData = 0
    while True:

        frame = vs.read()
        frame = imutils.resize(frame, width=400)

        barcodes = pyzbar.decode(frame)

        for barcode in barcodes:
            (x, y, w, h) = barcode.rect
            cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 0, 255), 2)
            barcodeData = barcode.data.decode("utf-8")
            barcodeType = barcode.type
            text = "{} ({})".format(barcodeData, barcodeType)
            cv2.putText(frame, text, (x, y - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)
            if barcodeData not in found:
                csv.write("{},{}\n".format(datetime.datetime.now(),
                                           barcodeData))
                csv.flush()
                found.add(barcodeData)

        cv2.imshow("Barcode Scanner", frame)
        key = cv2.waitKey(1) & 0xFF

        if barcodeData != 0:
            time.sleep(1)
            break

    print("[INFO] cleaning up...")
    print(barcodeData)
    usernumber = barcodeData[0:3]
    moctrl = int(barcodeData[3:5])
    gram = barcodeData[5:]

    csv.close()
    cv2.destroyAllWindows()
    vs.stop()
    label_subtext.configure(text='서버와 연동중입니다')
    label_maintext.configure(text='서버와 연동중입니다')
    label_image.configure(image=image3, command=change3to4)
    label_yesbutton.pack_forget()
    label_nobutton.pack_forget()


def change3to4():
    label_subtext.configure(text='버튼을 클릭해주세요')
    label_maintext.configure(text='선택하신 프로틴이 맞는지 확인해 주세요')
    label_image.configure(image=image4, command=change4to5)
    label_yesbutton.pack(side="left")
    label_nobutton.pack(side="right")


def change4to5():
    label_subtext.configure(text='기다려주세요')
    label_maintext.configure(text='투하중입니다.')
    label_image.configure(image=image1, command=actmotor)
    label_yesbutton.pack_forget()
    label_nobutton.pack_forget()


def change5to6():
    label_subtext.configure(text='감사합니다')
    label_maintext.configure(text='투하가 완료되었습니다')
    label_image.configure(command=resetscreen)


def resetscreen():
    label_maintext.configure(text="투입구에 텀블러를 올려주세요")
    label_image.configure(image=image1, command=change1to2)
    label_subtext.configure(text="투입하셨다면 클릭해주세요")


def whichmotor(motorcontrol):
    if motorcontrol == 1:
        p1.start(0)
        p1.ChangeDutyCycle(50)
        time.sleep(1)
        p1.stop()
    elif motorcontrol == 2:
        p2.start(0)
        p2.ChangeDutyCycle(50)
        time.sleep(1)
        p2.stop()
    elif motorcontrol == 3:
        p3.start(0)
        p3.ChangeDutyCycle(50)
        time.sleep(1)
        p3.stop()
    elif motorcontrol == 4:
        p4.start(0)
        p4.ChangeDutyCycle(50)
        time.sleep(1)
        p4.stop()
    elif motorcontrol == 5:
        p5.start(0)
        p5.ChangeDutyCycle(50)
        time.sleep(1)
        p5.stop()
    elif motorcontrol == 6:
        p6.start(0)
        p6.ChangeDutyCycle(50)
        time.sleep(1)
        p6.stop()


def actmotor():
    whichmotor(moctrl)
    change5to6()


# endregion
# region 이미지파일 및 초기설정
image1 = tkinter.PhotoImage(file="tumblr.png")
image2 = tkinter.PhotoImage(file="qrcode.png")
image3 = tkinter.PhotoImage(file="server.png")
image4 = tkinter.PhotoImage(file="protein.png")
image5 = tkinter.PhotoImage(file="tumblrwithscoop.png")

# 초기화면
label_maintext = tkinter.Label(win, text="투입구에 텀블러를 올려주세요", font=("나눔고딕", 40))
label_image = tkinter.Button(win, image=image1)
label_subtext = tkinter.Label(win, text="투입하셨다면 클릭해주세요", font=("나눔고딕", 40))
label_image = tkinter.Button(win, image=image1, command=change1to2)

label_maintext.pack(expand=1, anchor=CENTER)
label_image.pack(expand=1, anchor=CENTER)
label_subtext.pack(expand=1, anchor=CENTER)
label_yesbutton = tkinter.Button(win, text="YES", command=change4to5, width=10, height=5)
label_nobutton = tkinter.Button(win, text="NO", command=change2to3, width=10, height=5)
# endregion
# 실행
if __name__ == "__main__":
    # 무게센서 클래스 지정
    wei_cup = Weightsensor()
    #    wei_powder = Weightsensor()
    mp_gui = multiprocessing.Process(target=win.mainloop)
    mp_wei_cup = multiprocessing.Process(target=wei_cup.weight_cup)
    #    mp_wei_powder = multiprocessing.Process(target=wei_powder.weight_powder)
    mp_gui.start()
    mp_wei_cup.start()
    #    mp_wei_powder.start()

    mp_gui.join()
    mp_wei_cup.join()
#    mp_wei_powder.join()

