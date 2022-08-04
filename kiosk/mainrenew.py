import tkinter
import tkinter as tk
from tkinter import *
import time
from PIL import Image

from tkinter import ttk


#창설정
win = tk.Tk()
win.title("Hy-personal Booster Vender")
win.geometry("1280x800")
win.resizable(True, True)
win.configure(bg='#FBCA53')
global count
count = 0
#화면전환함수
def clickMouse(event):
    global count
    if count == 0:
        maintext.pack_forget()
        logo_image.pack_forget()
        subtext.pack_forget()
        label_maintext.configure(text="투입구에 텀블러를 올려주세요")
        label_subtext.configure(text="투입하셨다면 클릭해주세요")
        label_image.configure(image=image1)
        label_maintext.pack(side='top',ipady=50)
        label_image.pack(side='top')
        label_subtext.pack(side='top',ipady=50)
        count += 1
        print(count)
    elif count == 1:
        label_maintext.configure(text='카메라에 qr코드를 인식시켜 주세요')
        label_subtext.configure(text='인식시켰다면 화면을 클릭해 주세요')
        label_image.configure(image=image2)
        count += 1
        print(count)
    elif count == 2:
        label_subtext.configure(text='서버와 연동중입니다')
        label_maintext.configure(text='서버와 연동중입니다')
        label_image.configure(image=image3)
        label_yesbutton.place_forget()
        label_nobutton.place_forget()
        count += 1
        print(count)
    elif count == 3:
        label_maintext.configure(text='선택하신 프로틴이 맞는지 확인해 주세요')
        label_subtext.pack_forget()

        #        label_subtext.configure(text='버튼을 클릭해주세요')
        label_image.pack_forget()
        label_powimage.pack(side="top")
        label_powtext_1.pack(side="top")
        label_powtext_2.pack(side="top")
        label_yesbutton.place(x=475,y=700)
        label_nobutton.place(x=730,y=700)
        count += 1
        print(count)
    elif count == 4:
        pass
    elif count == 5:
        label_subtext.configure(text='감사합니다')
        label_maintext.configure(text='투하가 완료되었습니다')
        label_image.configure(image=image6)
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
        print(count)
def yesbtn():
    global count
    count = 5
    label_subtext.configure(text='기다려주세요')
    label_maintext.configure(text='선택한 프로틴을 투하중입니다.')
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
    count = 3
    label_subtext.configure(text='서버와 연동중입니다')
    label_maintext.configure(text='서버와 연동중입니다')
    label_image.configure(image=image3)
    label_powtext_1.pack_forget()
    label_powtext_2.pack_forget()
    label_powimage.pack_forget()
    label_yesbutton.place_forget()
    label_nobutton.place_forget()
    label_image.pack()
    label_subtext.pack()
#이미지파일
image0 = tkinter.PhotoImage(file="images/logo.png")
image1 = tkinter.PhotoImage(file="images/cup.png")
image2 = tkinter.PhotoImage(file="images/qr.png")
image3 = tkinter.PhotoImage(file="images/server.png")
image4 = tkinter.PhotoImage(file="images/protein_resize.png")
image5 = tkinter.PhotoImage(file="images/scoop.png")
image6 = tkinter.PhotoImage(file="images/done.png")
yesimg = tkinter.PhotoImage(file="images/ybtn_resize.png")
noimg = tkinter.PhotoImage(file="images/nbtn_resize.png")
#초기화면
maintext = tkinter.Label(win, text="개인맞춤 운동 보조제 자판기\n""HY-PERSONAL BOOSTER VENDER", font=("HY헤드라인M",40),bg='#FBCA53', width=100, height=7)
logo_image = tkinter.Label(win, image=image0,bg='#FBCA53',width=200, height=200)
subtext = tkinter.Label(win, text="이용하시려면 화면을 클릭해 주세요", font=("HY헤드라인M",35),bg='#FBCA53')
maintext.pack(side='top')
logo_image.pack(side='top')
subtext.pack(side='bottom',ipady=100)

label_maintext = tkinter.Label(win, text="투입구에 텀블러를 올려주세요", font=("HY헤드라인M", 35), bg='#FBCA53')
label_image = tkinter.Label(win, image=image1,bg='#FBCA53')
label_subtext = tkinter.Label(win, text="투입하셨다면 클릭해주세요", font=("HY헤드라인M",35),bg='#FBCA53')
label_powtext_1 = tkinter.Label(win, text="WHI", font=("HY헤드라인M",20),bg='#FBCA53')
label_powtext_2 = tkinter.Label(win, text="40g", font=("HY헤드라인M",20),bg='#FBCA53')
label_powimage = tkinter.Label(win, image=image4, bg='#FBCA53')
label_yesbutton = tkinter.Button(win, image=yesimg,command=yesbtn, width=70,height=70)
label_nobutton = tkinter.Button(win, image=noimg, command=nobtn, width=70,height=70)
#label_yesbutton=tkinter.Button(win, text="YES",command=change4to5,width=10,height=5)
#label_nobutton=tkinter.Button(win, text="NO", command=change2to3,width=10,height=5)

#실행
win.bind("<Button>", clickMouse)
win.mainloop()