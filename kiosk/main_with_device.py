import tkinter
import tkinter as tk
from tkinter import ttk
from tkinter import *

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
def change2to3():
    label_subtext.configure(text='서버와 연동중입니다')
    label_maintext.configure(text='서버와 연동중입니다')
    label_image.configure(image=image1)

#초기화면
image1 = tkinter.PhotoImage(file="tumblr.png")
image2 = tkinter.PhotoImage(file="qrcode.png")

label_maintext = tkinter.Label(win, text="투입구에 텀블러를 올려주세요", font=("나눔고딕",40))
label_image = tkinter.Button(win, image=image1)
label_subtext = tkinter.Label(win, text="투입하셨다면 클릭해주세요", font=("나눔고딕",40))
label_image = tkinter.Button(win, image=image1, command=change1to2)

label_maintext.pack(expand=1, anchor=CENTER)
label_image.pack(expand=1, anchor=CENTER)
label_subtext.pack(expand=1, anchor=CENTER)

#실행
win.mainloop()