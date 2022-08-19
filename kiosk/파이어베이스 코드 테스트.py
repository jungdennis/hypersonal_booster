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
member_db = "1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/members/"
booster_db = "1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/booster/"
barcodetest = "alscksrb0"
userid = barcodetest[:-1]
powdercode = "testcode123"
eat_powder = db.reference(member_db + userid)
eat_powder.update({
    u'eat_powder' : powdercode
})
a = barcodetest[:-1]
print(a)
