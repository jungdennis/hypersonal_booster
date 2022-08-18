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
# endregion
# region firebase설정
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
global gram
global count
count = 0
global before_or_after
global check
global userid
global name
global brand
global sort
global check_number
global fat
global protein
global carb
global carl
global amount
# endregion
# 코드 나누기
def changeqrcode(barcodeqr):
    global userid
    global before_or_after
    userid = str(barcodeqr[:-1])
    before_or_after = int(barcodeqr[-1])
def decodeboosterdb(boostercode):
    global brand
    global sort
    brand_code = boostercode[0:3]
    class_1 = boostercode[4]
    class_2 = boostercode[5]
    class_3 = boostercode[6]
    if brand_code == "BDN":
        brand = "바디나인"
    elif brand_code == "CLB":
        brand = "칼로바이"
    elif brand_code == "MPT":
        brand = "마이프로틴"
    if class_1 == 0:
        #운동전
        if class_2 == 0:
            sort = "BCAA"
        else:
            sort = "부스터"
        if class_3 == 0:
            sort = "카페인 미포함 " + sort
        else:
            sort = "카페인 포함 " + sort
    elif class_1 == 1:
        #운동후
        if class_2 == 0:
            sort = "게이너"
        else:
            if class_3 == 0:
                sort = "WPC"
            elif class_3 == 1:
                sort = "WPI"
            elif class_3 == 2:
                sort = "비건"
            elif class_3 == 3:
                sort = "카제인"
def what_protein():
    global amount
    global name
    global brand
    global sort
    global carl
    global carb
    global fat
    global protein
    global check_number
    # region what protein
    if check_number == 1:
        name = "머슬킹콩 부스터 레몬맛"
        brand = "킹콩팩토리"
        sort = "카페인 함유 부스터"
        amount = 16
        carl = 40
        carb = 6
        fat = 0
        protein = 4
        label_powimage.configure(image=image4_1)
    elif check_number == 2:
        name = "퍼펙트 파워쉐이크 딸기맛"
        brand = "칼로바이"
        sort = "WPC"
        amount = 44
        carl = 168
        carb = 9
        fat = 1.2
        protein = 33
        label_powimage.configure(image=image4_2)
    elif check_number == 3:
        name = "퍼펙트 파워쉐이크 아이솔레이트 딸기맛"
        brand = "칼로바이"
        sort = "WPI"
        amount = 40
        carl = 155
        carb = 11
        fat = 1.1
        protein = 36
        label_powimage.configure(image=image4_3)
    elif check_number == 4:
        name = "식물성 프로틴 블렌드 초콜렛"
        brand = "마이프로틴"
        sort = "비건"
        amount = 30
        carl = 110
        carb = 3.3
        fat = 0.8
        protein = 21
        label_powimage.configure(image=image4_4)
    elif check_number == 5:
        name = "어드밴스드 웨이트 게이너 초콜렛스무스"
        brand = "마이프로틴"
        sort = "WPC 게이너"
        amount = 125
        carl = 442
        carb = 58
        fat = 6.8
        protein = 36
        label_powimage.configure(image=image4_5)
    elif check_number == 6:
        name = "퍼펙트 파워 BCAA 6000 포도맛"
        brand = "칼로바이"
        sort = "BCAA"
        amount = 10
        carl = 40
        carb = 5
        fat = 0
        protein = 5
        label_powimage.configure(image=image4_6)
    # endregion
# region 화면전환함수
def clickMouse(event):
    # region 함수 내 전역변수 선언
    global count
    global userid
    global gram
    global val_cup
    global first_weight
    global second_weight
    global before_or_after
    global brand
    global sort
    global amount
    global name
    global carl
    global carb
    global fat
    global protein
    global check_number
    # endregion
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
        print("%d번 페이지" %count)
    # 텀블러 인식 페이지에서 넘어가기
    elif count == 1:
        label_maintext.configure(text='카메라에 qr코드를 인식시켜 주세요')
        label_subtext.configure(text='인식시켰다면 화면을 클릭해 주세요')
        label_image.configure(image=image2)
        count += 1
        print("%d번 페이지" %count)
        win.after(500, next)
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
        print("투하량 %.1fg, 동작모터 %s번" %(gram, check_number))
        label_subtext.configure(text='감사합니다')
        label_maintext.configure(text='투하가 완료되었습니다')
        label_image.configure(image=image6)
        count += 1
        print("%d번 페이지" %count)
        win.after(3000, next)
    # 투하 완료에서 다시 메인페이지로 넘어가기 (next함수로 넘어감)
    elif count == 6:
        pass
def next():
    # region 함수 내 전역변수 선언
    global count
    global userid
    global gram
    global before_or_after
    global check
    global brand
    global sort
    check = 0
    global pow_number
    global barcode
    global amount
    global name
    global carl
    global carb
    global fat
    global protein
    global check_number
    # endregion
    if count == 2:
        barcode = "alscksrb0"
        changeqrcode(barcode)
        print(userid)
        print(before_or_after)
        label_subtext.configure(text='서버와 연동중입니다')
        label_maintext.configure(text='서버와 연동중입니다')
        label_image.configure(image=image3)
        label_yesbutton.place_forget()
        label_nobutton.place_forget()
        count += 1
        print("%d번 페이지" %count)
        win.after(1000, next)
    elif count == 3:
        # region db링크 및 초기설정
        member_db = "1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/members/"
        booster_db = "1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/booster/"
        before_powder = [""]
        after_powder = [""]
        check_number = 0
        check = 0
        # endregion
        # 운동 전 보충제 확인
        if before_or_after == 0:
            # region 데이터베이스로부터 추천 보충제 코드 받고 자판기에 있는 보충제코드와 비교
            for i in range(1, 4):
                before_powder.append(db.reference(member_db + userid + "/bp_" + str(i)).get())
                pow_number = 0
                print("db : %s" %before_powder[i])
                a = before_powder[i]
                for j in range(1, 7):
                    pow_number += 1
                    print(pow_number)
                    b = vender_powder[j]
                    print("vender : %s" %vender_powder[j])
                    if str(a) == str(b):
                        check_number = j
                        print("check_number : %d" % check_number)
                        break
                if check_number != 0:
                    check = i
                    print(check)
                    break
            # endregion
            # 자판기 안에 있음, 그램 정보 받고 텍스트 설정
            if check != 0:
                required_gram = int(db.reference(member_db + userid + "/bp_" + str(check) + "gram").get())
                what_protein()
                gram = required_gram
                rate = float(gram/amount)
                powtext = "\n제품 : %s\n브랜드: %s\n주요 성분: %s\n용량 : %.1fg\n" \
                          "영양정보\n1회분 %dg당 %dkcal 단백질 %dg 탄수화물 %dg 지방%dg\n" \
                          "총 %.1fkcal 단백질 %.1fg 탄수화물 %.1fg 지방%.1fg" %(name, brand, sort, gram,
                        amount, carl, protein, carb, fat, carl*rate, protein*rate, carb*rate, fat*rate)
                label_powtext_1.configure(text=powtext)
            # 자판기 안에 없음 비슷한거 찾음
            else:
                # region 보충제 종류 코드와 자판기에 있는 보충제 종류 코드 비교
                check = 0
                check_number = 0
                for i in range(1, 4):
                    a = str(before_powder[i])[3:7]
                    for j in range(1, 7):
                        b = str(vender_powder[j][3:7])
                        if a == b:
                            check_number = j
                            break
                    if check_number != 0:
                        check = i
                        break
                # endregion
                # 데이터베이스 그램으로부터 필요 단백질 그램 계산
                required_gram = float(db.reference(member_db + userid + "/bp_"+ str(check) +"gram").get())
                amount_gram = float(db.reference(booster_db + str(before_powder[check]) + "/amount(1회제공량(가루g액체ml))").get())
                db_protein = float(db.reference(booster_db + str(before_powder[check]) + "/protein(g)").get())
                required_protein = required_gram / amount_gram * db_protein
                print("보충제 종류 코드 : %s, check_number(원료통 번호) : %s\n요구용량:%.1fg,단백질:%.1fg\n"
                      "해당제품 1회용량:%.1fg 단백질:%.1fg" % (a, check_number,
                                                     required_gram, required_protein, amount_gram, db_protein))
                what_protein()
                # 필요 단백질 그램에 따른 프로틴 양 변화 및 텍스트 설정
                changed_gram = required_protein / protein * amount
                gram = changed_gram
                rate = float(gram / amount)
                powtext = "\n제품 : %s\n브랜드: %s\n주요 성분: %s\n용량 : %.1fg\n" \
                          "영양정보\n1회분 %dg당 %dkcal 단백질 %dg 탄수화물 %dg 지방%dg\n" \
                          "총 %.1fkcal 단백질 %.1fg 탄수화물 %.1fg 지방%.1fg" % (name, brand, sort, gram,
                                                                       amount, carl, protein, carb, fat, carl * rate,
                                                                       protein * rate, carb * rate, fat * rate)
                powtext = "\n회원님의 추천 보조제는 이 자판기에 존재하지 않습니다\n" \
                          "비슷한 종류의 보조제를 추천드립니다. 드시겠습니까?\n" + powtext
                label_powtext_1.configure(text=powtext)
        # 운동 후 보충제 확인
        else:
            # region 데이터베이스로부터 추천 보충제 코드 받고 자판기에 있는 보충제코드와 비교
            for i in range(1, 4):
                after_powder.append(db.reference(member_db + userid + "/ap_" + str(i)).get())
                pow_number = 0
                print("db : %s" %after_powder[i])
                a = after_powder[i]
                for j in range(1, 7):
                    pow_number += 1
                    b = vender_powder[j]
                    print("vender : %s" %vender_powder[j])
                    if str(a) == str(b):
                        check_number = j
                        print("check_number : %d" % check_number)
                        break
                if check_number != 0:
                    check = i
                    break
            # endregion
            # 자판기 안에 있음, 그램 정보 받고 텍스트 설정
            if check != 0:
                required_gram = int(db.reference(member_db + userid + "/ap_" + str(check) + "gram").get())
                what_protein()
                gram = required_gram
                rate = float(gram / amount)
                powtext = "\n제품 : %s\n브랜드: %s\n주요 성분: %s\n용량 : %.1fg\n" \
                          "영양정보\n1회분 %dg당 %dkcal 단백질 %dg 탄수화물 %dg 지방%dg\n" \
                          "총 %.1fkcal 단백질 %.1fg 탄수화물 %.1fg 지방%.1fg" % (name, brand, sort, gram,
                                                                       amount, carl, protein, carb, fat, carl * rate,
                                                                       protein * rate, carb * rate, fat * rate)
                label_powtext_1.configure(text=powtext)
            # 자판기 안에 없음 비슷한거 찾음
            else:
                # 보충제 종류 코드와 자판기에 있는 보충제 종류 코드 비교
                check = 0
                check_number = 0
                for i in range(1, 4):
                    a = str(after_powder[i])[3:7]
                    for j in range(1, 7):
                        b = str(vender_powder[j][3:7])
                        if a == b:
                            check_number = j
                            break
                    if check_number != 0:
                        check = i
                        break
                # 데이터베이스 그램으로부터 필요 단백질 그램 계산
                required_gram = float(db.reference(member_db + userid + "/ap_"+str(check)+"gram").get())
                amount_gram = float(db.reference(booster_db + str(after_powder[check]) + "/amount(1회제공량(가루g액체ml))").get())
                db_protein = float(db.reference(booster_db + str(after_powder[check]) + "/protein(g)").get())
                required_protein = required_gram / amount_gram * db_protein
                print("보충제 종류 코드 : %s, check_number(원료통 번호) : %s\n요구용량:%.1fg,단백질:%.1fg\n"
                      "해당제품 1회용량:%.1fg 단백질:%.1fg" % (a, check_number,
                                                     required_gram, required_protein, amount_gram, db_protein))
                what_protein()
                # 필요 단백질 그램에 따른 프로틴 양 변화 및 텍스트 설정
                changed_gram = required_protein / protein * amount
                gram = changed_gram
                rate = float(gram / amount)
                powtext = "\n제품 : %s\n브랜드: %s\n주요 성분: %s\n용량 : %.1fg\n" \
                          "영양정보\n1회분 %dg당 %dkcal 단백질 %dg 탄수화물 %dg 지방%dg\n" \
                          "총 %.1fkcal 단백질 %.1fg 탄수화물 %.1fg 지방%.1fg" % (name, brand, sort, gram,
                                                                       amount, carl, protein, carb, fat, carl * rate,
                                                                       protein * rate, carb * rate, fat * rate)
                powtext = "\n회원님의 추천 보조제는 이 자판기에 존재하지 않습니다\n" \
                          "비슷한 종류의 보조제를 추천드립니다. 드시겠습니까?\n" + powtext
                label_powtext_1.configure(text=powtext)
        # region 공통 텍스트 및 화면 설정
        label_maintext.configure(text='선택하신 프로틴이 맞는지 확인해 주세요')
        label_subtext.pack_forget()
        label_image.pack_forget()
        label_powimage.pack(side="left",ipadx=70,anchor='nw')
        label_powtext_1.pack(side="left")
        label_yesbutton.place(x=475,y=550)
        label_nobutton.place(x=730,y=550)
        label_upbutton.place(x=560, y=550)
        label_downbutton.place(x=645, y=550)
        count += 1
        print("%d번 페이지" %count)
        # endregion
    elif count == 6:
        maintext.pack(side='top')
        logo_image.pack(side='top')
        subtext.pack(side='bottom', ipady=100)
        label_maintext.pack_forget()
        label_image.pack_forget()
        label_subtext.pack_forget()
        count = 0
        print("%d번 페이지" %count)
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
    label_upbutton.place_forget()
    label_downbutton.place_forget()
    label_image.pack()
    label_subtext.pack()
def nobtn():
    global count
    maintext.pack(side='top')
    logo_image.pack(side='top')
    subtext.pack(side='bottom', ipady=100)
    label_maintext.pack_forget()
    label_image.pack_forget()
    label_subtext.pack_forget()
    label_powimage.pack_forget()
    label_powtext_1.pack_forget()
    label_yesbutton.place_forget()
    label_nobutton.place_forget()
    label_upbutton.place_forget()
    label_downbutton.place_forget()
    count = 0
def morebtn():
    # region 함수 내 전역변수 선언
    global gram
    global amount
    global name
    global carl
    global carb
    global fat
    global protein
    global check
    # endregion
    gram += 1
    rate = float(gram / amount)
    powtext = "\n제품 : %s\n브랜드: %s\n주요 성분: %s\n용량 : %.1fg\n" \
              "영양정보\n1회분 %dg당 %dkcal 단백질 %dg 탄수화물 %dg 지방%dg\n" \
              "총 %.1fkcal 단백질 %.1fg 탄수화물 %.1fg 지방%.1fg" % (name, brand, sort, gram,
                                                           amount, carl, protein, carb, fat, carl * rate,
                                                           protein * rate, carb * rate, fat * rate)
    if check != 0:
        powtext = "\n회원님의 추천 보조제는 이 자판기에 존재하지 않습니다\n" \
                          "비슷한 종류의 보조제를 추천드립니다. 드시겠습니까?\n" + powtext
    label_powtext_1.configure(text=powtext)
def lessbtn():
    # region 함수 내 전역변수 선언
    global gram
    global amount
    global name
    global carl
    global carb
    global fat
    global protein
    global check
    # endregion
    gram -= 1
    if gram < 10:
        gram += 1
    rate = float(gram / amount)
    powtext = "\n제품 : %s\n브랜드: %s\n주요 성분: %s\n용량 : %.1fg\n" \
              "영양정보\n1회분 %dg당 %dkcal 단백질 %dg 탄수화물 %dg 지방%dg\n" \
              "총 %.1fkcal 단백질 %.1fg 탄수화물 %.1fg 지방%.1fg" % (name, brand, sort, gram,
                                                           amount, carl, protein, carb, fat, carl * rate,
                                                           protein * rate, carb * rate, fat * rate)
    if check != 0:
        powtext = "\n회원님의 추천 보조제는 이 자판기에 존재하지 않습니다\n" \
                          "비슷한 종류의 보조제를 추천드립니다. 드시겠습니까?\n" + powtext
    label_powtext_1.configure(text=powtext)
# endregion
# region 창설정
win = tk.Tk()
win.title("Hy-personal Booster Vender")
win.geometry("1280x800")
win.resizable(True, True)
win.configure(bg='#FBCA53')
# endregion
# region 이미지파일
image0 = tkinter.PhotoImage(file="images/logo.png")
image1 = tkinter.PhotoImage(file="images/cup.png")
image2 = tkinter.PhotoImage(file="images/qr.png")
image3 = tkinter.PhotoImage(file="images/server.png")

image4_1 = tkinter.PhotoImage(file="images/원료통1_부스터_resize.png")
image4_2 = tkinter.PhotoImage(file="images/원료통2_WPC_resize.png")
image4_3 = tkinter.PhotoImage(file="images/원료통3_WPI_resize.png")
image4_4 = tkinter.PhotoImage(file="images/원료통4_비건_resize.png")
image4_5 = tkinter.PhotoImage(file="images/원료통5_게이너_resize.png")
image4_6 = tkinter.PhotoImage(file="images/원료통6_BCAA_resize.png")

image5 = tkinter.PhotoImage(file="images/scoop.png")
image6 = tkinter.PhotoImage(file="images/done.png")

yesimg = tkinter.PhotoImage(file="images/ybtn_resize.png")
noimg = tkinter.PhotoImage(file="images/nbtn_resize.png")

upimg = tkinter.PhotoImage(file="images/up_resize.png")
downimg = tkinter.PhotoImage(file="images/down_resize.png")
# endregion
# region 자판기내 프로틴 고유번호
vender_powder = ["", "KKF0011-01", "CLB0110-02", "CLB0111-04", "MPT0113-09", "MPT0100-02", "CLB0000-01"]
# endregion
# region 초기화면 설정
maintext = tkinter.Label(win, text="개인맞춤 운동 보조제 자판기\n""HY-PERSONAL BOOSTER VENDER",
                         font=("G마켓 산스 TTF Bold", 40),bg='#FBCA53', width=100, height=7)
logo_image = tkinter.Label(win, image=image0, bg='#FBCA53', width=100, height=100)
subtext = tkinter.Label(win, text="이용하시려면 화면을 터치해 주세요", font=("G마켓 산스 TTF Medium", 35), bg='#FBCA53')
maintext.pack(side='top')
logo_image.pack(side='top')
subtext.pack(side='bottom', ipady=100)

label_maintext = tkinter.Label(win, text="투입구에 텀블러를 올려주세요", font=("G마켓 산스 TTF Medium", 35), bg='#FBCA53')
label_image = tkinter.Label(win, image=image1,bg='#FBCA53')
label_subtext = tkinter.Label(win, text="투입하셨다면 클릭해주세요", font=("G마켓 산스 TTF Medium", 35), bg='#FBCA53')
label_powtext_1 = tkinter.Label(win, text="", font=("G마켓 산스 TTF Medium", 20),
                                bg='#FBCA53', height=400, width=300, anchor='nw', justify=LEFT)
label_powimage = tkinter.Label(win, image=image4_1, bg='#FBCA53', width=300, height=400)
label_yesbutton = tkinter.Button(win, image=yesimg, command=yesbtn, width=70, height=70,
                                 anchor="center", bg='#FBCA53', activebackground='#FBCA53', relief=FLAT, borderwidth=0)
label_nobutton = tkinter.Button(win, image=noimg, command=nobtn, width=70, height=70,
                                anchor="center", bg='#FBCA53', activebackground='#FBCA53', relief=FLAT, borderwidth=0)
label_upbutton = tkinter.Button(win, image=upimg, command=morebtn, width=70, height=70,
                                anchor="center", bg='#FBCA53', activebackground='#FBCA53', relief=FLAT,
                                repeatdelay=10, repeatinterval=100, borderwidth=0)
label_downbutton = tkinter.Button(win, image=downimg, command=lessbtn, width=70, height=70,
                                  anchor="center", bg='#FBCA53', activebackground='#FBCA53', relief=FLAT,
                                  repeatdelay=10, repeatinterval=100, borderwidth=0)
# endregion

# 실행
if __name__ == "__main__":
    win.bind("<Button>", clickMouse)
    win.mainloop()
    '''
    mt_gui = Thread(target=win.mainloop)
    mt_gui.start()
    '''