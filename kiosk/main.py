import tkinter
import tkinter as tk
from tkinter import ttk
from tkinter import *
# test
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
    label_image.configure(image=image3,command=change3to4)
def change3to4():
    label_subtext.configure(text='선택하신 프로틴이 아니라면 아래 버튼을 클릭해주세요')
    label_maintext.configure(text='선택하신 프로틴이 맞는지 확인해 주세요')
    label_image.configure(image=image4)
    label_yesbutton=tkinter.Button(win, text="YES",command=change4to5).pack(side="left")
    label_nobutton=tkinter.Button(win, text="NO", command=change2to3).pack(side="right")
def change4to5():
    label_subtext.configure(text='기다려주세요')
    label_maintext.configure(text='선택한 프로틴을 투하중입니다.')
    label_image.configure(image=image5)


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
#화면전환1


#실행
win.mainloop()