from gpiozero import Motor
import time

md1 = Motor(forward = 20, backward = 21)
md2 = Motor(forward = 5, backward = 6)
md3 = Motor(forward = 13, backward = 19)

## 모터 : 1-20, 2-21, 3-5, 4-6, 5-13, 6-19

while True:
    md1.forward()
    time.sleep(5)

    md1.backward()
    time.sleep(5)

    md2.forward()
    time.sleep(5)

    md2.backward()
    time.sleep(5)

    md3.forward()
    time.sleep(5)

    md3.backward()
    time.sleep(5)








