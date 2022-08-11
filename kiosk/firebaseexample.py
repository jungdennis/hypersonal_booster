#-*-coding:utf-8 -*-

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from firebase_admin import db
from datetime import datetime

cred = credentials.Certificate("hypersonal-booster-firebase-adminsdk-ikwz1-f2efb4d5f9.json")
firebase_admin.initialize_app(cred,{'databaseURL': 'https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app/1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/members'})
doc_ref = db.reference()
'''
usernumber = 1
username = "abc"
sex = "male"
protein_num = 1
gram = 31
doc_ref.update({
    u'id': usernumber, u'nickname':username, u'sex': sex, u'date': datetime.today()
})
'''
doc = doc_ref.get()
print(doc)
number = 0
text = "1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/members"

ref = db.reference(text + "/" + str(number) + "/f")
print(text)
print(ref.get())
