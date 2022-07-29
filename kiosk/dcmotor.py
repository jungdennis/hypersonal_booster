from gpiozero import Motor
import time

md1 = Motor(forward=20, backward=21)
#md2 = Motor(forward=12, backward=16)
#md3 = Motor(forward=7, backward=8)

motorcontrol = 0

def whichmotor(motorcontrol):
    if motorcontrol == 0:
        md1.forward(speed=0.3)
        time.sleep(3)
    elif motorcontrol == 1:
        md1.backward(speed=0.3)
        time.sleep(3)
'''
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
'''
while True:
    md1.forward(speed=0.3)
    time.sleep(3)
    md1.backward(speed=0.3)
    time.sleep(3)