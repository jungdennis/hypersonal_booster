#-*-coding:utf-8 -*-

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from firebase_admin import db
from datetime import datetime

cred = credentials.Certificate("hypersonal-booster-firebase-adminsdk-ikwz1-f2efb4d5f9.json")
firebase_admin.initialize_app(cred,{'databaseURL': 'https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app/1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/members'})
'''
doc_ref = db.reference()
doc = doc_ref.get()
print(doc)
'''
number = 0
userid = "test"
member_db = "1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/apptest/"

booster_db = "1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/booster/"
barcode = "o0Molbhzmqf1GZJAnJYQxDWQ0Mi2,0"
userid = barcode.split(",")[0]
bora = barcode.split(",")[1]
bp = [""]
bpgram = [0]
if bora == "0":
    recommend_db = member_db + userid + "/Booster_before"
    for i in range(1, 4):
        getdata = db.reference(recommend_db + "/bp" + str(i)).get()
        bp.append(getdata.split(',')[0])
        bpgram.append(int(getdata.split(',')[1]))

for i in range(1, 4):
    print("bp%d : %s, bp%d gram : %d" % (i, bp[i], i, bpgram[i]))
