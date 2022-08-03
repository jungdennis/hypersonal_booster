import tkinter
import tkinter as tk
from tkinter import ttk
from tkinter import *

#창설정
win = tk.Tk()
win.title("Hy-personal Booster Vender")
win.geometry("1280x800")
win.resizable(True, True)
win['bg']='#FBCA53'

# region 화면전환함수
def mousePressevent
def change1to2():
    label_subtext.configure(text='인식시켰다면 화면을 클릭해 주세요')
    label_maintext.configure(text='카메라에 qr코드를 인식시켜 주세요')
    label_image.configure(image=image2,command=change2to3)
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
    label_yesbutton.pack(side="left",padx=70,pady=40)
    label_nobutton.pack(side="right",padx=70,pady=40)
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
# endregion
#이미지파일
#image0 = tkinter.PhotoImage(file="test.svg")
image1 = tkinter.PhotoImage(file="tumblr.png")
image2 = tkinter.PhotoImage(file="qrcode.png")
image3 = tkinter.PhotoImage(file="server.png")
image4 = tkinter.PhotoImage(file="protein.png")
image5 = tkinter.PhotoImage(file="tumblrwithscoop.png")

#label_mainpage = tkinter.Label(win, text="개인맞춤 운동 보조제 자판기\nHY-PERSONAL  BOOSTER VENDER\n", font=("HY헤드라인M",40))
#초기화면
label_maintext = tkinter.Label(win, text="투입구에 텀블러를 올려주세요", font=("나눔고딕",40), bg='#FBCA53')
label_image = tkinter.Button(win, image=image1,bg='#FBCA53')
label_subtext = tkinter.Label(win, text="투입하셨다면 클릭해주세요", font=("나눔고딕",40),bg='#FBCA53')
label_image = tkinter.Button(win, image=image1, command=change1to2,bg='#FBCA53')

label_maintext.pack(expand=1)
label_image.pack(expand=1)
label_subtext.pack(expand=1)
label_yesbutton=tkinter.Button(win, text="YES",command=change4to5,bg='#FBCA53')
label_nobutton=tkinter.Button(win, text="NO", command=change2to3,bg='#FBCA53')

#실행
win.mainloop()