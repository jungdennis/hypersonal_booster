#region 라이브러리
import sys
from PyQt5.QtWidgets import *
from PyQt5 import uic
from PyQt5.QtGui import *
from PyQt5.QtWidgets import QApplication, QWidget, QPushButton, QVBoxLayout
import os
import mouse
#endregion

#region GUI 설정
form_class = uic.loadUiType("uis/mainpage.ui")[0]
global page_number
# 화면을 띄우는데 사용되는 Class 선언
class WindowClass(QMainWindow, form_class):
    def __init__(self):
        super().__init__()
        self.setupUi(self)
        self.stackedWidget: QStackedWidget  # class type 지정해주기
        self.stackedWidget.setCurrentIndex(0)
        self.page_number = 0
        self.yesbtn.clicked.connect(self.yesbuttonFunction)
        self.nobtn.clicked.connect(self.nobuttonFunction)
        #region 이미지파일 설정
        self.qPixmapVar = QPixmap()
        self.qPixmapVar.load("images/test.svg")
        self.picture.setPixmap(self.qPixmapVar)
        self.qPixmapVar1 = QPixmap()
        self.qPixmapVar1.load("images/cup.svg")
        self.page_1_image.setPixmap(self.qPixmapVar1)
        self.qPixmapVar2 = QPixmap()
        self.qPixmapVar2.load("images/qr.svg")
        self.page_2_image.setPixmap(self.qPixmapVar2)
        self.qPixmapVar3 = QPixmap()
        self.qPixmapVar3.load("images/server.svg")
        self.page_3_image.setPixmap(self.qPixmapVar3)
        self.qPixmapVar4 = QPixmap()
        self.qPixmapVar4.load("images/protein.png")
        self.qPixmapVar4.scaledToWidth(400)
        self.page_4_image.setPixmap(self.qPixmapVar4)

        self.qPixmapVar5 = QPixmap()
        self.qPixmapVar5.load("images/scoop.svg")
        self.page_5_image.setPixmap(self.qPixmapVar5)
        self.qPixmapVar6 = QPixmap()
        self.qPixmapVar6.load("images/done.svg")
        self.page_6_image.setPixmap(self.qPixmapVar6)
        self.yesbtn.setStyleSheet('border-image:url("images/ybtn.png");border:0px;')
        self.nobtn.setStyleSheet('border-image:url("images/nbtn.png");border:0px;')
        #endregion
    def mousePressEvent(self, e):
        if self.page_number == 0:
            self.stackedWidget.setCurrentIndex(1)
            self.page_number += 1
        elif self.page_number == 1:
            self.stackedWidget.setCurrentIndex(2)
            self.page_number += 1
        elif self.page_number == 2:
            self.stackedWidget.setCurrentIndex(3)
            self.page_number += 1
        elif self.page_number == 3:
            self.stackedWidget.setCurrentIndex(4)
            self.page_number += 1
        elif self.page_number == 4:
            pass
        elif self.page_number == 5:
            self.stackedWidget.setCurrentIndex(6)
            self.page_number += 1
        elif self.page_number == 6:
            self.stackedWidget.setCurrentIndex(0)
            self.page_number = 0
    def yesbuttonFunction(self):
        self.stackedWidget.setCurrentIndex(5)
        self.page_number = 5
    def nobuttonFunction(self):
        self.stackedWidget.setCurrentIndex(3)
        self.page_number = 3
#endregion

if __name__ == "__main__":
    app = QApplication(sys.argv)
    myWindow = WindowClass()
    myWindow.show()
    app.exec_()