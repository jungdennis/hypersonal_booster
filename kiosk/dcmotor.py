from gpiozero import Motor
import time

#8번핀은 더미핀 사용안할거임, 나머지핀은 모터 한쪽으로 돌리는 핀
dcmotor_1 =Motor(forward=14, backward=8)
dcmotor_2 =Motor(forward=15, backward=8)
dcmotor_3 =Motor(forward=18, backward=8)
dcmotor_4 =Motor(forward=23, backward=8)
dcmotor_5 =Motor(forward=24, backward=8)
dcmotor_6 =Motor(forward=25, backward=8)

motorcontrol = 0

#모터제어식
if motorcontrol == 0:
    dcmotor_1.forward(speed=0.3)
    time.sleep(5)
elif motorcontrol == 1:
    dcmotor_2.forward(speed=0.3)
    time.sleep(5)
elif motorcontrol == 2:
    dcmotor_3.forward(speed=0.3)
    time.sleep(5)
elif motorcontrol == 3:
    dcmotor_4.forward(speed=0.3)
    time.sleep(5)
elif motorcontrol == 4:
    dcmotor_5.forward(speed=0.3)
    time.sleep(5)
elif motorcontrol == 5:
    dcmotor_6.forward(speed=0.3)
    time.sleep(5)
