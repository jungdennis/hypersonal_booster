import RPi.GPIO as GPIO
import time
GPIO.setmode(GPIO.BOARD)
motor1=20
motor2=21
c_step=10
GPIO.setup(motor1,GPIO.OUT,initial=GPIO.LOW)
GPIO.setup(motor2,GPIO.OUT,initial=GPIO.LOW)
p1=GPIO.PWM(motor1,100)
p2=GPIO.PWM(motor2,100)

p1.start(0)
p2.start(0)
try:
	while 1:
		for pw in range(0,101,c_step):
			p1.ChangeDutyCycle(pw)
			time.sleep(0.5)
		time.sleep(0.5)
		for pw in range(0,101,c_step):
			p2.ChangeDutyCycle(pw)
			time.sleep(0.5)
		time.sleep(0.5)
except KeyboardInterrupt:
	pass
p1.stop()
p2.stop()
GPIO.cleanup()