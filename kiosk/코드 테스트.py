import tkinter as tk
window = tk.Tk()
import tkinter.font as tkFont

def call1():
    print(round(0.12345,2))

window.geometry('200x200')

frame = tk.Frame(window)
frame.pack()

button1 = tk.Button(frame, text ="Button 1", command = call1,
                    fg = 'blue',font ='Hack', bd = 5,relief = "flat",activebackground='#FBCA53')
button1.pack(pady = 5)

print(list(tkFont.families()))

window.mainloop()