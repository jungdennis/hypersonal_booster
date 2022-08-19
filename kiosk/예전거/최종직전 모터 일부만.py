# region 라이브러리
import tkinter
import tkinter as tk
from tkinter import *
import time
from PIL import Image
from tkinter import ttk
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
from threading import Thread
# endregion
# region 전역변수 선언
global first_weight
global second_weight
global val_cup
global val_1
global moctrl
global usernumber
global gram
global count
count = 0
# endregion
# region 모터설정
# region 모터 핀 설정
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
motor1 = 20
GPIO.setup(motor1, GPIO.OUT, initial=GPIO.LOW)
p1 = GPIO.PWM(motor1, 10)
motor2 = 21
GPIO.setup(motor2, GPIO.OUT, initial=GPIO.LOW)
p2 = GPIO.PWM(motor2, 10)
# endregion
# region 모터제어함수(번호,시간)
def whichmotor(motorcontrol,getgram):
    global val_cup
    global first_weight
    global second_weight
    second_weight = val_cup
    if motorcontrol % 2 == 1:
        while (second_weight - first_weight) < getgram:
                p1.start(0)
                p1.ChangeDutyCycle(100)
                time.sleep(3)
                p1.stop()
                time.sleep(3)
                second_weight = val_cup
                print("second: %d" %second_weight)
    elif motorcontrol % 2 == 0:
        while (second_weight - first_weight) < getgram:
                p2.start(0)
                p2.ChangeDutyCycle(100)
                time.sleep(3)
                p2.stop()
                time.sleep(3)
                second_weight = val_cup
                print("second: %d" % second_weight)
# endregion
# endregion
# region 무게센서 설정
# region 라이브러리 초기설정
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
# endregion
# region 텀블러, 원료통1~6세팅
hx_main = HX711(26, 19)
hx_main.set_reading_format("MSB", "MSB")
hx_main.set_reference_unit(399)

hx_1 = HX711(13, 6)
hx_1.set_reading_format("MSB", "MSB")
hx_1.set_reference_unit(403)
# endregion
# 무게센서 함수
class Weightsensor():
    def __init__(self):
        global val_cup
        global val_1
    def weight_cup(self):
        hx_main.reset()
        hx_main.tare()
        while True:
            # 무게 측정
            global val_cup
            val_cup = round(hx_main.get_weight(5))
            hx_main.power_down()
            hx_main.power_up()
            time.sleep(0.5)
            print("컵 센서 측정값: %f" % val_cup)
    def weight_powder(self):
        # region reset&tare
        hx_1.reset()
        hx_1.tare()
        # endregion
        while True:
            # region getweight
            val_1 = round(hx_1.get_weight(5), 1)
            # endregion
            # region power_down_up
            hx_1.power_down()
            hx_1.power_up()
            # endregion
            time.sleep(10)
            print("원료통1 측정값: %f" % val_1)
# endregion

# region 창설정
win = tk.Tk()
win.title("Hy-personal Booster Vender")
win.geometry("1280x800")
win.resizable(True, True)
win.configure(bg='#FBCA53')
# endregion
# region 화면전환함수
def clickMouse(event):
    global count
    global moctrl
    global usernumber
    global gram
    global val_cup
    global first_weight
    global second_weight
    # 메인페이지에서 넘어가기
    if count == 0:
        maintext.pack_forget()
        logo_image.pack_forget()
        subtext.pack_forget()
        label_maintext.configure(text="투입구에 텀블러를 올려주세요")
        label_subtext.configure(text="투입하셨다면 클릭해주세요")
        label_image.configure(image=image1)
        label_maintext.pack(side='top',ipady=30)
        label_image.pack(side='top')
        label_subtext.pack(side='top',ipady=30)
        count += 1
        print(count)
    # 텀블러 인식 페이지에서 넘어가기
    elif count == 1:
        first_weight = val_cup
        print("first: %d" % first_weight)
        label_maintext.configure(text='카메라에 qr코드를 인식시켜 주세요')
        label_subtext.configure(text='인식시켰다면 화면을 클릭해 주세요')
        label_image.configure(image=image2)
        count += 1
        print(count)
        win.after(500,next)
    # qr코드 인식페이지에서 넘어가기(next함수로 자동진행)
    elif count == 2:
        pass
    # 서버 연동페이지에서 넘어가기(next함수로 자동진행)
    elif count == 3:
        pass
    # 프로틴 정보 확인,버튼으로만 넘어가기 넘어가기
    elif count == 4:
        pass
    # 투하 페이지에서 넘어가기
    elif count == 5:
        whichmotor(moctrl, gram)
        label_subtext.configure(text='감사합니다')
        label_maintext.configure(text='투하가 완료되었습니다')
        label_image.configure(image=image6)
        count += 1
        print(count)
        win.after(3000,next)
    # 투하 완료에서 다시 메인페이지로 넘어가기
    elif count == 6:
        maintext.pack()
        logo_image.pack()
        subtext.pack()
        label_maintext.pack_forget()
        label_image.pack_forget()
        label_subtext.pack_forget()
        count = 0
        print(count)
def next():
    global count
    global moctrl
    global usernumber
    global gram
    if count == 2:
        ap = argparse.ArgumentParser()
        ap.add_argument("-o", "--output", type=str, default="barcodes.csv",
                        help="path to output CSV file containing barcodes")
        args = vars(ap.parse_args())
        print("[INFO] starting video stream...")
        vs = VideoStream(src=0).start()  # USB 웹캠 카메라 사용시
        time.sleep(0.5)
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
        usernumber = int(barcodeData[0:3])
        moctrl = int(barcodeData[3:5])
        gram = int(barcodeData[5:])
        print("회원번호 : %d" %usernumber)
        print("선택원료 : %d번" %moctrl)
        print("용량 : %dg" % gram)
        csv.close()
        cv2.destroyAllWindows()
        vs.stop()
        # endregion
        label_subtext.configure(text='서버와 연동중입니다')
        label_maintext.configure(text='서버와 연동중입니다')
        label_image.configure(image=image3)
        label_yesbutton.place_forget()
        label_nobutton.place_forget()
        count += 1
        win.after(1000,next)
        print(count)
    elif count == 3:
        label_maintext.configure(text='선택하신 프로틴이 맞는지 확인해 주세요')
        label_subtext.pack_forget()
        # region qr코드 분해후 상세 설명 변경란
        if moctrl == 1:
            label_powimage.configure(image=image4_1)
            label_powtext_1.configure(text="\n제품 : WPI\n\n용량 : %dg\n\n상세성분: 1회분(30g)당 단백질 20g 탄수화물 10g" %gram)
        elif moctrl == 2:
            label_powimage.configure(image=image4_2)
            label_powtext_1.configure(text="\n제품 : WPC\n\n용량 : %dg\n\n상세성분: 1회분(30g)당 단백질 20g 탄수화물 10g" %gram)
        elif moctrl == 3:
            label_powimage.configure(image=image4_3)
            label_powtext_1.configure(text="제품 : CASEIN\n용량 : %dg\n상세성분: 1회분(30g)당 단백질 20g 탄수화물 10g" %gram)
        elif moctrl == 4:
            label_powtext_1.configure(text="제품 : VEG\n용량 : %dg\n상세성분: 1회분(30g)당 단백질 20g 탄수화물 10g" %gram)
            label_powimage.configure(image=image4_4)
        elif moctrl == 5:
            label_powimage.configure(image=image4_5)
            label_powtext_1.configure(text="제품 : GAINER\n용량 : %dg\n상세성분: 1회분(30g)당 단백질 20g 탄수화물 10g" %gram)
        elif moctrl == 6:
            label_powimage.configure(image=image4_6)
            label_powtext_1.configure(text="제품 : BCAA\n용량 : %dg\n상세성분: 1회분(30g)당 단백질 20g 탄수화물 10g" %gram)
        # endregion
        label_image.pack_forget()
        label_powimage.pack(side="left",ipadx=100,anchor='nw')
        label_powtext_1.pack(side="left")
#        label_powtext_2.pack(side="left",anchor='nw')
        label_yesbutton.place(x=475,y=550)
        label_nobutton.place(x=730,y=550)
        count += 1
        print(count)        
    elif count == 6:
        maintext.pack()
        logo_image.pack()
        subtext.pack()
        label_maintext.pack_forget()
        label_image.pack_forget()
        label_subtext.pack_forget()
        count = 0
# yes no 버튼
def yesbtn():
    global count
    count = 5
    label_subtext.configure(text='기다려주세요')
    label_maintext.configure(text='버튼을 눌러 투하를 시작해 주세요')
    label_image.configure(image=image5)
    label_powtext_1.pack_forget()
    label_powtext_2.pack_forget()
    label_powimage.pack_forget()
    label_yesbutton.place_forget()
    label_nobutton.place_forget()
    label_image.pack()
    label_subtext.pack()
def nobtn():
    global count
    label_subtext.configure(text='서버와 연동중입니다')
    label_maintext.configure(text='서버와 연동중입니다')
    label_image.configure(image=image3)
    label_yesbutton.place_forget()
    label_nobutton.place_forget()
    count = 3
    win.after(1000, next)
    print(count)
# endregion

# region 이미지파일
image0 = tkinter.PhotoImage(file="../images/logo.png")
image1 = tkinter.PhotoImage(file="../images/cup.png")
image2 = tkinter.PhotoImage(file="../images/qr.png")
image3 = tkinter.PhotoImage(file="../images/server.png")

image4_1 = tkinter.PhotoImage(file="../images/protein_1_WPI_resize.png")
image4_2 = tkinter.PhotoImage(file="../images/protein_2_WPC_resize.png")
image4_3 = tkinter.PhotoImage(file="../images/protein_3_CAS_resize.png")
image4_4 = tkinter.PhotoImage(file="../images/protein_4_VEG_resize.png")
image4_5 = tkinter.PhotoImage(file="../images/protein_5_GAI_resize.png")
image4_6 = tkinter.PhotoImage(file="../images/protein_6_BCAA_resize.png")

image5 = tkinter.PhotoImage(file="../images/scoop.png")
image6 = tkinter.PhotoImage(file="../images/done.png")
yesimg = tkinter.PhotoImage(file="../images/ybtn_resize.png")
noimg = tkinter.PhotoImage(file="../images/nbtn_resize.png")
# endregion
#초기화면
maintext = tkinter.Label(win, text="개인맞춤 운동 보조제 자판기\n""HY-PERSONAL BOOSTER VENDER", font=("G마켓 산스 TTF Bold",40),bg='#FBCA53', width=100, height=7)
logo_image = tkinter.Label(win, image=image0,bg='#FBCA53',width=200, height=200)
subtext = tkinter.Label(win, text="이용하시려면 화면을 터치해 주세요", font=("G마켓 산스 TTF Medium",35),bg='#FBCA53')
maintext.pack(side='top')
logo_image.pack(side='top')
subtext.pack(side='bottom',ipady=100)

label_maintext = tkinter.Label(win, text="투입구에 텀블러를 올려주세요", font=("G마켓 산스 TTF Medium", 35), bg='#FBCA53')
label_image = tkinter.Label(win, image=image1,bg='#FBCA53')
label_subtext = tkinter.Label(win, text="투입하셨다면 클릭해주세요", font=("G마켓 산스 TTF Medium",35),bg='#FBCA53')
label_powtext_1 = tkinter.Label(win, text="WHI", font=("G마켓 산스 TTF Medium",20),bg='#FBCA53',height=400,anchor='nw')
label_powtext_2 = tkinter.Label(win, text="40g", font=("G마켓 산스 TTF Medium",20),bg='#FBCA53')
label_powimage = tkinter.Label(win, image=image4_1, bg='#FBCA53')
label_yesbutton = tkinter.Button(win, image=yesimg,command=yesbtn, width=70,height=70, anchor="center",bg='#FBCA53')
label_nobutton = tkinter.Button(win, image=noimg, command=nobtn, width=70,height=70, anchor="center",bg='#FBCA53')


# 무게센서 클래스 지정
if __name__ == "__main__":
    win.bind("<Button>", clickMouse)
    wei_cup = Weightsensor()
    wei_pow = Weightsensor()
    mt_gui = Thread(target=win.mainloop)
    mt_wei_cup = Thread(target=wei_cup.weight_cup)
    mt_wei_pow = Thread(target=wei_pow.weight_powder)
    mt_gui.start()
    mt_wei_cup.start()
    mt_wei_pow.start()