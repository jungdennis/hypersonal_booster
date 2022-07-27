import tkinter
import tkinter as tk
from tkinter import ttk
from tkinter import *
import sys
import time
import multiprocessing

# 무게센서hx711설정
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


# dc모터설정
from gpiozero import Motor
import time

md1 = Motor(forward=20, backward=21)
md2 = Motor(forward=12, backward=16)
md3 = Motor(forward=7, backward=8)
def whichmotor(motorcontrol):
    if motorcontrol == 0:
        md1.forward(speed=0.3)
        time.sleep(3)
    elif motorcontrol == 1:
        md1.backward(speed=0.3)
        time.sleep(3)
    elif motorcontrol == 2:
        md2.forward(speed=0.3)
        time.sleep(3)
    elif motorcontrol == 3:
        md2.backward(speed=0.3)
        time.sleep(3)
    elif motorcontrol == 4:
        md3.forward(speed=0.3)
        time.sleep(3)
    elif motorcontrol == 5:
        md3.backrward(speed=0.3)
        time.sleep(3)


# 텀블러, 원료통1~6세팅
hx_main = HX711(26, 19)
hx_1 = HX711(13, 6)
hx_2 = HX711(5, 11)
hx_3 = HX711(9, 10)
hx_4 = HX711(22, 27)
hx_5 = HX711(17, 4)
hx_6 = HX711(3, 2)
# 무게센서 초기 오차값(텀블러는 이미 측정)```
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


class Weightsensor:
    def __init__(self):
        self.count = 0
    def weight_cup(self):
        hx_main.reset()
        hx_main.tare()
        while TRUE:
            #무게 측정
            val_cup = round(hx_main.get_weight(5))
            hx_main.power_down()
            hx_main.power_up()
            time.sleep(0.1)
            print("컵 센서 측정값: %f" % val_cup)
    def weight_1(self):
        hx_1.reset()
        hx_1.tare()
        while TRUE:
            val_1 = round(hx_1.get_weight(5),1)
            hx_1.power_down()
            hx_1.power_up()
            time.sleep(0.1)
            print("원료통1 측정값: %f" % val_1)
    def weight_2(self):
        hx_2.reset()
        hx_2.tare()
        while TRUE:
            val_2 = round(hx_2.get_weight(5),1)
            hx_2.power_down()
            hx_2.power_up()
            time.sleep(0.1)
            print("원료통2 측정값: %f" % val_2)
    def weight_3(self):
        hx_3.reset()
        hx_3.tare()
        while TRUE:
            val_3 = round(hx_3.get_weight(5),1)
            hx_3.power_down()
            hx_3.power_up()
            time.sleep(0.1)
            print("원료통3 측정값: %f" % val_3)
    def weight_4(self):
        hx_4.reset()
        hx_4.tare()
        while TRUE:
            val_4 = round(hx_4.get_weight(5),1)
            hx_4.power_down()
            hx_4.power_up()
            time.sleep(0.1)
            print("원료통4 측정값: %f" % val_4)
    def weight_5(self):
        hx_5.reset()
        hx_5.tare()
        while TRUE:
            val_5 = round(hx_5.get_weight(5), 1)
            hx_5.power_down()
            hx_5.power_up()
            time.sleep(0.1)
            print("원료통5 측정값: %f" % val_5)
    def weight_6(self):
        hx_6.reset()
        hx_6.tare()
        while TRUE:
            val_6 = round(hx_6.get_weight(5), 1)
            hx_6.power_down()
            hx_6.power_up()
            time.sleep(0.1)
            print("원료통6 측정값: %f" % val_6)


# 창설정
win = tk.Tk()
win.title("Hy-personal Booster Vender")
win.geometry("1280x800")
win.resizable(True, True)


# 화면전환함수
def change1to2():
    label_subtext.configure(text='인식시켰다면 화면을 클릭해 주세요')
    label_maintext.configure(text='카메라에 qr코드를 인식시켜 주세요')
    label_image.configure(image=image2, command=change2to3)
#    wei = Weightsensor()
#    wei_1 = wei.weight_cup()
#    print(wei_1)
def change2to3():
    label_subtext.configure(text='서버와 연동중입니다')
    label_maintext.configure(text='서버와 연동중입니다')
    label_image.configure(image=image3, command=change3to4)
    label_yesbutton.pack_forget()
    label_nobutton.pack_forget()
def change3to4():
    label_subtext.configure(text='버튼을 클릭해주세요')
    label_maintext.configure(text='선택하신 프로틴이 맞는지 확인해 주세요')
    label_image.configure(image=image4, command=change2to3)
    label_yesbutton.pack(side="left")
    label_nobutton.pack(side="right")


def change4to5():
    label_subtext.configure(text='기다려주세요')
    label_maintext.configure(text='선택한 프로틴을 투하중입니다.')
    label_image.configure(image=image5, command=change5to6)
    label_yesbutton.pack_forget()
    label_nobutton.pack_forget()
    '''
    time.sleep(1)
    whichmotor(0)
    time.sleep(1)
    whichmotor(1)

    time.sleep(1)
    whichmotor(2)
    time.sleep(1)
    whichmotor(3)
    time.sleep(1)
    whichmotor(4)
    time.sleep(1)
    whichmotor(5)
'''

def change5to6():
    label_subtext.configure(text='감사합니다')
    label_maintext.configure(text='투하가 완료되었습니다')
    label_image.configure(command=resetscreen)


def resetscreen():
    label_maintext.configure(text="투입구에 텀블러를 올려주세요")
    label_image.configure(image=image1, command=change1to2)
    label_subtext.configure(text="투입하셨다면 클릭해주세요")


# 이미지파일
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

# 실행
if __name__ == "__main__":
    #무게센서 클래스 지정
    wei_cup = Weightsensor()
    wei_1 = Weightsensor()
    wei_2 = Weightsensor()
    wei_3 = Weightsensor()
    wei_4 = Weightsensor()
    wei_5 = Weightsensor()
    wei_6 = Weightsensor()

    mp_gui = multiprocessing.Process(target=win.mainloop)
    mp_wei_cup = multiprocessing.Process(target=wei_cup.weight_cup)
    mp_wei_1 = multiprocessing.Process(target=wei_1.weight_1)
    mp_wei_2 = multiprocessing.Process(target=wei_2.weight_2)
    mp_wei_3 = multiprocessing.Process(target=wei_3.weight_3)
    mp_wei_4 = multiprocessing.Process(target=wei_4.weight_4)
    mp_wei_5 = multiprocessing.Process(target=wei_5.weight_5)
    mp_wei_6 = multiprocessing.Process(target=wei_6.weight_6)

    mp_gui.start()
    mp_wei_cup.start()
    mp_wei_1.start()
    mp_wei_2.start()
    mp_wei_3.start()
    mp_wei_4.start()
    mp_wei_5.start()
    mp_wei_6.start()

    mp_gui.join()
    mp_wei_cup.join()
    mp_wei_1.join()
    mp_wei_2.join()
    mp_wei_3.join()
    mp_wei_4.join()
    mp_wei_5.join()
    mp_wei_6.join()