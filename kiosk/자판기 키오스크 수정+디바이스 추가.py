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
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
cred = credentials.Certificate("hypersonal-booster-firebase-adminsdk-ikwz1-f2efb4d5f9.json")
firebase_admin.initialize_app(cred,{'databaseURL': 'https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app/1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/members'})
# endregion
# region 전역변수 선언
global first_weight
global second_weight
global val_cup
global val_1
global val_2
global val_3
global val_4
global val_5
global val_6
global moctrl
global usernumber
global gram
global count
count = 0
global before_or_after
global check
# endregion
# region 모터
# region 모터 핀 설정
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
motor1 = 21
GPIO.setup(motor1, GPIO.OUT, initial=GPIO.LOW)
p1 = GPIO.PWM(motor1, 10)
motor2 = 20
GPIO.setup(motor2, GPIO.OUT, initial=GPIO.LOW)
p2 = GPIO.PWM(motor2, 10)
motor3 = 16
GPIO.setup(motor3, GPIO.OUT, initial=GPIO.LOW)
p3 = GPIO.PWM(motor3, 10)
motor4 = 12
GPIO.setup(motor4, GPIO.OUT, initial=GPIO.LOW)
p4 = GPIO.PWM(motor4, 10)
motor5 = 7
GPIO.setup(motor5, GPIO.OUT, initial=GPIO.LOW)
p5 = GPIO.PWM(motor5, 10)
motor6 = 8
GPIO.setup(motor6, GPIO.OUT, initial=GPIO.LOW)
p6 = GPIO.PWM(motor6, 10)
# endregion
#모터제어함수(번호,그램)
def whichmotor(motorcontrol,getgram):
    global val_cup
    global first_weight
    global second_weight
    second_weight = val_cup
    if motorcontrol == 1:
        p1.start(0)
        p1.ChangeDutyCycle(100)
        time.sleep(8)
        p1.stop()
        time.sleep(2)
        second_weight = val_cup
        print("second: %d" % second_weight)
        while (second_weight - first_weight) < getgram:
                p1.start(0)
                p1.ChangeDutyCycle(100)
                time.sleep(1)
                p1.stop()
                time.sleep(2)
                second_weight = val_cup
                print("second: %d" %second_weight)
    elif motorcontrol == 2:
        p2.start(0)
        p2.ChangeDutyCycle(100)
        time.sleep(8)
        p2.stop()
        time.sleep(2)
        second_weight = val_cup
        print("second: %d" % second_weight)
        while (second_weight - first_weight) < getgram:
                p2.start(0)
                p2.ChangeDutyCycle(100)
                time.sleep(1)
                p2.stop()
                time.sleep(2)
                second_weight = val_cup
                print("second: %d" % second_weight)
    elif motorcontrol == 3:
        p3.start(0)
        p3.ChangeDutyCycle(100)
        time.sleep(8)
        p3.stop()
        time.sleep(2)
        second_weight = val_cup
        print("second: %d" % second_weight)
        while (second_weight - first_weight) < getgram:
                p3.start(0)
                p3.ChangeDutyCycle(100)
                time.sleep(1)
                p3.stop()
                time.sleep(2)
                second_weight = val_cup
                print("second: %d" % second_weight)
    elif motorcontrol == 4:
        p4.start(0)
        p4.ChangeDutyCycle(100)
        time.sleep(8)
        p4.stop()
        time.sleep(2)
        second_weight = val_cup
        print("second: %d" % second_weight)
        while (second_weight - first_weight) < getgram:
                p4.start(0)
                p4.ChangeDutyCycle(100)
                time.sleep(1)
                p4.stop()
                time.sleep(2)
                second_weight = val_cup
                print("second: %d" % second_weight)
    elif motorcontrol == 5:
        p5.start(0)
        p5.ChangeDutyCycle(100)
        time.sleep(8)
        p5.stop()
        time.sleep(2)
        second_weight = val_cup
        print("second: %d" % second_weight)
        while (second_weight - first_weight) < getgram:
                p5.start(0)
                p5.ChangeDutyCycle(100)
                time.sleep(1)
                p5.stop()
                time.sleep(2)
                second_weight = val_cup
                print("second: %d" % second_weight)
    elif motorcontrol == 6:
        p6.start(0)
        p6.ChangeDutyCycle(100)
        time.sleep(8)
        p6.stop()
        time.sleep(2)
        second_weight = val_cup
        print("second: %d" % second_weight)
        while (second_weight - first_weight) < getgram:
                p6.start(0)
                p6.ChangeDutyCycle(100)
                time.sleep(1)
                p6.stop()
                time.sleep(2)
                second_weight = val_cup
                print("second: %d" % second_weight)
# endregion
# region 무게 센서
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
# region 무게 센서 핀 및 상수 설정
hx_main = HX711(26, 19)
hx_main.set_reading_format("MSB", "MSB")
hx_main.set_reference_unit(399)

hx_1 = HX711(13, 6)
hx_1.set_reading_format("MSB", "MSB")
hx_1.set_reference_unit(423)

hx_2 = HX711(5, 11)
hx_2.set_reading_format("MSB", "MSB")
hx_2.set_reference_unit(406)

hx_3 = HX711(9, 10)
hx_3.set_reading_format("MSB", "MSB")
hx_3.set_reference_unit(414)

hx_4 = HX711(22, 27)
hx_4.set_reading_format("MSB", "MSB")
hx_4.set_reference_unit(401)

hx_5 = HX711(17, 4)
hx_5.set_reading_format("MSB", "MSB")
hx_5.set_reference_unit(399)

hx_6 = HX711(3, 2)
hx_6.set_reading_format("MSB", "MSB")
hx_6.set_reference_unit(412)
# endregion
# 무게센서 함수
class Weightsensor():
    def __init__(self):
        global val_cup
        global val_1
        global val_2
        global val_3
        global val_4
        global val_5
        global val_6
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
        while True:
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
            time.sleep(3)
            print("원료통1 측정값: %f" % val_1)
            print("원료통2 측정값: %f" % val_2)
            print("원료통3 측정값: %f" % val_3)
            print("원료통4 측정값: %f" % val_4)
            print("원료통5 측정값: %f" % val_5)
            print("원료통6 측정값: %f" % val_6)
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
    global before_or_after
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
        win.after(3000, next)
    # 투하 완료에서 다시 메인페이지로 넘어가기 (next함수로 넘어감)
    elif count == 6:
        pass
def next():
    global count
    global moctrl
    global usernumber
    global gram
    global before_or_after
    global check
    global userid
    check = 0
    global pow_number
    if count == 2:
        # region QR 코드 인식
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
        userid = str(barcodeData[:-1])
        before_or_after = int(barcodeData[-1])
        print("회원ID : %s" % userid)
        print("before 0, after 1 : %d" % before_or_after)
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
        win.after(1000, next)
        print(count)
    elif count == 3:
        member_db = "1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/members/"
        booster_db = "1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/boostertest/"
        before_or_after = 0
        before_powder = [""]
        after_powder = [""]
        check_number = 0
        # before
        if before_or_after == 0:
            for i in range(1, 4):
                before_powder.append(db.reference(member_db + userid + "/bp_" + str(i)).get())
                pow_number = 0
                print("db : %s" %before_powder[i])
                a = before_powder[i]
                for j in range(1, 7):
                    pow_number += 1
                    b = vender_powder[j]
                    print("vender : %s" %vender_powder[j])
                    if str(a) == str(b):
                        check = 1
                        print("check : %d" % check)
                        break
                check_number = i
                print(check_number)
                if check == 1:
                    break
            # 자판기 안에 있음
            if check == 1:
                required_gram = int(db.reference(member_db + userid + "/bp_" + str(check_number) + "gram").get())
                brand = "마이프로틴"
                name = "프로틴 테스트"
                amount = 35
                fat = 7
                carl = 107
                # region 텍스트 설정
                if check_number == 1:
                    sort = "WPI"
                    protein = 11
                    carb = 7
                    label_powimage.configure(image=image4_1)
                elif check_number == 2:
                    sort = "WPC"
                    protein = 12
                    carb = 8
                    label_powimage.configure(image=image4_2)
                elif check_number == 3:
                    sort = "카제인"
                    protein = 13
                    carb = 9
                    label_powimage.configure(image=image4_3)
                elif check_number == 4:
                    sort = "비건"
                    protein = 14
                    carb = 10
                    label_powimage.configure(image=image4_4)
                elif check_number == 5:
                    sort = "게이너"
                    protein = 15
                    carb = 11
                    label_powimage.configure(image=image4_5)
                elif check_number == 6:
                    sort = "부스터"
                    protein = 16
                    carb = 12
                    label_powimage.configure(image=image4_6)
                # endregion
                powtext = "\n제품 : %s\n브랜드: %s\n주요 성분: %s\n용량 : %.1fg\n" \
                          "상세성분: 1회분 %dg당 %dkcal 단백질 %dg 탄수화물 %dg 지방%dg" %(name,
                            brand, sort, required_gram,carl, amount, protein, carb, fat)
                label_powtext_1.configure(text=powtext)
            # 자판기 안에 없음 비슷한거 찾음
            else:
                brand = "마이프로틴"
                name = "프로틴 테스트"
                amount = 35
                fat = 11
                carl = 103
                check_number = 0
                for i in range(1, 4):
                    a = str(before_powder[i])[0:3]
                    for j in range(1, 7):
                        b = str(vender_powder[j][0:3])
                        if a == b:
                            check_number = i
                            break
                    if check_number != 0:
                        break
                required_gram = float(db.reference(member_db + userid + "/bp_"+str(check_number)+"gram").get())
                amount_gram = float(db.reference(booster_db + str(before_powder[check_number]) + "/amount").get())
                db_protein = float(db.reference(booster_db + str(before_powder[check_number]) + "/protein").get())
                required_protein = required_gram / amount_gram * db_protein
                print(
                    "요구용량:%.1fg,단백질:%.1fg\n해당제품 1회용량:%.1fg 단백질:%.1fg" % (required_gram, required_protein, amount_gram, db_protein))

                print(a)
                # region 텍스트 설정
                if check_number == 1:
                    sort = "WPI"
                    protein = 11
                    carb = 7
                    label_powimage.configure(image=image4_1)
                elif check_number == 2:
                    sort = "WPC"
                    protein = 12
                    carb = 8
                    label_powimage.configure(image=image4_2)
                elif check_number == 3:
                    sort = "카제인"
                    protein = 13
                    carb = 9
                    label_powimage.configure(image=image4_3)
                elif check_number == 4:
                    sort = "비건"
                    protein = 14
                    carb = 10
                    label_powimage.configure(image=image4_4)
                elif check_number == 5:
                    sort = "게이너"
                    protein = 15
                    carb = 11
                    label_powimage.configure(image=image4_5)
                elif check_number == 6:
                    sort = "부스터"
                    protein = 16
                    carb = 12
                    label_powimage.configure(image=image4_6)
                # endregion
                changed_gram = required_protein / protein * 30
                powtext = "\n제품 : %s\n브랜드: %s\n주요 성분: %s\n용량 : %.1fg\n" \
                          "상세성분: 1회분 %dg당 %dkcal 단백질 %dg 탄수화물 %dg 지방%dg" % (name,
                                                                            brand, sort, changed_gram, amount, carl,
                                                                            protein, carb, fat)
                powtext = "\n회원님의 추천 보조제는 이 자판기에 존재하지 않습니다\n" \
                          "비슷한 종류의 보조제를 추천드립니다. 드시겠습니까?\n" + powtext
                label_powtext_1.configure(text=powtext)
        # after
        else:
            for i in range(1, 4):
                after_powder.append(db.reference(member_db + usernumber + "/ap_" + str(i)).get())
                pow_number = 0
                print("db : %s" %after_powder[i])
                a = after_powder[i]
                for j in range(1, 7):
                    pow_number += 1
                    b = vender_powder[j]
                    print("vender : %s" %vender_powder[j])
                    if str(a) == str(b):
                        check = 1
                        print("check : %d" % check)
                        break
                check_number = i
                print(check_number)
                if check == 1:
                    break
            # 자판기 안에 있음
            if check == 1:
                required_gram = int(db.reference(member_db + usernumber + "/ap_" + str(check_number) + "gram").get())
                brand = "마이프로틴"
                name = "프로틴 테스트"
                amount = 35
                fat = 7
                carl = 107
                # region 텍스트 설정
                if check_number == 1:
                    sort = "WPI"
                    protein = 11
                    carb = 7
                    label_powimage.configure(image=image4_1)
                elif check_number == 2:
                    sort = "WPC"
                    protein = 12
                    carb = 8
                    label_powimage.configure(image=image4_2)
                elif check_number == 3:
                    sort = "카제인"
                    protein = 13
                    carb = 9
                    label_powimage.configure(image=image4_3)
                elif check_number == 4:
                    sort = "비건"
                    protein = 14
                    carb = 10
                    label_powimage.configure(image=image4_4)
                elif check_number == 5:
                    sort = "게이너"
                    protein = 15
                    carb = 11
                    label_powimage.configure(image=image4_5)
                elif check_number == 6:
                    sort = "부스터"
                    protein = 16
                    carb = 12
                    label_powimage.configure(image=image4_6)
                # endregion
                powtext = "\n제품 : %s\n브랜드: %s\n주요 성분: %s\n용량 : %.1fg\n" \
                          "상세성분: 1회분 %dg당 %dkcal 단백질 %dg 탄수화물 %dg 지방%dg" %(name,
                            brand, sort, required_gram,carl, amount, protein, carb, fat)
                label_powtext_1.configure(text=powtext)
            # 자판기 안에 없음 비슷한거 찾음
            else:
                brand = "마이프로틴"
                name = "프로틴 테스트"
                amount = 35
                fat = 11
                carl = 103
                check_number = 0
                for i in range(1, 4):
                    a = str(before_powder[i])[0:3]
                    for j in range(1, 7):
                        b = str(vender_powder[j][0:3])
                        if a == b:
                            check_number = i
                            break
                    if check_number != 0:
                        break
                required_gram = float(db.reference(member_db + usernumber + "/ap_"+str(check_number)+"gram").get())
                amount_gram = float(db.reference(booster_db + str(before_powder[check_number]) + "/amount").get())
                db_protein = float(db.reference(booster_db + str(before_powder[check_number]) + "/protein").get())
                required_protein = required_gram / amount_gram * db_protein
                print(
                    "요구용량:%.1fg,단백질:%.1fg\n해당제품 1회용량:%.1fg 단백질:%.1fg" % (required_gram, required_protein, amount_gram, db_protein))

                print(a)
                # region 텍스트 설정
                if check_number == 1:
                    sort = "WPI"
                    protein = 11
                    carb = 7
                    label_powimage.configure(image=image4_1)
                elif check_number == 2:
                    sort = "WPC"
                    protein = 12
                    carb = 8
                    label_powimage.configure(image=image4_2)
                elif check_number == 3:
                    sort = "카제인"
                    protein = 13
                    carb = 9
                    label_powimage.configure(image=image4_3)
                elif check_number == 4:
                    sort = "비건"
                    protein = 14
                    carb = 10
                    label_powimage.configure(image=image4_4)
                elif check_number == 5:
                    sort = "게이너"
                    protein = 15
                    carb = 11
                    label_powimage.configure(image=image4_5)
                elif check_number == 6:
                    sort = "부스터"
                    protein = 16
                    carb = 12
                    label_powimage.configure(image=image4_6)
                # endregion
                changed_gram = required_protein / protein * 30
                powtext = "\n제품 : %s\n브랜드: %s\n주요 성분: %s\n용량 : %.1fg\n" \
                          "상세성분: 1회분 %dg당 %dkcal 단백질 %dg 탄수화물 %dg 지방%dg" % (name,
                                                                            brand, sort, changed_gram, amount, carl,
                                                                            protein, carb, fat)
                powtext = "\n회원님의 추천 보조제는 이 자판기에 존재하지 않습니다\n" \
                          "비슷한 종류의 보조제를 추천드립니다. 드시겠습니까?\n" + powtext
                label_powtext_1.configure(text=powtext)
        label_maintext.configure(text='선택하신 프로틴이 맞는지 확인해 주세요')
        label_subtext.pack_forget()
        label_image.pack_forget()
        label_powimage.pack(side="left",ipadx=70,anchor='nw')
        label_powtext_1.pack(side="left")
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
    label_powimage.pack_forget()
    label_yesbutton.place_forget()
    label_nobutton.place_forget()
    label_image.pack()
    label_subtext.pack()
def nobtn():
    global count
    maintext.pack()
    logo_image.pack()
    subtext.pack()
    label_maintext.pack_forget()
    label_image.pack_forget()
    label_subtext.pack_forget()
    label_powimage.pack_forget()
    label_powtext_1.pack_forget()
    label_yesbutton.place_forget()
    label_nobutton.place_forget()
    count = 0
# endregion

# region 이미지파일
image0 = tkinter.PhotoImage(file="images/logo.png")
image1 = tkinter.PhotoImage(file="images/cup.png")
image2 = tkinter.PhotoImage(file="images/qr.png")
image3 = tkinter.PhotoImage(file="images/server.png")

image4_1 = tkinter.PhotoImage(file="images/protein_1_WPI_resize.png")
image4_2 = tkinter.PhotoImage(file="images/protein_2_WPC_resize.png")
image4_3 = tkinter.PhotoImage(file="images/protein_3_CAS_resize.png")
image4_4 = tkinter.PhotoImage(file="images/protein_4_VEG_resize.png")
image4_5 = tkinter.PhotoImage(file="images/protein_5_GAI_resize.png")
image4_6 = tkinter.PhotoImage(file="images/protein_6_BCAA_resize.png")

image5 = tkinter.PhotoImage(file="images/scoop.png")
image6 = tkinter.PhotoImage(file="images/done.png")
yesimg = tkinter.PhotoImage(file="images/ybtn_resize.png")
noimg = tkinter.PhotoImage(file="images/nbtn_resize.png")
# endregion

#자판기내 프로틴 고유번호
vender_powder = ["", "1011", "1021", "1031", "1041", "1051", "1061"]
# region 초기 화면 설정
maintext = tkinter.Label(win, text="개인맞춤 운동 보조제 자판기\n""HY-PERSONAL BOOSTER VENDER", font=("G마켓 산스 TTF Bold",40),bg='#FBCA53', width=100, height=7)
logo_image = tkinter.Label(win, image=image0,bg='#FBCA53',width=200, height=200)
subtext = tkinter.Label(win, text="이용하시려면 화면을 터치해 주세요", font=("G마켓 산스 TTF Medium",35),bg='#FBCA53')
maintext.pack(side='top')
logo_image.pack(side='top')
subtext.pack(side='bottom',ipady=100)

label_maintext = tkinter.Label(win, text="투입구에 텀블러를 올려주세요", font=("G마켓 산스 TTF Medium", 35), bg='#FBCA53')
label_image = tkinter.Label(win, image=image1,bg='#FBCA53')
label_subtext = tkinter.Label(win, text="투입하셨다면 클릭해주세요", font=("G마켓 산스 TTF Medium",35),bg='#FBCA53')
label_powtext_1 = tkinter.Label(win, text="WHI", font=("G마켓 산스 TTF Medium",20),
                                bg='#FBCA53',height=400,width=300,anchor='nw',justify=LEFT)
label_powimage = tkinter.Label(win, image=image4_1, bg='#FBCA53', width=300, height=400)
label_yesbutton = tkinter.Button(win, image=yesimg,command=yesbtn, width=70,height=70, anchor="center",bg='#FBCA53')
label_nobutton = tkinter.Button(win, image=noimg, command=nobtn, width=70,height=70, anchor="center",bg='#FBCA53')
# endregion

# 실행
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