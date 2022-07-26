import tkinter
import tkinter as tk
from tkinter import ttk
from tkinter import *
import sys
import time
#무게센서hx711설정
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
#dc모터설정
from gpiozero import Motor
import time

md1 = Motor(forward=20, backward=21)
md2 = Motor(forward=12, backward=16)
md3 = Motor(forward=7, backward=8)

#텀블러, 원료통1~6세팅
hx_main = HX711(26, 19)
hx_1 = HX711(13, 6)
'''
hx_2 = HX711(5, 11)
hx_3 = HX711(9, 10)
hx_4 = HX711(22, 27)
hx_5 = HX711(17, 4)
hx_6 = HX711(3, 2)
'''
#무게센서 초기 오차값(텀블러는 이미 측정)
hx_main.set_reading_format("MSB", "MSB")
hx_main.set_reference_unit(399)
hx_1.set_reading_format("MSB", "MSB")
hx_1.set_reference_unit(referenceUnit)
'''
hx_2.set_reading_format("MSB", "MSB")
hx_2.set_reference_unit(referenceUnit)
hx_3.set_reading_format("MSB", "MSB")
hx_3.set_reference_unit(referenceUnit)
hx_4.set_reading_format("MSB", "MSB")
hx_4.set_reference_unit(referenceUnit)
hx_5.set_reading_format("MSB", "MSB")
hx_5.set_reference_unit(referenceUnit)
hx_6.set_reading_format("MSB", "MSB")
hx_6.set_reference_unit(referenceUnit)
'''
class Weightsensor:
    def __init__(self):
        self.count = 0
    def weight_cup(self):
        hx_main.reset()
        hx_main.tare()
        while True:
            try:
                #무게센서 값 측정
                val_cup = hx_main.get_weight(5)
                hx_main.power_down()
                hx_main.power_up()
                time.sleep(0.1)
                self.count += 1
            except (self.count == 10):
                return val_cup
                break
    def weight_1(self):
        hx_1.reset()
        hx_1.tare()
        while True:
            try:
                #무게센서 값 측정
                val_cup = hx_1.get_weight(5)
                hx_1.power_down()
                hx_1.power_up()
                time.sleep(0.1)
                self.count += 1
            except (self.count == 10):
                return val_cup
                break
    '''
    def weight_2(self):
        hx_2.reset()
        hx_2.tare()
        while True:
            try:
                #무게센서 값 측정
                val_cup = hx_2.get_weight(5)
                hx_2.power_down()
                hx_2.power_up()
                time.sleep(0.1)
                self.count += 1
            except (self.count == 10):
                return val_cup
                break
    def weight_3(self):
        hx_3.reset()
        hx_3.tare()
        while True:
            try:
                #무게센서 값 측정
                val_cup = hx_3.get_weight(5)
                hx_3.power_down()
                hx_3.power_up()
                time.sleep(0.1)
                self.count += 1
            except (self.count == 10):
                return val_cup
                break
    def weight_4(self):
        hx_4.reset()
        hx_4.tare()
        while True:
            try:
                #무게센서 값 측정
                val_cup = hx_4.get_weight(5)
                hx_4.power_down()
                hx_4.power_up()
                time.sleep(0.1)
                self.count += 1
            except (self.count == 10):
                return val_cup
                break
    def weight_5(self):
        hx_5.reset()
        hx_5.tare()
        while True:
            try:
                #무게센서 값 측정
                val_cup = hx_5.get_weight(5)
                hx_5.power_down()
                hx_5.power_up()
                time.sleep(0.1)
                self.count += 1
            except (self.count == 10):
                return val_cup
                break
    def weight_6(self):
        hx_6.reset()
        hx_6.tare()
        while True:
            try:
                #무게센서 값 측정
                val_cup = hx_6.get_weight(5)
                hx_6.power_down()
                hx_6.power_up()
                time.sleep(0.1)
                self.count += 1
            except (self.count == 10):
                return val_cup
                break
'''

#창설정
win = tk.Tk()
win.title("Hy-personal Booster Vender")
win.geometry("1280x800")
win.resizable(True, True)

#화면전환함수
def change1to2():
    label_subtext.configure(text='인식시켰다면 화면을 클릭해 주세요')
    label_maintext.configure(text='카메라에 qr코드를 인식시켜 주세요')
    label_image.configure(image=image2,command=change2to3)
    wei = Weightsensor()
    wei_1 = wei.weight_cup()
    print(wei_1)
def change2to3():
    label_subtext.configure(text='서버와 연동중입니다')
    label_maintext.configure(text='서버와 연동중입니다')
    label_image.configure(image=image3,command=change3to4)
    label_yesbutton.pack_forget()
    label_nobutton.pack_forget()
def change3to4():
    label_subtext.configure(text='버튼을 클릭해주세요')
    label_maintext.configure(text='선택하신 프로틴이 맞는지 확인해 주세요')
    label_image.configure(image=image4,command=change2to3)
    label_yesbutton.pack(side="left")
    label_nobutton.pack(side="right")
def change4to5():
    label_subtext.configure(text='기다려주세요')
    label_maintext.configure(text='선택한 프로틴을 투하중입니다.')
    label_image.configure(image=image5,command=change5to6)
    label_yesbutton.pack_forget()
    label_nobutton.pack_forget()
def change5to6():
    label_subtext.configure(text='감사합니다')
    label_maintext.configure(text='투하가 완료되었습니다')
    label_image.configure(command=resetscreen)
def resetscreen():
    label_maintext.configure(text="투입구에 텀블러를 올려주세요")
    label_image.configure(image=image1,command=change1to2)
    label_subtext.configure(text="투입하셨다면 클릭해주세요")

#이미지파일
image1 = tkinter.PhotoImage(file="tumblr.png")
image2 = tkinter.PhotoImage(file="qrcode.png")
image3 = tkinter.PhotoImage(file="server.png")
image4 = tkinter.PhotoImage(file="protein.png")
image5 = tkinter.PhotoImage(file="tumblrwithscoop.png")

#초기화면
label_maintext = tkinter.Label(win, text="투입구에 텀블러를 올려주세요", font=("나눔고딕",40))
label_image = tkinter.Button(win, image=image1)
label_subtext = tkinter.Label(win, text="투입하셨다면 클릭해주세요", font=("나눔고딕",40))
label_image = tkinter.Button(win, image=image1, command=change1to2)

label_maintext.pack(expand=1, anchor=CENTER)
label_image.pack(expand=1, anchor=CENTER)
label_subtext.pack(expand=1, anchor=CENTER)
label_yesbutton=tkinter.Button(win, text="YES",command=change4to5,width=10,height=5)
label_nobutton=tkinter.Button(win, text="NO", command=change2to3,width=10,height=5)

#실행
win.mainloop()